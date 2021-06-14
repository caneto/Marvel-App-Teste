package br.com.capsistema.marvel.ui.feature.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.capsistema.marvel.R
import br.com.capsistema.marvel.ui.feature.main.adapter.ViewPagerAdapter
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private lateinit var presenter: MainActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupTabLayout()

        val config = RealmConfiguration.Builder()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        val realm = Realm.getInstance(config)
        Log.v("Config", "Successfully opened a realm at: ${realm.path}")

        presenter = MainActivityPresenter(this)
        presenter.start()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }


    private fun setupActionBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setIcon(R.drawable.ic_launcher_foreground)
    }

    private fun setupTabLayout(){
        viewPager.adapter = ViewPagerAdapter(this, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}
