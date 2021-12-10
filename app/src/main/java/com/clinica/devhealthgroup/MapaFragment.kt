package com.clinica.devhealthgroup

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MapaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MapaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapaFragment : Fragment(), OnMapReadyCallback {
    private var map: GoogleMap? = null

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.map = googleMap

        // verificar se localização está autorizada
        val ok = PermissionUtils.validate(requireActivity(), 1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        // colocar o botão de localização
        if (ok) map?.isMyLocationEnabled = true


        // criar um objeto de latitude e longitude
        val location =LatLng(-23.66284834894077, -46.52676858775841)

        // posicionar o mapa na coordenada criada, com um valor de zoom
        val update = CameraUpdateFactory.newLatLngZoom(location, 18f)
        map?.moveCamera(update)

        // colocar um marcador no local selecionado
        map?.addMarker(MarkerOptions()
                .title("Clinica Psicologia")
                .snippet("Psicologa Carolina Ramos Bertolino")
                .position(location)
        )
        // tipo do mapa
        map?.mapType=GoogleMap.MAP_TYPE_NORMAL
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_mapa, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    // habilitar botão de localização após permissão do usuário
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResult: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult)
        for (result in grantResult) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                map?.isMyLocationEnabled = true
                return
            }
        }
    }

}
