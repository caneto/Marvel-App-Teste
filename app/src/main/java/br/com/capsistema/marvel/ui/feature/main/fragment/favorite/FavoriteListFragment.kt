package br.com.capsistema.marvel.ui.feature.main.fragment.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.capsistema.marvel.R
import br.com.capsistema.marvel.data.DataManager
import br.com.capsistema.marvel.data.db.DatabaseManager
import br.com.capsistema.marvel.data.model.Character
import br.com.capsistema.marvel.data.network.ApiManager
import br.com.capsistema.marvel.ui.feature.main.fragment.character.adapter.CharacterListAdapter
import br.com.capsistema.marvel.utils.ScreenUtils
import kotlinx.android.synthetic.main.fragment_favorite_list.*

class FavoriteListFragment : Fragment(), FavoriteListFragmentContract.View, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: FavoriteListFragmentContract.Presenter

    private var adapter: CharacterListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        presenter = FavoriteListFragmentPresenter(this, DataManager(ApiManager(), DatabaseManager()))
        presenter.start()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        swipeRefreshFavoriteList.setOnRefreshListener(this)

        val layoutManager =
            GridLayoutManager(context, 2)
        recyclerFavoriteList.layoutManager = layoutManager
    }


    /*
        SwipeRefresh Listener
     */
    override fun onRefresh() {
        swipeRefreshFavoriteList.isRefreshing = true
        presenter.refresh()
    }


    /*
        View Contract
     */
    override fun showFavorites(characterList: List<Character>) {
        swipeRefreshFavoriteList.isRefreshing = false

        if (characterList.isNotEmpty()) {
            textViewNoFavoritesFound.visibility = View.GONE
        } else {
            textViewNoFavoritesFound.visibility = View.VISIBLE
        }

        if (adapter != null) {
            adapter?.removeAll()
        }

        val list = ArrayList<Character>()
        if(characterList.isNotEmpty()) {
            list.addAll(characterList)
        }

        adapter = CharacterListAdapter(list, context, presenter, ScreenUtils.getScreenWidth(activity?.windowManager))
        recyclerFavoriteList.adapter = adapter
    }

}