package com.joopda.qrscanexample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.joopda.qrscanexample.components.CustomBalloonAdapter
import com.joopda.qrscanexample.components.MarkerEventListener
import com.joopda.qrscanexample.data.MarkerItem
import com.joopda.qrscanexample.databinding.ActivityMapBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.lang.NullPointerException
import kotlin.system.exitProcess

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private var mapView: MapView? = null
    private var marker: MapPOIItem? = null
    private val eventListener = MarkerEventListener(this)
    var markerList = arrayListOf<MarkerItem>()

        /*TODO
        *  - DTO
        *  - ITEM(Model)
        *  - */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = MapView(this)
        marker = MapPOIItem()
        binding.mapContainer.addView(mapView)
        if(mapView != null) {
            addList()
            getCurrentPosition()
        }else{
            Log.e("mapView","NULL")
        }

            binding.mapBtn.setOnClickListener {
                addList()
                Toast.makeText(this, "불러오기", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addList(){
        markerList.clear()

        markerList.add(//37.52492954927366, 126.99751942499772
            MarkerItem(
                name = "희재집",
                lat = 37.52492954927366,
                lng = 126.99751942499772,
                tag = 1
            )
        )

        markerList.add(//37.49692793419638, 127.04055614034083
            MarkerItem(
                name = "줍다",
                lat = 37.49692793419638,
                lng = 127.04055614034083,
                tag = 2
            )
        )

        markerList.add(//37.49672360295564, 127.04050249605169
            MarkerItem(
                name = "cafe shins",
                lat = 37.49672360295564,
                lng = 127.04050249605169,
                tag = 3
            )
        )

        markerList.add(//37.595855491258604, 126.9163617556868
            MarkerItem(
                name = "본가",
                lat = 37.595855491258604,
                lng = 126.9163617556868,
                tag = 4
            )
        )
    }

    private fun getCurrentPosition(){
        val pCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if(pCheck == PackageManager.PERMISSION_GRANTED){
            val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                val userCurrentLocation: Location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                val latitude = userCurrentLocation.latitude //x 위도
                val longitude = userCurrentLocation.longitude //x 경도
                val currentPosition = MapPoint.mapPointWithGeoCoord(latitude, longitude) //x 위도 경도 사용하여 표시
                mapView?.setMapCenterPoint(currentPosition, true) //x 현재 위치 기점으로 이동
//37.49692793419638, 127.04055614034083
                //현재 위치 마킹
                markerList.forEach {
                    marker?.apply {
                        itemName = it.name?: ""
                        tag = it.tag?: 0
                        mapPoint = MapPoint.mapPointWithGeoCoord(it.lat!!, it.lng!!)//currentPosition
                        markerType = MapPOIItem.MarkerType.CustomImage
                        customImageResourceId = R.drawable.marker_red
                        selectedMarkerType = MapPOIItem.MarkerType.CustomImage
                        customSelectedImageResourceId = R.drawable.marker_red
                        isCustomImageAutoscale = true
                        setCustomImageAnchor(0.5f, 1f)
                    }
                    mapView?.addPOIItem(marker)
                }
                    mapView?.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater)) //커스텀 말풍선 등록
                    mapView?.setPOIItemEventListener(eventListener) //마커 클릭 이벤트 리스너 적용


            }catch (e: NullPointerException){
                Log.e("LOCATION_ERROR", e.toString())
                ActivityCompat.finishAffinity(this)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                exitProcess(0)
            }
        }else{
            Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
        }
    }


    companion object{
        const val PERMISSIONS_REQUEST_CODE = 100
        var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}