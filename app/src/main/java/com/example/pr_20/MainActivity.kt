package com.example.pr_20

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var wallpaperLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageView.setImageURI(uri)
                }
            }
        }

        wallpaperLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    setAppBackground(uri)
                }
            }
        }

        val browserButton: Button = findViewById(R.id.browser)
        browserButton.setOnClickListener {
            val url = "https://www.google.com"
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        }

        val youtubeButton: Button = findViewById(R.id.youtube)
        youtubeButton.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, "https://www.youtube.com/watch?v=dQw4w9WgXcQ".toUri())
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)
        }

        val photoButton: Button = findViewById(R.id.photo)
        imageView = findViewById(R.id.imageView)

        photoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        val photoChooserBtn: Button = findViewById(R.id.photo_default)
        photoChooserBtn.setOnClickListener {
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val chooser = Intent.createChooser(pickIntent, "Выберите приложение")
            imagePickerLauncher.launch(chooser)
        }

        val callButton: Button = findViewById(R.id.call_button)
        callButton.setOnClickListener {
            val phoneNumber = "1234567890"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = "tel:$phoneNumber".toUri()
            startActivity(intent)
        }

        val backgroundButton: Button = findViewById(R.id.background_picker_button)
        backgroundButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            wallpaperLauncher.launch(intent)
        }
    }

    private fun setAppBackground(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val drawable = Drawable.createFromStream(inputStream, uri.toString())
            val mainLayout: View = findViewById(R.id.main)
            mainLayout.background = drawable
            Toast.makeText(this, "Фон приложения изменен!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("MainActivity", "Ошибка при установке фона", e)
            Toast.makeText(this, "Ошибка при установке фона: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}