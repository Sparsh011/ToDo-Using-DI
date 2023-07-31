package com.example.todoappcompose.di

import android.content.Context
import androidx.room.Room
import com.example.todoappcompose.data.ToDoDao
import com.example.todoappcompose.data.ToDoDatabase
import com.example.todoappcompose.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ToDoDatabase {
        return Room.databaseBuilder(
            context,
            ToDoDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideDao(database: ToDoDatabase): ToDoDao {
        return database.toDoDao()
    }
}