package br.com.capsistema.marvel.ui.feature.main.fragment.favorite

import br.com.capsistema.marvel.data.model.Character
import br.com.capsistema.marvel.ui.base.BaseUiContract
import br.com.capsistema.marvel.ui.feature.main.fragment.base.BaseCharacterAdapterContract

interface FavoriteListFragmentContract {

    interface View : BaseUiContract.View {
        fun showFavorites(characterList: List<Character>)

    }

    interface Presenter : BaseUiContract.Presenter, BaseCharacterAdapterContract.Presenter {
        fun refresh()

    }

}