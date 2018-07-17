/*
 * Copyright (C) 2018 Ilya Lebedev
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

package io.github.ilya_lebedev.worldmeal.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import java.net.URL;

import io.github.ilya_lebedev.worldmeal.AppExecutors;
import io.github.ilya_lebedev.worldmeal.data.database.AreaEntry;
import io.github.ilya_lebedev.worldmeal.data.network.response.AreaListResponse;

/**
 * WorldMealNetworkDataSource
 * Provides an API for doing all operations with the server data
 */
public class WorldMealNetworkDataSource {

    // For singleton instantiation
    private static final Object LOCK = new Object();
    private static WorldMealNetworkDataSource sInstance;

    private final Context mContext;
    private final AppExecutors mExecutors;

    private final MutableLiveData<AreaEntry[]> mDownloadedAreaEntry;

    private WorldMealNetworkDataSource(Context context, AppExecutors appExecutors) {
        mContext = context;
        mExecutors = appExecutors;

        mDownloadedAreaEntry = new MutableLiveData<>();
    }

    public static WorldMealNetworkDataSource getInstance(Context context, AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WorldMealNetworkDataSource(context, appExecutors);
            }
        }

        return sInstance;
    }

    public LiveData<AreaEntry[]> getCurrentAreaList() {
        return mDownloadedAreaEntry;
    }

    public void startFetchAreaList() {
        WorldMealFetchUtils.startFetchAreaList(mContext);
    }

    public void fetchAreaList() {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL areaListRequestUrl = WorldMealNetworkUtils.getAreaListUrl();

                    String jsonAreaListResponse = WorldMealNetworkUtils.
                            getResponseFromHttpUrl(areaListRequestUrl);

                    AreaListResponse response = MealDbJsonParser.parseAreaList(jsonAreaListResponse);

                    if (response != null && response.getAreaList().length != 0) {
                        mDownloadedAreaEntry.postValue(response.getAreaList());
                    }
                } catch (Exception e) {
                    // Server probably invalid
                    e.printStackTrace();
                }
            }
        });
    }

    void fetchCategoryList() {
        // TODO
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                // TODO
            }
        });
    }

    void fetchIngredientList() {
        // TODO
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                // TODO
            }
        });
    }

    void fetchAreaMealList(String areaName) {
        // TODO
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                // TODO
            }
        });
    }

    void fetchCategoryMealList(String categoryName) {
        // TODO
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                // TODO
            }
        });
    }

    void fetchIngredientMealList(String ingredientName) {
        // TODO
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                // TODO
            }
        });
    }

}
