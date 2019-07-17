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
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cuhacking.app.BuildConfig

import com.cuhacking.app.R
import com.cuhacking.app.data.map.Floor
import com.cuhacking.app.di.injector
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*

class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }

    private val viewModel: MapViewModel by viewModels { injector.mapViewModelFactory() }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = view.findViewById<MapView>(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { map ->
            val style = when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> Style.DARK
                else -> Style.LIGHT
            }
            map.setStyle(style)

            map.cameraPosition = CameraPosition.Builder()
                .target(LatLng(45.3823547, -75.6974599))
                .zoom(17.0)
                .build()

            viewModel.floorSource.observe(this, Observer { source ->
                map.style?.let { style ->
                    style.addSource(source)

                    val layer = FillLayer("rb", source.id)
                    layer.setProperties(
                        fillColor("#212121")
                    )
                    layer.setFilter(Expression.eq(Expression.get("floor"), 1))

                    val lineLayer = LineLayer("rb-lines", source.id)
                    lineLayer.setProperties(
                        lineWidth(2f),
                        lineColor("#7C39BF")
                    )
                    lineLayer.setFilter(Expression.eq(Expression.get("floor"), 1))

                    style.addLayer(layer)
                    style.addLayer(lineLayer)
                }
            })

            viewModel.selectedFloor.observe(this, Observer { floor ->
                val fillLayer = map.style?.getLayer("rb") as FillLayer
                fillLayer.setFilter(Expression.eq(Expression.get("floor"), floor.number))

                val lineLayer = map.style?.getLayer("rb-lines") as LineLayer
                lineLayer.setFilter(Expression.eq(Expression.get("floor"), floor.number))
            })
        }

        view.findViewById<TextView>(R.id.first).setOnClickListener {
            viewModel.setFloor(Floor.LV01)
        }
        view.findViewById<TextView>(R.id.second).setOnClickListener {
            viewModel.setFloor(Floor.LV02)
        }
        view.findViewById<TextView>(R.id.third).setOnClickListener {
            viewModel.setFloor(Floor.LV03)
        }
    }

    override fun onStart() {
        super.onStart()
        view?.findViewById<MapView>(R.id.map_view)?.onStart()
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
}
