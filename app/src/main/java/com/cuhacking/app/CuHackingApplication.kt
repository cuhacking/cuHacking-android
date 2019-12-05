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

package com.cuhacking.app

import android.app.Application
import com.cuhacking.app.data.DefaultDataInfoProvider
import com.cuhacking.app.di.CoreComponent
import com.cuhacking.app.di.DaggerComponentProvider
import com.cuhacking.app.di.DaggerCoreComponent
import com.cuhacking.app.di.SharedPreferencesModule
import com.jakewharton.threetenabp.AndroidThreeTen

class CuHackingApplication : Application(), DaggerComponentProvider {

    override val component: CoreComponent by lazy {
        DaggerCoreComponent.builder()
            .applicationContext(this)
            .sharedPreferenceModule(SharedPreferencesModule(DefaultDataInfoProvider.INFO_PREF))
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        component.inject(this)
    }

}