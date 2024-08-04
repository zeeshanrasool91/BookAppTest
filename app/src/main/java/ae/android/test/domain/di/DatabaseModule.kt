package ae.android.test.domain.di

import ae.android.test.data.db.AppDatabase
import ae.android.test.data.db.BookDao
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "book_database"
        ).build()
    }

    @Provides
    fun provideBookDao(database: AppDatabase): BookDao {
        return database.bookDao()
    }
}
