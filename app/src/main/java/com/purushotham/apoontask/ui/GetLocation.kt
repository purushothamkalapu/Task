package com.purushotham.apoontask.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.purushotham.apoontask.R
import com.purushotham.apoontask.databinding.ActivityGetLocationBinding
import java.util.*


class GetLocation : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var currentLatLong: LatLng
    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityGetLocationBinding
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btSelect.setOnClickListener {
            val intent = Intent()
            intent.putExtra("lat", currentLatLong.latitude.toString())
            intent.putExtra("lng", currentLatLong.longitude.toString())
            intent.putExtra("address", binding.tvAddress.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                currentLatLong = LatLng(location.latitude, location.longitude)
                binding.tvAddress.text = getAdress(currentLatLong)
                //placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 18f))
                mMap.setOnCameraIdleListener {

                    currentLatLong = mMap.cameraPosition.target
                    binding.tvAddress.text = getAdress(currentLatLong)
                }
            }
        }


    }


    /*private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong).title("Buradayım")
            .snippet(getAdress(currentLatLong))
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }*/

    private fun getAdress(currentLatLong: LatLng): String? {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val adress = geoCoder.getFromLocation(currentLatLong.latitude, currentLatLong.longitude, 1)
        return adress?.get(0)?.getAddressLine(0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                setUpMap()
            } else {
                requestPermission()
                // Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), LOCATION_REQUEST_CODE
        )
    }


    override fun onMarkerClick(p0: Marker): Boolean = false
}

