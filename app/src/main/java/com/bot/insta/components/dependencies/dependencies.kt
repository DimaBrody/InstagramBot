package com.bot.insta.components.dependencies

import android.content.Context
import com.bot.insta.components.factory.ViewModelFactory
import com.bot.insta.components.mvvm.viewmodel.ViewModelProvider
import com.bot.insta.data.UserDatabase
import com.bot.insta.data.repository.DatabaseRepository
import com.bot.insta.data.repository.DatabaseRepositoryImpl
import com.bot.insta.network.api.InstagramApi
import com.bot.insta.network.source.NetworkDataSource
import com.bot.insta.network.source.NetworkDataSourceImpl
import com.bot.insta.tools.prefs.PreferenceProvider

val viewModelFactory: ViewModelFactory
    get() = Dependencies.factorySingleton

val instagramApi: InstagramApi
    get() = Dependencies.instagramApiSingleton

val prefs: PreferenceProvider
    get() = Dependencies.prefsSingleton

val networkDataSource: NetworkDataSource
    get() = Dependencies.networkDataSourceSingleton

val databaseRepository: DatabaseRepository
    get() = Dependencies.databaseRepositorySingleton

fun dependencies(context: () -> Context) {
    Dependencies.context = context().applicationContext
    ViewModelProvider.set(get())
}

private object Dependencies {

    lateinit var context: Context

    private var factorySingletonContainer: ViewModelFactory? = null

    private var instagramApiSingletonContainer: InstagramApi? = null

    private var prefsSingletonContainer: PreferenceProvider? = null

    private var networkDataSourceSingletonContainer: NetworkDataSource? = null

    private var databaseSingletonContainer: UserDatabase? = null

    private var databaseRepositorySingletonContainer: DatabaseRepository? = null

    val factorySingleton: ViewModelFactory
        get() = factorySingletonContainer ?: ViewModelFactory(get(), get())
            .also { factorySingletonContainer = it }

    val instagramApiSingleton: InstagramApi
        get() = instagramApiSingletonContainer ?: InstagramApi("", "")
            .also { instagramApiSingletonContainer = it }

    val prefsSingleton: PreferenceProvider
        get() = prefsSingletonContainer ?: PreferenceProvider(context)
            .also { prefsSingletonContainer = it }

    val networkDataSourceSingleton: NetworkDataSource
        get() = networkDataSourceSingletonContainer ?: NetworkDataSourceImpl(get())
            .also { networkDataSourceSingletonContainer = it }

    val databaseSingleton: UserDatabase
        get() = databaseSingletonContainer ?: UserDatabase(context)
            .also { databaseSingletonContainer = it }

    val databaseRepositorySingleton: DatabaseRepository
        get() = databaseRepositorySingletonContainer
            ?: DatabaseRepositoryImpl(databaseSingleton.userDao())
                .also { databaseRepositorySingletonContainer = it }

}

inline fun <reified D> get() = with(D::class.java) {
    when {
        isAssignableFrom(ViewModelFactory::class.java) -> viewModelFactory as D
        isAssignableFrom(PreferenceProvider::class.java) -> prefs as D
        isAssignableFrom(InstagramApi::class.java) -> instagramApi as D
        isAssignableFrom(NetworkDataSource::class.java) -> networkDataSource as D
        isAssignableFrom(DatabaseRepository::class.java) -> databaseRepository as D
        else -> throw IllegalArgumentException("Wrong class.")
    }
}

inline fun <reified D> instance(): Lazy<D> = lazy { get<D>() }