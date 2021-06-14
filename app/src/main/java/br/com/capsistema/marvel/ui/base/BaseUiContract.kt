package br.com.capsistema.marvel.ui.base

interface BaseUiContract {
    interface View {

    }

    interface Presenter {
        fun start()
        fun onDestroy()
    }

}