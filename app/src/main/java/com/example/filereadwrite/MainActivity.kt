package com.example.filereadwrite

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var tvData: TextView

    companion object {
        const val REQUEST_CODE_WRITE_PERM = 401
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvData = findViewById(R.id.tvData)

        val btnWriteFile: Button = findViewById(R.id.btnWriteFile)
        btnWriteFile.setOnClickListener {
            writeFile("Hello: " + Date(System.currentTimeMillis()).toString())
        }

        val btnReadFile: Button = findViewById(R.id.btnReadFile)
        btnReadFile.setOnClickListener {
            val data = readFile()
            tvData.text = data
        }

        requestNeededPermission()
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "I need it for File", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_PERM)
        } else {
            Toast.makeText(this, "Already have permission", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_WRITE_PERM -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "WRITE_EXTERNAL_STORAGE perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "WRITE_EXTERNAL_STORAGE perm NOT granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun writeFile(data: String) {
        val file = Environment.getExternalStorageDirectory().toString() + "/test.txt"

        Toast.makeText(this, file, Toast.LENGTH_LONG).show()

        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(file)
            os.write(data.toByteArray())
            os.flush()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun readFile(): String {
        var result = ""
        val file = Environment.getExternalStorageDirectory().toString() + "/test.txt"
        var inputStream: FileInputStream? = null

        try {
            inputStream = FileInputStream(file)
            val byteStream = ByteArrayOutputStream()
            var ch: Int
            while (inputStream.read().also { ch = it } != -1) {
                byteStream.write(ch)
            }
            result = byteStream.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }
}