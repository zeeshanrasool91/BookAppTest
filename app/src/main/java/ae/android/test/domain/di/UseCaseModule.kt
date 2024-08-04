package ae.android.test.domain.di

import ae.android.test.domain.repository.BookRepository
import ae.android.test.domain.usecase.FetchBooksUseCase
import ae.android.test.domain.usecase.UpdateBookUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideFaceBookUseCase(
        bookRepository: BookRepository
    ): FetchBooksUseCase {
        return FetchBooksUseCase(bookRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateBookUseCase(
        bookRepository: BookRepository
    ): UpdateBookUseCase {
        return UpdateBookUseCase(bookRepository)
    }

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
