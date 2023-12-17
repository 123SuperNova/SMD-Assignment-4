package com.example.assignment4.sampledata
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

/** The geometry of a Geocoding result.  */
class Geometry : Serializable {
    /**
     * The bounding box which can fully contain the returned result (optionally returned). Note that
     * these bounds may not match the recommended viewport. (For example, San Francisco includes the
     * Farallon islands, which are technically part of the city, but probably should not be returned
     * in the viewport.)
     */
    var bounds: Bounds? = null

    /**
     * The geocoded latitude/longitude value. For normal address lookups, this field is typically the
     * most important.
     */
    var location: LatLng? = null

    /** The level of certainty of this geocoding result.  */
    var locationType: LocationType? = null

    /**
     * The recommended viewport for displaying the returned result. Generally the viewport is used to
     * frame a result when displaying it to a user.
     */
    var viewport: Bounds? = null

    override fun toString(): String {
        return String.format(
            "[Geometry: %s (%s) bounds=%s, viewport=%s]", location, locationType, bounds, viewport)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}