package br.com.capsistema.marvel.ui.feature.main.fragment.character

import android.animation.ValueAnimator
import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.view.*
import android.view.animation.Animation
import br.com.capsistema.marvel.R
import br.com.capsistema.marvel.data.DataManager
import br.com.capsistema.marvel.data.db.DatabaseManager
import br.com.capsistema.marvel.data.model.Character
import br.com.capsistema.marvel.data.network.ApiManager
import br.com.capsistema.marvel.ui.feature.main.fragment.character.adapter.CharacterListAdapter
import br.com.capsistema.marvel.utils.EndlessRecyclerViewScrollListener
import br.com.capsistema.marvel.utils.ScreenUtils
import kotlinx.android.synthetic.main.fragment_character_list.*


class CharacterListFragment : Fragment(), CharacterListFragmentContract.View, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: CharacterListFragmentContract.Presenter

    private var adapter: CharacterListAdapter? = null
    private var animatorFooter: ValueAnimator? = null
    private var tryAgainSnackBar: Snackbar? = null
    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        presenter = CharacterListFragmentPresenter(this, DataManager(ApiManager(), DatabaseManager()))
        presenter.start()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.main_menu, menu)

        val manager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView?.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {

                presenter.performSearch(query)
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        swipeRefreshCharacterList.setOnRefreshListener(this)

        val layoutManager =
            GridLayoutManager(context, 2)
        recyclerCharacterList.layoutManager = layoutManager
        recyclerCharacterList.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                presenter.getCharacters(page)
            }

            override fun canLoadMore(): Boolean {
                return presenter.canLoadMore()
            }
        })
    }


    /*
        SwipeRefresh Listener
     */
    override fun onRefresh() {
        hideTryAgain()
        searchView?.setQuery("", true)
        searchView?.clearFocus()
        searchView?.onActionViewCollapsed()
        swipeRefreshCharacterList.isRefreshing = true
        presenter.refresh()
    }


    /*
        View Contract
     */
    /**
     * Add the given results to the characters list
     */
    override fun addCharacters(results: ArrayList<Character>, isFiltering: Boolean) {
        if (adapter == null) {
            adapter = CharacterListAdapter(results, context, presenter, ScreenUtils.getScreenWidth(activity?.windowManager))
            recyclerCharacterList.adapter = adapter
        } else {
            adapter?.addItems(results, isFiltering)
        }
    }

    /**
     * Clear the character list
     */
    override fun clearList() {
        adapter?.removeAll()
    }

    /**
     * Search the given term
     */
    override fun searchTerm(query: String?) {
        adapter?.filter?.filter(query)
    }

    /**
     * Refreshes the list of characters
     */
    override fun refreshCharacters(results: ArrayList<Character>) {
        adapter = CharacterListAdapter(results, context, presenter, ScreenUtils.getScreenWidth(activity?.windowManager))
        recyclerCharacterList.adapter = adapter
    }

    /**
     * Show the loading view at the middle of the screen
     */
    override fun showLoading() {
        lottieLoading.visibility = View.VISIBLE

        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1500)
        animator.addUpdateListener { valueAnimator -> lottieLoading?.progress = valueAnimator.animatedValue as Float }

        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE

        //lottieLoading?.useHardwareAcceleration(true)
        lottieLoading?.enableMergePathsForKitKatAndAbove(true)

        animator.start()
    }

    /**
     * Show the loading view on footer of the screen
     */
    override fun showLoadingFooter() {
        animatorFooter = ValueAnimator.ofFloat(0F, 1F)
        animatorFooter?.duration = 2000

        val hsv: FloatArray
        var runColor: Int

        hsv = FloatArray(3)
        hsv[1] = 1f
        hsv[2] = 1f
        animatorFooter?.addUpdateListener { animation ->
            hsv[0] = 360 * animation.animatedFraction

            runColor = Color.HSVToColor(hsv)
            viewFooterLoading?.setBackgroundColor(runColor)
        }

        animatorFooter?.repeatCount = Animation.INFINITE
        animatorFooter?.repeatMode = ValueAnimator.REVERSE

        animatorFooter?.start()
        viewFooterLoading.visibility = View.VISIBLE
    }

    /**
     *  Hide All loadings on screen
     */
    override fun hideLoading() {
        lottieLoading.visibility = View.GONE
        lottieLoading.cancelAnimation()
        viewFooterLoading.visibility = View.GONE
        animatorFooter?.end()
        swipeRefreshCharacterList.isRefreshing = false
    }

    /**
     * Show the error alert in case of internet error
     */
    override fun showInternetError() {
        showError(R.string.try_again_internet_error)
    }

    /**
     * Show the error alert in case of generic error
     */
    override fun showGenericError() {
        showError(R.string.try_again_generic_error)
    }

    /**
     * Hide the error alert
     */
    override fun hideTryAgain() {
        tryAgainSnackBar?.dismiss()
    }


    /*
        Util
     */
    private fun showError(message: Int) {
        tryAgainSnackBar = Snackbar
                .make(coordinatorCharacterList, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again) {
                    searchView?.setQuery("", true)
                    searchView?.clearFocus()
                    searchView?.onActionViewCollapsed()
                    presenter.onTryAgainClick()
                }

        tryAgainSnackBar?.show()
    }
}