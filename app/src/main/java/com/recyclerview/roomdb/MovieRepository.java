package com.recyclerview.roomdb;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.recyclerview.modal.Movie_Data;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<Movie_Data>> mAllWords;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    MovieRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.wordDao();
        mAllWords = mMovieDao.getAlphabetizedWords();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Movie_Data>> getAllWords() {
        return mAllWords;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    void insert(Movie_Data word) {
        new insertAsyncTask(mMovieDao).execute(word);
    }

    void insertAll(List<Movie_Data> movies) {
        new insertAllAsyncTask(mMovieDao).execute(movies);
    }

    void deleteAll() {
        new deleteAsyncTask(mMovieDao).execute();
    }
    private static class insertAsyncTask extends AsyncTask<Movie_Data, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie_Data... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }



    private static class insertAllAsyncTask extends AsyncTask<List<Movie_Data>, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAllAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Movie_Data>... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }



    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
