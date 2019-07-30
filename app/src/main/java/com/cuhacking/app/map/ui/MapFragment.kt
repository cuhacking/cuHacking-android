/*
 *    Copyright 2019 cuHacking
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cuhacking.app.map.ui

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cuhacking.app.BuildConfig

import com.cuhacking.app.R
import com.cuhacking.app.data.map.Floor
import com.cuhacking.app.di.injector
import com.google.android.material.button.MaterialButtonToggleGroup
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }

    private val viewModel: MapViewModel by viewModels { injector.mapViewModelFactory() }
    private var map: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireContext(), BuildConfig.MAPBOX_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = view.findViewById<MapView>(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { mapboxMap ->
            map = mapboxMap
            val mapStyle = when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> Style.DARK
                else -> Style.LIGHT
            }
            mapboxMap.setStyle(mapStyle) { style ->
                viewModel.floorSource.observe(this, Observer { source ->
                    applyMapStyle(style, source)
                })
            }

            mapboxMap.cameraPosition = CameraPosition.Builder()
                .target(LatLng(45.3823547, -75.6974599))
                .zoom(17.0)
                .build()

            viewModel.selectedFloor.observe(this, Observer { floor ->
                val fillLayer = mapboxMap.style?.getLayer("rb") as FillLayer
                fillLayer.setFilter(eq(get("floor"), floor.number))

                val lineLayer = mapboxMap.style?.getLayer("rb-lines") as LineLayer
                lineLayer.setFilter(eq(get("floor"), floor.number))
            })
        }

        view.findViewById<MaterialButtonToggleGroup>(R.id.button_toggle_group)
            .addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (!isChecked) return@addOnButtonCheckedListener

                when (checkedId) {
                    R.id.first -> viewModel.setFloor(Floor.LV01)
                    R.id.second -> viewModel.setFloor(Floor.LV02)
                    R.id.third -> viewModel.setFloor(Floor.LV03)
                }
            }
    }

    private fun applyMapStyle(style: Style, source: GeoJsonSource) {
        try {
            style.addSource(source)
        } catch (_: Exception) {
        }

        val layer = FillLayer("rb", source.id)
        layer.setProperties(
            fillColor(
                match(
                    get("type"), color(Color.parseColor("#212121")),
                    stop("room", color(Color.parseColor("#00FF00"))),
                    stop("washroom", color(Color.parseColor("#FFFF00"))),
                    stop("elevator", color(Color.parseColor("#FF0000"))),
                    stop("stairs", color(Color.parseColor("#0000FF"))),
                    stop("hallway", color(Color.parseColor("#FFFFFF")))
                )
            )
        )
        layer.setFilter(eq(get("floor"), 1))

        val lineLayer = LineLayer("rb-lines", source.id)
        lineLayer.setProperties(
            lineWidth(2f),
            lineColor("#7C39BF")
        )
        lineLayer.setFilter(eq(get("floor"), 1))

        style.addLayer(layer)
        style.addLayer(lineLayer)
    }

    override fun onStart() {
        super.onStart()
        view?.findViewById<MapView>(R.id.map_view)?.onStart()
    }

    override fun onPause() {
        super.onPause()
        view?.findViewById<MapView>(R.id.map_view)?.onPause()
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<MapView>(R.id.map_view)?.onResume()
    }

    override fun onStop() {
        super.onStop()
        view?.findViewById<MapView>(R.id.map_view)?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.findViewById<MapView>(R.id.map_view)?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        view?.findViewById<MapView>(R.id.map_view)?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.findViewById<MapView>(R.id.map_view)?.onSaveInstanceState(outState)
    }
}
