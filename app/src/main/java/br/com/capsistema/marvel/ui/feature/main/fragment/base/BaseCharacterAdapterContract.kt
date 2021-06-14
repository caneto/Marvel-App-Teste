package br.com.capsistema.marvel.ui.feature.main.fragment.base

import br.com.capsistema.marvel.data.model.Character

interface BaseCharacterAdapterContract {

    interface Presenter {

        fun onFavoriteClick(character: Character)
    }
}