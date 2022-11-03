package com.tocaboca.tocalifew

import android.app.Application
import android.content.Context
import com.onesignal.OneSignal
import com.orhanobut.hawk.Hawk
import com.tocaboca.tocalifew.black.Advert
import com.tocaboca.tocalifew.black.CNST
import com.tocaboca.tocalifew.black.CNST.ONESIGNAL_APP_ID
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class AppClass: Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        GlobalScope.launch(Dispatchers.IO) {
            applyDeviceId(context = applicationContext)
        }
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)


    }

    private suspend fun applyDeviceId(context: Context) {
        val advertisingInfo = Advert(context)
        val idInfo = advertisingInfo.getAdvertisingId()
        Hawk.put(CNST.MAIN_ID, idInfo)
    }
}