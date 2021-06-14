package br.com.capsistema.marvel.ui.feature.main.fragment.character.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.capsistema.marvel.R
import br.com.capsistema.marvel.data.model.Character
import br.com.capsistema.marvel.ui.feature.main.fragment.base.BaseCharacterAdapterContract
import br.com.capsistema.marvel.utils.loadImage
import br.com.capsistema.marvel.utils.setPaletteColor
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class CharacterListAdapter(var list: ArrayList<Character>,
                           private var context: Context?,
                           private var presenter: BaseCharacterAdapterContract.Presenter,
                           private var screenWidth: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {


    private var filterList = list
    private var listFilter: CharacterListFilter? = null
    private var width = screenWidth/2
    private var height = screenWidth/2

    private var picasso = Picasso.get()

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_character, parent, false)

        val imageViewCharacter: AppCompatImageView = v.findViewById(R.id.imageViewCharacter)
        imageViewCharacter.layoutParams.width = screenWidth/2
        imageViewCharacter.layoutParams.height = screenWidth/2


        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val character = list[position]
        val viewHolder = holder as ViewHolder

        viewHolder.imageViewCharacter.loadImage("${character.thumbnail?.path}.${character.thumbnail?.extension}",
                {
                    viewHolder.textViewName.setPaletteColor(it)
                })

        val path = "${character.thumbnail?.path}.${character.thumbnail?.extension}"
        picasso.load(path)
             .resize(width,height)
             .into(viewHolder.imageViewCharacter)

        viewHolder.textViewName.text = character.name

        viewHolder.view.setOnClickListener {
            //            presenter.onListItemClick(position, it.findViewById(R.id.imageViewCharacter))
        }

        if(character.isFavorite) {
            viewHolder.imageViewFavorite.setImageResource(R.drawable.start_filled)
        } else {
            viewHolder.imageViewFavorite.setImageResource(R.drawable.star_bordered)
        }

        viewHolder.imageViewFavorite.tag = character
        viewHolder.imageViewFavorite.setOnClickListener {
            if(!character.isFavorite) {
                (it as AppCompatImageView).setImageResource(R.drawable.start_filled)
            } else {
                (it as AppCompatImageView).setImageResource(R.drawable.star_bordered)
            }
            presenter.onFavoriteClick(it.tag as Character)
        }
    }

    override fun getFilter(): Filter {
        if(listFilter == null) {
            listFilter = CharacterListFilter(filterList, this)
        }

        return listFilter!!
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView = view.findViewById(R.id.textViewCharacterName)
        var imageViewCharacter: AppCompatImageView = view.findViewById(R.id.imageViewCharacter)
        var imageViewFavorite: AppCompatImageView = view.findViewById(R.id.imageViewFavoriteStar)
    }

    fun addItems(newList: ArrayList<Character>, isFiltering: Boolean) {
        list.addAll(newList)

        if(!isFiltering) {
            notifyItemRangeInserted(list.size - newList.size, list.size - 1)
        }
    }

    fun removeAll() {
        list.clear()
        notifyDataSetChanged()
    }

}