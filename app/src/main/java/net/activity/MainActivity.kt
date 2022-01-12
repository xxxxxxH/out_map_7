package net.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import net.event.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.x
import utils.DialogUtils
import utils.MapUtils

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener {
    var map: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        x.Ext.init(application)
        x.Ext.setDebug(BuildConfig.DEBUG)
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
                    it.setOnMyLocationButtonClickListener(this)
                }
            }
            "nearby" -> {
                startActivity(Intent(this,NearbyActivity::class.java))
            }
            "streetview" -> {
                startActivity(Intent(this,StreetviewActivity::class.java))
            }
            "inter" -> {
                startActivity(Intent(this,InteractiveActivity::class.java))
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
                val builder = PlacePicker.IntentBuilder()
                startActivityForResult(
                    builder.build(this),
                    1
                )
            }
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        map?.let {
            locationBtnClick(it)
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val place = PlacePicker.getPlace(data, this)
                        val position = CameraPosition.Builder().target(place.latLng)
                            .zoom(15f)
                            .bearing(0f)
                            .tilt(25f)
                            .build()
                        map?.animateCamera(
                            CameraUpdateFactory.newCameraPosition(position),
                            1000,
                            null
                        )
                    }
                }
            }
        }
    }
}