package br.com.capsistema.marvel.data.db

import br.com.capsistema.marvel.data.model.Character
import io.reactivex.Single

interface DaoAccess {

    fun getFavorite(character: Character): Single<Character>

    fun getFavorites(): Single<List<Character>>

    fun insertFavorite(character: br.com.capsistema.marvel.data.model.Character)

    fun deleteFavorite (character: br.com.capsistema.marvel.data.model.Character)

}