package ae.android.test.domain.di

import ae.android.test.domain.repository.BookRepository
import ae.android.test.data.repository.BookRepositoryImpl
import ae.android.test.data.db.BookDao
import ae.android.test.data.api.BookApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideBookRepository(
        bookDao: BookDao,
        bookApiService: BookApiService
    ): BookRepository {
        return BookRepositoryImpl(bookDao, bookApiService)
    }
}
