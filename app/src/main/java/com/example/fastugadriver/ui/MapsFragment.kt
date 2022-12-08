package com.example.fastugadriver.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.FormErrorResponse
import com.example.fastugadriver.data.pojos.SuccessResponse
import com.example.fastugadriver.databinding.FragmentMapsBinding
import com.example.fastugadriver.gateway.MapBoxGateway
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
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
import java.util.*



class MapsFragment : Fragment(){

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
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
        context?.let {  Mapbox.getInstance(it, getString(R.string.mapbox_access_token)) }

        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mapView = view.findViewById(R.id.map) as MapView
        mapView.onCreate(savedInstanceState)

        val mapBoxGateway = MapBoxGateway()

        mapBoxGateway.responseDirections.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer

            currentRoute = response.body()!!.routes()[0]
            setCameraPosition()
            setMapStyle()

            val mapBoxGateway = MapBoxGateway()

            mapBoxGateway.responseDirections.observe(viewLifecycleOwner, Observer {
                val response = it ?: return@Observer

                currentRoute = response.body()!!.routes()[0]
                setCameraPosition()
                setMapStyle()

            })


        })

        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap

            mapboxMap.setStyle(Style.MAPBOX_STREETS
            ) { style -> // Set the origin location to the restaurant (ESTG)

                showStaticPath(style)
            }
        }

    }



    // region Show Path
    private fun showStaticPath(style: Style) {
        origin = Point.fromLngLat(-8.820920, 39.734866)

        // Set the destination location to the client
        destination = Point.fromLngLat(-8.824283, 39.658306 )

        initSource(style)
        initLayers(style)


        val mapBoxGateway = MapBoxGateway()

        mapBoxGateway.responseDirections.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer

            currentRoute = response.body()!!.routes()[0]
            setCameraPosition()
            setMapStyle()

        })

        mapBoxGateway.getRoute(origin, destination, getString(R.string.mapbox_access_token))

    }

    private fun setMapStyle(){
        mapboxMap.getStyle { style ->
            // Retrieve and update the source designated for showing the directions route
            val source: GeoJsonSource? = style.getSourceAs(ROUTE_SOURCE_ID)

            // Create a LineString with the directions route's geometry and
            // reset the GeoJSON source for the route LineLayer source
            source?.setGeoJson(currentRoute.geometry()?.let {
                LineString.fromPolyline(it,
                    PRECISION_6)
            })
        }
    }

    private fun setCameraPosition(){
        val position = CameraPosition.Builder()
            .target(LatLng(destination.latitude(), destination.longitude()))
            .zoom(10.5)
            .build()

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1)

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

    // endregion
    
}