package br.com.capsistema.marvel.ui.feature.main.fragment.character

import br.com.capsistema.marvel.data.model.Character
import br.com.capsistema.marvel.ui.base.BaseUiContract
import br.com.capsistema.marvel.ui.feature.main.fragment.base.BaseCharacterAdapterContract

interface CharacterListFragmentContract {

    interface View : BaseUiContract.View {

        fun addCharacters(results: ArrayList<Character>, isFiltering: Boolean)
        fun showLoading()
        fun hideLoading()
        fun showLoadingFooter()
        fun showInternetError()
        fun showGenericError()
        fun hideTryAgain()
        fun refreshCharacters(results: ArrayList<Character>)
        fun clearList()
        fun searchTerm(query: String?)
    }

    interface Presenter : BaseUiContract.Presenter, BaseCharacterAdapterContract.Presenter {

        fun getCharacters(currentPage: Int)

        fun onTryAgainClick()
        fun refresh()
        fun performSearch(query: String?)
        fun canLoadMore(): Boolean
    }

}