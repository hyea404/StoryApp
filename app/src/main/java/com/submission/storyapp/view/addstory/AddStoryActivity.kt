package com.submission.storyapp.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.submission.storyapp.R
import com.submission.storyapp.databinding.ActivityAddStoryBinding
import com.submission.storyapp.helper.ViewModelFactory
import com.submission.storyapp.helper.getImageUri
import com.submission.storyapp.helper.reduceFileImage
import com.submission.storyapp.helper.uriToFile
import com.submission.storyapp.helper.Result
import com.submission.storyapp.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            requestPermission.launch(REQUIRED_PERMISSION)
        }

        val launcherIntentCamera = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { isSuccess ->
            if (isSuccess) {
                showImg()
            }
        }

        postAddStory()

        binding.btnCamera.setOnClickListener {
            val uri = getImageUri(this)
            currentImageUri = uri
            uri?.let {
                launcherIntentCamera.launch(it)
            } ?: showToast(getString(R.string.empty_image))
        }

        binding.btnGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.uploadBtn.setOnClickListener {
            var token: String
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                val description = binding.etDescription.text.toString()

                if (description.isEmpty()) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Please tell us about your story!")
                        setMessage(getString(R.string.empty_description))
                        setCancelable(false)
                        setPositiveButton(getString(R.string.ok_message)) { _, _ ->
                            val intent = Intent(context, AddStoryActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    showLoading(true)

                    val requestBody = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile
                    )

                    viewModel.getSession().observe(this) { user ->
                        token = user.token
                        viewModel.addStory(token, multipartBody, requestBody)
                    }
                }
            } ?: showToast(getString(R.string.empty_image))
        }
    }

    private fun postAddStory() {
        viewModel.insertStoryResponse.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                    disableInterface()
                }

                is Result.Success -> {
                    showLoading(false)
                    enableInterface()
                    AlertDialog.Builder(this).apply {
                        setTitle("Congrats!")
                        setMessage(getString(R.string.upload_message))
                        setCancelable(false)
                        setPositiveButton(getString(R.string.next)) { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    showLoading(false)
                    enableInterface()
                }
            }
        }
    }

    private fun enableInterface() {
        binding.btnCamera.isEnabled = true
        binding.btnGallery.isEnabled = true
        binding.uploadBtn.isEnabled = true
        binding.etDescription.isEnabled = true
    }

    private fun disableInterface() {
        binding.btnCamera.isEnabled = false
        binding.btnGallery.isEnabled = false
        binding.uploadBtn.isEnabled = false
        binding.etDescription.isEnabled = false
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        when (isGranted) {
            true -> Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            false -> Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImg()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun showImg() {
        currentImageUri?.let {
            Log.d("Image URI", "show Image:$it")
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator1.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}