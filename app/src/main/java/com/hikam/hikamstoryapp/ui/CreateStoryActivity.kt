package com.hikam.hikamstoryapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hikam.hikamstoryapp.viewmodel.ViewModelFactory
import com.hikam.hikamstoryapp.data.ResultState
import com.hikam.hikamstoryapp.getImageUri
import com.hikam.hikamstoryapp.reduceFileImage
import com.hikam.hikamstoryapp.uriToFile
import com.hikam.hikamstoryapp.R
import com.hikam.hikamstoryapp.databinding.ActivityCreateStoryBinding
import com.hikam.hikamstoryapp.viewmodel.CreateStoryViewModel

class CreateStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStoryBinding

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<CreateStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            showImage()
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.editDeskripsi.text.toString()

            viewModel.uploadImage(imageFile, description).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showToast(result.data.message)
                        showLoading(false)
                        navigateToMain()
                    }
                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning_message))
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
