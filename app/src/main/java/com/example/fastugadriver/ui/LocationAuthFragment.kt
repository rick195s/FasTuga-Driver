package com.example.fastugadriver.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.example.fastugadriver.R


class LocationAuthFragment : Fragment() {

    private val LOCATION_PERMISSION: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location_auth, container, false)

        val button: Button = view.findViewById<View>(R.id.btnAllow) as Button
        button.setOnClickListener {
            requestLocationPermission()
        }
        return view
    }

    fun requestLocationPermission () {
        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            AlertDialog.Builder(requireActivity())
                .setTitle("Permission needed")
                .setMessage("This permission is needed to enter the aplication")
                .setPositiveButton(R.string.accept_location_auth) { dialog, which ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)

                }
                .setNegativeButton(R.string.no_location_auth) { dialog, which ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create().show();
        }else{
            requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION);
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode == LOCATION_PERMISSION){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                recreate(requireActivity())
                Toast.makeText(requireActivity(), "Permission GRANTED. Redirecting...", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

}