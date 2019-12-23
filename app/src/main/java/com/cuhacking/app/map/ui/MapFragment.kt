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
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cuhacking.app.BuildConfig
import com.cuhacking.app.R
import com.cuhacking.app.data.map.Floor
import com.cuhacking.app.di.injector
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class MapFragment : Fragment(R.layout.map_fragment) {

    companion object {
        fun newInstance() = MapFragment()
    }

    private val viewModel: MapViewModel by viewModels { injector.mapViewModelFactory() }
    private var map: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireContext(), BuildConfig.MAPBOX_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = view.findViewById<MapView>(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { mapboxMap ->
            map = mapboxMap
            val mapStyle =
                when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> Style.DARK
                    else -> Style.LIGHT
                }
            mapboxMap.setStyle(mapStyle) { style ->
                viewModel.floorSource.observe(this, Observer { source ->
                    applyMapStyle(style, source)
                })
            }

            mapboxMap.addOnMapClickListener(::handleMapClick)

            if (savedInstanceState == null) {
                map?.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(45.382344, -75.696256))
                    .zoom(18.0)
                    .bearing(-125.0)
                    .build()

                mapboxMap.setLatLngBoundsForCameraTarget(
                    LatLngBounds.Builder()
                        .include(LatLng(45.39242286156869, -75.70335388183594))
                        .include(LatLng(45.379824116060036, -75.68588733673096))
                        .build()
                )
                mapboxMap.setMinZoomPreference(15.0)

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

        val bottomCard = view.findViewById<MaterialCardView>(R.id.bottom_card)
        val bottomSheet = BottomSheetBehavior.from(bottomCard)
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.selectedRoom.observe(this, Observer { roomId ->
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            view.findViewById<TextView>(R.id.room_name).text = roomId
        })
    }

    private fun applyMapStyle(style: Style, source: GeoJsonSource) {
        style.addSource(source)

        FillLayer("rb", source.id).apply {
            setProperties(
                fillColor(
                    match(
                        get("type"), color(Color.parseColor("#AAAAAA")),
                        stop("room", color(Color.parseColor("#9756D8"))),
                        stop("washroom", color(Color.parseColor("#F6D500"))),
                        stop("elevator", color(Color.parseColor("#DD001E"))),
                        stop("stairs", color(Color.parseColor("#006CA9"))),
                        stop("hallway", color(Color.parseColor("#FEFEFE")))
                    )
                ),
                fillOpacity(
                    match(
                        get("type"), literal(1f),
                        stop("open", 0f)
                    )
                )
            )
            style.addLayer(this)
        }

        FillLayer("rb-backdrop", source.id).apply {
            setProperties(
                fillColor(Color.parseColor("#888888"))
            )
            style.addLayerBelow(this, "rb")
        }

        LineLayer("rb-lines", source.id).apply {
            setProperties(
                lineWidth(3f),
                lineColor("#212121")
            )
            style.addLayer(this)
        }

        LineLayer("rb-backdrop-lines", source.id).apply {
            setProperties(
                lineWidth(5f),
                lineColor("#212121")
            )
            style.addLayer(this)
        }

        SymbolLayer("rb-symbols", source.id).apply {
            setProperties(textField(get("name")))
            style.addLayer(this)
        }

        setLayerFilters(style, Floor.LV01)
        viewModel.selectedFloor.observe(this, Observer { floor ->
            val id = when (floor) {
                Floor.LV01 -> R.id.first
                Floor.LV02 -> R.id.second
                Floor.LV03 -> R.id.third
                else -> R.id.first
            }

            view?.findViewById<FloorSelectionButton>(id)?.let {
                if (!it.isChecked) {
                    it.isChecked = true
                }
            }

            setLayerFilters(style, floor)
        })
    }

    private fun setLayerFilters(style: Style, floor: Floor) {
        // Room fills
        (style.getLayer("rb") as FillLayer)
            .setFilter(
                all(
                    eq(get("floor"), floor.number),
                    any(
                        eq(get("type"), "elevator"),
                        eq(get("type"), "room"),
                        eq(get("type"), "washroom"),
                        eq(get("type"), "stairs"),
                        eq(get("type"), "hallway")
                    )
                )
            )

        // Backdrop layer
        (style.getLayer("rb-backdrop") as FillLayer)
            .setFilter(all(eq(get("floor"), floor.number), eq(get("type"), "backdrop")))

        // Room outlines
        (style.getLayer("rb-lines") as LineLayer)
            .setFilter(all(eq(get("floor"), floor.number), eq(get("type"), "line")))

        // Backdrop lines
        (style.getLayer("rb-backdrop-lines") as LineLayer)
            .setFilter(all(eq(get("floor"), floor.number), eq(get("type"), "backdrop-line")))

        // Labels/Icons
        (style.getLayer("rb-symbols") as SymbolLayer)
            .setFilter(all(eq(get("floor"), floor.number), eq(get("label"), true)))
    }

    private fun handleMapClick(latLng: LatLng): Boolean {
        map?.let { mapboxMap ->
            val pointF = mapboxMap.projection.toScreenLocation(latLng)
            val rectF = RectF(pointF.x - 10, pointF.y - 10, pointF.x + 10, pointF.y - 10)
            val features = mapboxMap.queryRenderedFeatures(rectF, "rb")

            if (features.size > 0) {
                if (features[0].hasNonNullValueForProperty("room")) {
                    viewModel.selectRoom(features[0].getStringProperty("room"))
                }

                return true
            }
        }

        return false
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

    override fun onDestroyView() {
        super.onDestroyView()
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
