package com.example.android.homepage.ui.location

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android.homepage.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_location.*
import java.util.*
import kotlin.collections.HashMap


class AddLocation : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private var filePath: Uri? = null
    private var uploadFileName: String? = null

    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    //access database table
    private var locationDatabase: DatabaseReference? = null
    //to get the current database pointer
    private var locationReference: DatabaseReference? = null
    private var locationListener: ChildEventListener? = null

    private var longitude: Double? = null
    private var latitude: Double? = null

    private var addressList:List<Address>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        //get location latitude and longitude

        //initialize fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()

        imageButtonLocation.setOnClickListener {
            //check runtime permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //always request for permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }else{
                    //permission granted
                    pickImageFromGallery()
                }
            }else{
                pickImageFromGallery()
            }

            //INITIALISE FIREBASESTORAGE
            storage = FirebaseStorage.getInstance()
            storageReference = storage!!.reference

            //INITIALISE FIREBASEDATABASE
            locationDatabase = FirebaseDatabase.getInstance().reference
            //to access to the table
            locationReference = FirebaseDatabase.getInstance().getReference("location")
            buttonSave.setOnClickListener {
                //uploadFile()//FirebaseNoSignedInUserException
                addNewLocation()
            }
        }


        //upload image to firebase and get the url
        //input the image thing all as one class
        //then do the mapping as what input the db at the main activity
    }

    private fun checkPermission() {
        val permission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermission()
        }
        if(permission == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        }
    }

    private fun requestLocationUpdates(){
        locationRequest = LocationRequest()

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            LocationCallback()
            , Looper.getMainLooper())
    }

    private fun getLocation() {
        //check granted permission, if granted straight get the input from the user :D
        requestLocationUpdates()

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                longitude = it.longitude
                latitude = it.latitude

                addressList = Geocoder(this).getFromLocation(latitude!!, longitude!!,1)

                val address = (addressList as MutableList<Address>?)!![0]
                editTextAddress1.setText(address.featureName+ ", " +  address.thoroughfare + ", ")
                editTextAddress2.setText(address.subLocality + ", ")
                editTextAddress3.setText(address.postalCode + " " + address.locality + ", " + address.adminArea)

                Toast.makeText(applicationContext, "The location retrieved successfully", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getFullAddress(): String{
        val addressString = editTextAddress1.text.toString() + editTextAddress2.text.toString() + editTextAddress3.text.toString()

        return addressString
    }
    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        }
    }

    private fun addNewLocation(){
        //val locUrl:Uri? = storageReference.child("locImages/$uploadFileName").getDownloadUrl().getResult()

        val locationName:String = editTextLocationName.text.toString()

        val locationAddress = getFullAddress()

        val locationImage = "https://ocvoice.files.wordpress.com/2008/08/hpim2553.jpg"

        val locationOperation: String = spinnerDayStart.selectedItem.toString() + " to " + spinnerDayEnd.selectedItem.toString() + " " + editTextOperationTime.text

        val location = Location(locationName
            , locationAddress
            , locationImage
            ,locationOperation)

        val locationValues = location.toMap()
        val childUpdates = HashMap<String, Any>()

        //ADD NEW ENTITY
        val key = locationDatabase!!.child("location").push().key

        //ADD THE INTO THE NEWLY CREATED ENTITY
        childUpdates.put("/location/$key", locationValues)

        locationDatabase!!.updateChildren(childUpdates)
        Toast.makeText(this, "Successfully added new location", Toast.LENGTH_SHORT).show()

    }
    private fun uploadFile(){
        if(filePath != null){
            Toast.makeText(this,"Uploading", Toast.LENGTH_SHORT).show()
        }
        uploadFileName = UUID.randomUUID().toString()
        val imageRef = storageReference!!.child("locImages/" + uploadFileName)
        imageRef.putFile(filePath!!)
            .addOnSuccessListener {
                Toast.makeText(this,"File success to uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"File failed to upload", Toast.LENGTH_SHORT).show()
            }
    }

    private fun pickImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    companion object {
        //IMAGE PICK CODE
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
        private const val REQUEST_LOCATION = 1
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }else{
                    //permission from popup denied
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //handle image pick result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Picasso.get().load(data?.data).into(imageButtonLocation)
            filePath = data?.data
        }
    }
}