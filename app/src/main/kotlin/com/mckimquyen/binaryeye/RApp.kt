package com.mckimquyen.binaryeye

import android.app.Application
import com.mckimquyen.binaryeye.database.Db
import com.mckimquyen.binaryeye.ext.setupApplovinAd
import com.mckimquyen.binaryeye.pref.Pref

//TODO firebase
//TODO roy93~ review in app

//done mckimquyen
//120hz
//font scale
//ad id
//leak canary
//proguard
//ui switch
//ic_launcher
//app version
//rate app
//more app
//share app
//policy
//double tap to exit
//keystore
//20 tester
//ad applovin

val db = Db()
val prefs = Pref()

class RApp : Application() {
    override fun onCreate() {
        super.onCreate()
        db.open(this)
        prefs.init(this)
        this.setupApplovinAd()
    }
}
