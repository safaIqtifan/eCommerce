package com.example.ecommerce

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_admin_add_new_product.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AdminAddNewProductActivity : AppCompatActivity() {

    lateinit var CategoryName:String
    lateinit var Description:String
    lateinit var Price:String
    lateinit var Pname:String
    lateinit var saveCurrentDate:String
    lateinit var saveCurrentTime:String
    lateinit var AddNewProductButton: Button
    lateinit var InputProductImage: ImageView
    lateinit var InputProductName:EditText
   lateinit var InputProductDescription:EditText
    lateinit var InputProductPrice:EditText
    lateinit var ImageUri:Uri
    lateinit var productRandomKey:String
    lateinit var downloadImageUrl:String
    lateinit var ProductImagesRef:StorageReference
    lateinit var ProductsRef:DatabaseReference
    lateinit var loadingBar:ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_new_product)


        CategoryName = getIntent().getExtras()?.get("category").toString()
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images")
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products")
        AddNewProductButton = findViewById(R.id.add_new_product)
        InputProductImage = findViewById(R.id.select_product_image)
        InputProductName = findViewById(R.id.product_name) as EditText
        InputProductDescription = findViewById(R.id.product_description)
        InputProductPrice = findViewById(R.id.product_price)
        loadingBar = ProgressDialog(this)
        InputProductImage.setOnClickListener {

                OpenGallery()

        }
        AddNewProductButton.setOnClickListener {

                ValidateProductData()

        }
    }
    private fun OpenGallery() {
        val galleryIntent = Intent()
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/*")
        startActivityForResult(galleryIntent, GalleryPick)
    }
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {
            ImageUri = data.getData()!!
            InputProductImage.setImageURI(ImageUri)
        }
    }
    private fun ValidateProductData() {
        Description = InputProductDescription.getText().toString()
        Price = InputProductPrice.getText().toString()
        Pname = InputProductName.getText().toString()
        if (ImageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        }
        else
        {
            StoreProductInformation()
        }
    }
    private fun StoreProductInformation() {
        loadingBar.setTitle("Add New Product")
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(calendar.getTime())
        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calendar.getTime())
        productRandomKey = saveCurrentDate + saveCurrentTime
        val filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg")
        val uploadTask = filePath.putFile(ImageUri)
        uploadTask.addOnFailureListener(object:OnFailureListener {
            override fun onFailure(@NonNull e:Exception) {
                val message = e.toString()
                Toast.makeText(this@AdminAddNewProductActivity, "Error: " + message, Toast.LENGTH_SHORT).show()
                loadingBar.dismiss()
            }
        }).addOnSuccessListener(object:OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(taskSnapshot:UploadTask.TaskSnapshot) {
                Toast.makeText(this@AdminAddNewProductActivity, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show()
                val urlTask = uploadTask.continueWithTask(object:Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                    @Throws(Exception::class)
                    override fun then(@NonNull task:Task<UploadTask.TaskSnapshot>):Task<Uri> {
                        if (!task.isSuccessful())
                        {
                            throw task.getException()!!
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString()
                        return filePath.getDownloadUrl()
                    }
                }).addOnCompleteListener(object:OnCompleteListener<Uri> {
                    override fun onComplete(@NonNull task:Task<Uri>) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString()
                            Toast.makeText(this@AdminAddNewProductActivity, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show()
                            SaveProductInfoToDatabase()
                        }
                    }
                })
            }
        })
    }
    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any>()
       // var user:Map<String, Any> = HashMap()
        productMap.put("pid", productRandomKey)
        productMap.put("date", saveCurrentDate)
        productMap.put("time", saveCurrentTime)
        productMap.put("description", Description)
        productMap.put("image", downloadImageUrl)
        productMap.put("category", CategoryName)
        productMap.put("price", Price)
        productMap.put("pname", Pname)
        ProductsRef.child(productRandomKey).updateChildren(productMap)
            .addOnCompleteListener(object:OnCompleteListener<Void> {
                override fun onComplete(@NonNull task:Task<Void>) {
                    if (task.isSuccessful())
                    {
                        val intent = Intent(this@AdminAddNewProductActivity, AdminCategoryActivity::class.java)
                        startActivity(intent)
                        loadingBar.dismiss()
                        Toast.makeText(this@AdminAddNewProductActivity, "Product is added successfully..", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        loadingBar.dismiss()
                        val message = task.getException().toString()
                        Toast.makeText(this@AdminAddNewProductActivity, "Error: " + message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
    companion object {
        private val GalleryPick = 1
    }
}