package com.joopda.qrscanexample

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.zxing.integration.android.IntentIntegrator
import com.joopda.qrscanexample.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        getHashKey()

        binding.startScanButton.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setBarcodeImageEnabled(false)
            integrator.setBeepEnabled(true)
            integrator.setPrompt("QR 코드를 읽어주세요")
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.captureActivity = ScanActivity::class.java
            integrator.initiateScan()
        }

        binding.startMap.setOnClickListener {
            val i = Intent(this, MapActivity::class.java)
            startActivity(i)
        }
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                Log.e("INFO: ", "${result.contents}")
                if(result.contents.startsWith("http")){
                    val uri = Uri.parse(result.contents)
                    val i = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(i)
                }else{
                    Toast.makeText(this, "${result.contents}", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }

            if (result.barcodeImagePath != null) {
                val bm = BitmapFactory.decodeFile(result.barcodeImagePath)
                Glide.with(this).load(bm).into(binding.scannedImage)
                binding.scannedImage.setImageBitmap(bm)
            }else{
                Log.e("Image", "Bitmap Null")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}