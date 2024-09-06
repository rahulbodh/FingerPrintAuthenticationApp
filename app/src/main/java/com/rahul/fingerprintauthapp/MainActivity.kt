package com.rahul.fingerprintauthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private lateinit var biometricManager: BiometricManager
    private lateinit var executor: Executor

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)){
            BiometricManager.BIOMETRIC_SUCCESS ->
                Toast.makeText(this , "Biometric authentication is available" , Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(this , "Biometric authentication is not available on this device" , Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(this , "Biometric authentication is not available on this device" , Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(this , "No fingerprint enrolled" , Toast.LENGTH_SHORT).show()
        }

        executor  = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
//                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
//                Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                // You can open your app or perform your action here
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
//                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })

        // Set up the BiometricPrompt info
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for My App")
            .setSubtitle(" ")
            .setNegativeButtonText("Use Password")
            .setConfirmationRequired(true) // If true, the user needs to confirm after biometric
            .build()


        biometricPrompt.authenticate(promptInfo)


    }
}