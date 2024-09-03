package com.denish.quizappcs.core.di

import com.denish.quizappcs.core.service.AuthService
import com.denish.quizappcs.data.repo.QuizRepo
import com.denish.quizappcs.data.repo.QuizRepoFirestore
import com.denish.quizappcs.data.repo.ResultRepo
import com.denish.quizappcs.data.repo.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Provides
    @Singleton
    fun provideQuizRepo(authService: AuthService): QuizRepo {
        return QuizRepoFirestore(authService)
    }

    @Provides
    @Singleton
    fun provideUserRepo(): UserRepo {
        return UserRepo()
    }

    @Provides
    @Singleton
    fun provideResultRepo(): ResultRepo {
        return ResultRepo()
    }
}