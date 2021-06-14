package br.com.capsistema.marvel.ui.feature.main.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import br.com.capsistema.marvel.R
import br.com.capsistema.marvel.ui.feature.main.fragment.character.CharacterListFragment
import br.com.capsistema.marvel.ui.feature.main.fragment.favorite.FavoriteListFragment
import java.util.*

class ViewPagerAdapter(private val context: Context,
                       private val manager: FragmentManager
) : FragmentStatePagerAdapter(manager) {

    private val mFragmentsList: MutableList<Fragment> by lazy {
        ArrayList<Fragment>()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val charactersFragment = CharacterListFragment()
                mFragmentsList.add(charactersFragment)
                charactersFragment
            }
            1 -> {
                val favoritesFragment = FavoriteListFragment()
                mFragmentsList.add(favoritesFragment)
                favoritesFragment
            }
            else -> {
                // Trocar para outro fragment
                val favoritesFragment = FavoriteListFragment()
                mFragmentsList.add(favoritesFragment)
                favoritesFragment
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.characters)
            1 -> context.getString(R.string.favorites)
            else -> null
        }
    }

}
