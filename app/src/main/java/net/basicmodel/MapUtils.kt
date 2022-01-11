package net.basicmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import org.greenrobot.eventbus.EventBus
@SuppressLint("MissingPermission")
object MapUtils {
    fun mapAsync(mapView: MapView){
        mapView.getMapAsync {
            EventBus.getDefault().post(MessageEvent("map",it))
        }
    }


    fun getLocation(context: Context): Location {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) as Location
    }

    fun getCameraPosition(lat:Double,lgt:Double): CameraPosition {
        return CameraPosition.builder().target(LatLng(lat, lgt))
            .zoom(15.5f)
            .bearing(0f)
            .tilt(25f)
            .build()
    }
}