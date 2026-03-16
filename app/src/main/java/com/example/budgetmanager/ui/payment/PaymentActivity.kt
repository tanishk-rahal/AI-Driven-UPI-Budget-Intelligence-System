package com.example.budgetmanager.ui.payment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetmanager.databinding.ActivityPaymentBinding
import java.net.URLEncoder

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var upiId: String? = null
    private var receiverName: String? = null

    // Activity Result API to handle payment response
    private val upiPaymentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        if (result.resultCode == Activity.RESULT_OK || result.resultCode == 11) {
            if (data != null) {
                val response = data.getStringExtra("response")
                Log.d("UPI_PAYMENT", "Response: $response")
                parseUpiResponse(response)
            } else {
                Log.d("UPI_PAYMENT", "Response data is null")
                Toast.makeText(this, "Transaction failed or cancelled", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("UPI_PAYMENT", "Payment cancelled by user")
            Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        val scannedData = intent.getStringExtra("scanned_data")
        parseUpiUri(scannedData)

        setupUI()
    }

    private fun parseUpiUri(uriString: String?) {
        if (uriString == null) return
        try {
            val uri = Uri.parse(uriString)
            if (uri.scheme == "upi") {
                upiId = uri.getQueryParameter("pa")
                receiverName = uri.getQueryParameter("pn")
            } else {
                upiId = uriString
            }
        } catch (e: Exception) {
            upiId = uriString
        }
    }

    private fun setupUI() {
        binding.tvReceiverInfo.text = receiverName ?: upiId ?: "Receiver name or UPI ID"
        
        binding.btnBack.setOnClickListener {
            finish()
        }

        val categories = arrayOf("Select category...", "Food & Dining", "Transport", "Shopping", "Healthcare", "Entertainment", "Bills & Utilities")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.categorySpinner.adapter = adapter

        binding.btnContinue.setOnClickListener {
            val amount = binding.etAmount.text.toString()
            val buyingInfo = binding.etBuying.text.toString()
            val additionalNote = binding.etNote.text.toString()
            val note = if (buyingInfo.isNotEmpty()) buyingInfo else if (additionalNote.isNotEmpty()) additionalNote else "Payment"
            
            val payeeVpa = upiId ?: ""
            val payeeName = receiverName ?: "Receiver"

            if (amount.isEmpty() || amount == "0") {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (payeeVpa.isEmpty()) {
                Toast.makeText(this, "Invalid UPI ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            launchUpiPayment(payeeVpa, payeeName, amount, note)
        }
    }

    private fun launchUpiPayment(pa: String, pn: String, am: String, tn: String) {
        val transactionId = "T${System.currentTimeMillis()}"
        val transactionRefId = "R${System.currentTimeMillis()}"
        val merchantCode = "0000"
        
        val encodedPn = try {
            URLEncoder.encode(pn, "UTF-8")
        } catch (e: Exception) {
            pn
        }

        val upiUri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", pa)
            .appendQueryParameter("pn", encodedPn)
            .appendQueryParameter("mc", merchantCode)
            .appendQueryParameter("tr", transactionRefId)
            .appendQueryParameter("tid", transactionId)
            .appendQueryParameter("tn", tn)
            .appendQueryParameter("am", am)
            .appendQueryParameter("cu", "INR")
            .build()

        Log.d("UPI_PAYMENT", "Generated URI: $upiUri")

        val upiIntent = Intent(Intent.ACTION_VIEW)
        upiIntent.data = upiUri

        // On Android 11+ (API 30), intent.resolveActivity might return null
        // even if UPI apps exist, due to package visibility restrictions.
        // The most reliable way is to try starting the activity directly.
        try {
            val chooser = Intent.createChooser(upiIntent, "Pay with...")
            upiPaymentLauncher.launch(chooser)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No UPI app installed", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseUpiResponse(response: String?) {
        if (response == null) {
            Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show()
            return
        }

        val responseMap = mutableMapOf<String, String>()
        val parts = response.split("&")
        for (part in parts) {
            val keyValue = part.split("=")
            if (keyValue.size >= 2) {
                responseMap[keyValue[0].lowercase()] = keyValue[1]
            }
        }

        val status = responseMap["status"]?.uppercase()
        when (status) {
            "SUCCESS" -> {
                Toast.makeText(this, "Transaction Successful", Toast.LENGTH_LONG).show()
                finish()
            }
            "FAILURE" -> {
                Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show()
            }
            "SUBMITTED" -> {
                Toast.makeText(this, "Transaction Submitted", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Transaction Cancelled or Failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}
