package com.example.fastugadriver.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.*
import com.example.fastugadriver.R
import com.example.fastugadriver.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("UNREACHABLE_CODE")
class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.isMyLocationEnabled = true

        val staticOrder = LatLng(39.73462959799744, -8.82104894637871)
        googleMap.addMarker(MarkerOptions().position(staticOrder).title("Marker Order"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(staticOrder))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo( 11.0f ))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}