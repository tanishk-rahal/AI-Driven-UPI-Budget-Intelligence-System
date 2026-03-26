package com.example.budgetmanager.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetmanager.BuildConfig
import com.example.budgetmanager.data.AppDatabase
import com.example.budgetmanager.data.ExpenseLog
import com.example.budgetmanager.databinding.ActivityPaymentBinding
import com.example.budgetmanager.network.RetrofitClient
import com.example.budgetmanager.network.models.CreateOrderRequest
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityPaymentBinding

    private var upiId: String?        = null
    private var receiverName: String? = null
    private var currentOrderId: String? = null
    private var currentAmount: Double   = 0.0
    private var currentNote: String     = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        upiId        = intent.getStringExtra("UPI_ID")
        receiverName = intent.getStringExtra("PAYEE_NAME")

        binding.tvPayeeName.text = receiverName ?: "Unknown"
        binding.tvUpiId.text     = upiId ?: ""

        Checkout.preload(applicationContext)

        binding.btnPay.setOnClickListener { validateAndPay() }
    }

    private fun validateAndPay() {
        val amountText = binding.etAmount.text.toString().trim()
        val note       = binding.etReason.text.toString().trim()

        val amount = amountText.toDoubleOrNull()
        when {
            amountText.isBlank() -> {
                binding.etAmount.error = "Enter an amount"
                return
            }
            amount == null || amount <= 0 -> {
                binding.etAmount.error = "Enter a valid amount"
                return
            }
            amount > 500000 -> {
                binding.etAmount.error = "Amount cannot exceed ₹5,00,000"
                return
            }
        }
        when {
            note.isBlank() -> {
                binding.etReason.error = "Enter a reason"
                return
            }
            note.length > 45 -> {
                binding.etReason.error = "Reason must be under 45 characters"
                return
            }
        }

        currentAmount = amount!!
        currentNote   = note

        binding.btnPay.isEnabled       = false
        binding.progressBar.visibility = View.VISIBLE

        createOrderAndPay(amount, note)
    }

    private fun createOrderAndPay(amount: Double, note: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createOrder(
                    CreateOrderRequest(amount = amount, note = note)
                )
                if (response.success) {
                    currentOrderId = response.orderId
                    openRazorpayCheckout(
                        orderId     = response.orderId,
                        amountPaise = response.amount,
                        note        = note
                    )
                } else {
                    handleError("Order creation failed — try again")
                }
            } catch (e: Exception) {
                handleError("Server unreachable: ${e.message}")
            }
        }
    }

    private fun openRazorpayCheckout(
        orderId: String,
        amountPaise: Int,
        note: String
    ) {
        binding.progressBar.visibility = View.GONE

        val checkout = Checkout()
        checkout.setKeyID(BuildConfig.RAZORPAY_KEY_ID)

        try {
            val options = JSONObject().apply {
                put("name",        "Budget Manager")
                put("description", note)
                put("order_id",    orderId)
                put("amount",      amountPaise)
                put("currency",    "INR")
                put("prefill", JSONObject().apply {
                    put("method", "upi")
                    put("vpa",    upiId ?: "")
                })
                put("method", JSONObject().apply {
                    put("upi",        true)
                    put("card",       false)
                    put("netbanking", false)
                    put("wallet",     false)
                })
                put("theme", JSONObject().apply {
                    put("color", "#6366F1")
                })
            }
            checkout.open(this, options)
        } catch (e: Exception) {
            handleError("Checkout error: ${e.message}")
        }
    }

    override fun onPaymentSuccess(paymentId: String?) {
        binding.btnPay.isEnabled = true
        saveExpense(status = "SUCCESS", paymentId = paymentId ?: "")
        Toast.makeText(this,
            "Payment successful! Expense logged.",
            Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onPaymentError(code: Int, description: String?) {
        binding.btnPay.isEnabled = true
        saveExpense(status = "FAILED", paymentId = "")
        val message = when (code) {
            Checkout.PAYMENT_CANCELED -> "Payment cancelled"
            Checkout.NETWORK_ERROR    -> "Network error — check connection"
            Checkout.INVALID_OPTIONS  -> "App configuration error"
            else                      -> "Payment failed — please retry"
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun saveExpense(status: String, paymentId: String) {
        val expense = ExpenseLog(
            id          = UUID.randomUUID().toString(),
            upiId       = upiId ?: "",
            payeeName   = receiverName ?: "",
            amount      = currentAmount,
            note        = currentNote,
            category    = autoCategory(currentNote),
            status      = status,
            paymentId   = paymentId,
            orderId     = currentOrderId ?: "",
            timestamp   = System.currentTimeMillis(),
            isConfirmed = status == "SUCCESS"
        )
        lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(applicationContext)
                .expenseDao()
                .insert(expense)
        }
    }

    private fun autoCategory(note: String): String {
        val n = note.lowercase()
        return when {
            n.containsAny("food","chai","coffee","lunch","dinner",
                "breakfast","snack","restaurant","eat","swiggy","zomato")
                -> "Food"
            n.containsAny("uber","ola","auto","metro","cab","bus",
                "petrol","train","travel","rapido")
                -> "Transport"
            n.containsAny("grocery","milk","vegetables","kirana",
                "sabzi","fruits","blinkit","zepto","bigbasket")
                -> "Groceries"
            n.containsAny("medicine","doctor","pharmacy","hospital",
                "medical","health","clinic","apollo")
                -> "Healthcare"
            n.containsAny("bill","electricity","recharge","broadband",
                "internet","water","gas","postpaid")
                -> "Utilities"
            n.containsAny("movie","game","netflix","spotify","prime",
                "hotstar","entertainment","fun")
                -> "Entertainment"
            else -> "Others"
        }
    }

    private fun handleError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.btnPay.isEnabled       = true
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun String.containsAny(vararg words: String) =
        words.any { this.contains(it) }
}
