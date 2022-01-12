package net.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.sdsmdg.tastytoast.TastyToast
import utils.MapUtils

class StreetviewActivity : AppCompatActivity(), OnStreetViewPanoramaReadyCallback {
    var lat: Double = 0.0
    var lgt: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streetview)
        val streetViewPanoramaFragment =
            supportFragmentManager
                .findFragmentById(R.id.streetviewpanorama) as SupportStreetViewPanoramaFragment?
        streetViewPanoramaFragment?.getStreetViewPanoramaAsync(this)
        val location = MapUtils.getLocation(this)
        lat = location.latitude
        lgt = location.longitude
        if (lat == 0.0 || lgt == 0.0) {
            lat = 37.754130
            lgt = -122.447129
            TastyToast.makeText(
                this,
                "Get Location Failed",
                TastyToast.LENGTH_SHORT,
                TastyToast.ERROR
            )
        }
        if (streetViewPanoramaFragment == null) {
            TastyToast.makeText(
                this,
                "Streetview can't show",
                TastyToast.LENGTH_SHORT,
                TastyToast.ERROR
            )
        }
    }

    override fun onStreetViewPanoramaReady(p0: StreetViewPanorama?) {
        p0?.setPosition(LatLng(lat, lgt))
    }
}