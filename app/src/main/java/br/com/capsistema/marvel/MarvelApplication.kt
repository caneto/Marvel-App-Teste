package br.com.capsistema.marvel

import androidx.multidex.MultiDexApplication
import io.realm.Realm
import io.realm.RealmConfiguration

class MarvelApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this)

        //RealmConfiguration.Builder.allowWritesOnUiThread(true)
    }

}