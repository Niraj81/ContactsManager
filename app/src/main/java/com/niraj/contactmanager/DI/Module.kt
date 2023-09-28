package com.niraj.contactmanager.DI

import android.content.Context
import androidx.room.Room
import com.niraj.contactmanager.DB.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Singleton
    @Provides
    fun providesDatabase(
        @ApplicationContext app: Context
    ) : ContactDatabase {
        return Room.databaseBuilder(
            app,
            ContactDatabase::class.java,
            "contactDatabase"
        ).build()
    }

    @Singleton
    @Provides
    fun providesContactDao(db: ContactDatabase) = db.dao
}