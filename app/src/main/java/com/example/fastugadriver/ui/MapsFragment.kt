package com.example.fastugadriver.ui

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fastugadriver.R
import com.example.fastugadriver.databinding.FragmentMapsBinding
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private lateinit var originLocation: Location
    private lateinit var client: MapboxDirections
    private lateinit var currentRoute: DirectionsRoute
    private lateinit var origin: Point
    private lateinit var destination: Point
    private val ROUTE_LAYER_ID = "route-layer-id"
    private val ROUTE_SOURCE_ID = "route-source-id"
    private val ICON_LAYER_ID = "icon-layer-id"
    private val ICON_SOURCE_ID = "icon-source-id"
    private val RED_PIN_ICON_ID = "red-pin-icon-id"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        context?.let { Mapbox.getInstance(it, getString(R.string.mapbox_access_token)) }

        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.map) as MapView
        // mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS
            ) { style -> // Set the origin location to the Alhambra landmark in Granada, Spain.
                origin = Point.fromLngLat(-3.588098, 37.176164)

                // Set the destination location to the Plaza del Triunfo in Granada, Spain.
                destination = Point.fromLngLat(-3.601845, 37.184080)
                initSource(style)
                initLayers(style)

                // Get the directions route from the Mapbox Directions API
                getRoute(mapboxMap, origin, destination)
            }
        }

        // onMapReady()
    }


/*
    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(9.0)
                .build()
        )

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
            initLocationComponent()
            setupGesturesListener()
            addAnnotationToMap()
        }
            // "https://api.mapbox.com/directions/v5/mapbox/driving/-8.808%2C39.7445%3B-8.8246%2C39.6581?alternatives=true&geometries=geojson&language=en&overview=simplified&steps=true&access_token=pk.eyJ1IjoiYnJ1bm9zaWx2YTAwIiwiYSI6ImNsYmFvN3F0aDBhcm8zdXBkaXp1eDBtZGEifQ.0djWh5zBBWNcDN9EoNc2sw"

    }
*/
    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     * @param mapboxMap the Mapbox map object that the route will be drawn on
     * @param origin      the starting point of the route
     * @param destination the desired finish point of the route
     */
    private fun getRoute(mapboxMap: MapboxMap?, origin: Point, destination: Point) {
        client = MapboxDirections.builder()
            .origin(origin)
            .destination(destination)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .accessToken(getString(R.string.mapbox_access_token))
            .build()
        client.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>,
            ) {
// You can get the generic HTTP info about the response
                println("Response code: " + response.code())
                if (response.body() == null) {
                    println("No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    println("No routes found")
                    return
                }

// Get the directions route
                currentRoute = response.body()!!.routes()[0]

// Make a toast which displays the route's distance
                println( "Distancia: " + currentRoute.distance())

                mapboxMap?.getStyle { style -> // Retrieve and update the source designated for showing the directions route
                    val source: GeoJsonSource? = style.getSourceAs(ROUTE_SOURCE_ID)

                    // Create a LineString with the directions route's geometry and
                    // reset the GeoJSON source for the route LineLayer source
                    source?.setGeoJson(currentRoute.geometry()?.let {
                        LineString.fromPolyline(it,
                            PRECISION_6)
                    })
                }
            }

            override fun onFailure(call: Call<DirectionsResponse?>, throwable: Throwable) {
                println("Error: " + throwable.message)

            }
        })
    }

    /**
     * Add the route and marker sources to the map
     */
    private fun initSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID))
        val iconGeoJsonSource =
            GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(arrayOf<Feature>(
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(),
                    destination.latitude())))))
        loadedMapStyle.addSource(iconGeoJsonSource)
    }

    /**
     * Add the route and marker icon layers to the map
     */
    private fun initLayers(loadedMapStyle: Style) {
        val routeLayer = LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID)

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
            lineCap(Property.LINE_CAP_ROUND),
            lineJoin(Property.LINE_JOIN_ROUND),
            lineWidth(5f),
            lineColor(Color.parseColor("#009688"))
        )
        loadedMapStyle.addLayer(routeLayer)

// Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
            resources.getDrawable(R.drawable.mapbox_marker_icon_red))!!)

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
            iconImage(RED_PIN_ICON_ID),
            iconIgnorePlacement(true),
            iconAllowOverlap(true),
            iconOffset(arrayOf(0f, -9f))))
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the Directions API request
        client.cancelCall()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


/*

    //region loading map
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }


    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    requireActivity(),
                    com.mapbox.maps.R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    requireActivity(),
                    com.mapbox.maps.R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(30.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(requireActivity(), "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }
    //endregion

    //region Add marker

    private fun addAnnotationToMap() {
        // Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes(
            requireActivity(),
            R.drawable.mapbox_marker_icon_red
        )?.let {
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            // Set options for the resulting symbol layer.
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                // Define a geographic coordinate.
                .withPoint(Point.fromLngLat(-8.80725, 39.7443))
                // Specify the bitmap you assigned to the point annotation
                // The bitmap will be added to map style automatically.
                .withIconImage(it)
            // Add the resulting pointAnnotation to the map.
            pointAnnotationManager?.create(pointAnnotationOptions)
        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
    //endregion

    //region override's on...
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }
    //endregion

 */
}