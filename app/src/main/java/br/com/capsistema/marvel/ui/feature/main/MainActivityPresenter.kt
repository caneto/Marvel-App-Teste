package br.com.capsistema.marvel.ui.feature.main

class MainActivityPresenter(private var view: MainActivityContract.View?) : MainActivityContract.Presenter {

    override fun start() {

    }

    override fun onDestroy() {
        view = null
    }

}