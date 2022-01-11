package net.basicmodel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener {
    var map: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        mapview.onCreate(savedInstanceState)
        mapview.onResume()
        MapsInitializer.initialize(this)
        MapUtils.mapAsync(mapview)
        fb.setOnClickListener {
            DialogUtils.createDlg(this)
        }
    }


    private fun initMapSetting(map: GoogleMap) {
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isZoomControlsEnabled = true
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun locationBtnClick(map: GoogleMap) {
        val location = MapUtils.getLocation(this)
        val lat = location.latitude
        val lgt = location.longitude
        if (lat != 0.0 && lgt != 0.0) {
            map.let {
                it.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        MapUtils.getCameraPosition(
                            lat,
                            lgt
                        )
                    ), 1000, null
                )
                it.addMarker(
                    MarkerOptions()
                        .position(LatLng(lat, lgt)).title("On Your Location")
                )
            }
        }
    }

    private fun setMapType(type: String) {
        when (type) {
            "normal" -> {
                map?.let {
                    it.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
            }
            "hybird" -> {
                map?.let {
                    it.mapType = GoogleMap.MAP_TYPE_HYBRID
                }
            }
            "sat" -> {
                map?.let {
                    it.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
            }
            "terrain" -> {
                map?.let {
                    it.mapType = GoogleMap.MAP_TYPE_TERRAIN
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: MessageEvent) {
        val msg = e.getMessage()
        when (msg[0]) {
            "map" -> {
                map = msg[1] as GoogleMap
                map?.let {
                    initMapSetting(it)
                    locationBtnClick(it)
                }
            }
            "nearby" -> {

            }
            "streetview" -> {

            }
            "inter" -> {

            }
            "normal" -> {
                setMapType("normal")
            }
            "hybird" -> {
                setMapType("hybird")
            }
            "sat" -> {
                setMapType("sat")
            }
            "terrain" -> {
                setMapType("terrain")
            }
            "search" -> {

            }
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        map?.let {
            locationBtnClick(it)
        }
        return false
    }
}