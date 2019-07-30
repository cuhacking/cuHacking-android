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

            if (savedInstanceState == null) {
                map?.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(45.382344, -75.696256))
                    .zoom(17.0)
                    .build()

                mapboxMap.uiSettings.isCompassEnabled = false
            }
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
        style.addSource(source)

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

        viewModel.selectedFloor.observe(this, Observer { floor ->
            val floorFillLayer = style.getLayer("rb") as FillLayer
            floorFillLayer.setFilter(eq(get("floor"), floor.number))

            val floorLineLayer = style.getLayer("rb-lines") as LineLayer
            floorLineLayer.setFilter(eq(get("floor"), floor.number))
        })
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

        // Remove all the layers we added
        map?.style?.layers?.let { layers ->
            layers.forEach { layer ->
                map?.style?.removeLayer(layer)
            }
        }

        // Remove data source so that it can be added again if the fragment is recreated
        viewModel.floorSource.value?.let { source ->
            map?.style?.removeSource(source)
        }
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
