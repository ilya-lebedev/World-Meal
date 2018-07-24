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
import io.github.ilya_lebedev.worldmeal.data.database.AreaMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryMealEntry;
import io.github.ilya_lebedev.worldmeal.data.network.response.AreaListResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.AreaMealResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.CategoryListResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.CategoryMealResponse;

/**
 * WorldMealNetworkDataSource
 * Provides an API for doing all operations with the server data
 */
public class WorldMealNetworkDataSource {

    private static final String TAG = "WorldMealNetworkDataSrc";

    // For singleton instantiation
    private static final Object LOCK = new Object();
    private static WorldMealNetworkDataSource sInstance;

    private final Context mContext;
    private final AppExecutors mExecutors;

    private final MutableLiveData<AreaEntry[]> mDownloadedAreaEntry;
    private final MutableLiveData<CategoryEntry[]> mDownloadedCategoryEntry;
    private final MutableLiveData<AreaMealEntry[]> mDownloadedAreaMealEntry;
    private final MutableLiveData<CategoryMealEntry[]> mDownloadedCategoryMealEntry;

    private WorldMealNetworkDataSource(Context context, AppExecutors appExecutors) {
        mContext = context;
        mExecutors = appExecutors;

        mDownloadedAreaEntry = new MutableLiveData<>();
        mDownloadedCategoryEntry = new MutableLiveData<>();
        mDownloadedAreaMealEntry = new MutableLiveData<>();
        mDownloadedCategoryMealEntry = new MutableLiveData<>();
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

    public LiveData<CategoryEntry[]> getCurrentCategoryList() {
        return mDownloadedCategoryEntry;
    }

    public LiveData<AreaMealEntry[]> getCurrentAreaMealList() {
        return mDownloadedAreaMealEntry;
    }

    public LiveData<CategoryMealEntry[]> getCurrentCategoryMealList() {
        return mDownloadedCategoryMealEntry;
    }

    public void startFetchAreaList() {
        WorldMealFetchUtils.startFetchAreaList(mContext);
    }

    public void startFetchCategoryList() {
        WorldMealFetchUtils.startFetchCategoryList(mContext);
    }

    public void startFetchAreaMealList(String areaName) {
        WorldMealFetchUtils.startFetchAreaMealList(mContext, areaName);
    }

    public void startFetchCategoryMealList(String categoryName) {
       WorldMealFetchUtils.startFetchCategoryMealList(mContext, categoryName);
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
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL categoryListRequestUrl = WorldMealNetworkUtils.getCategoryListUrl();

                    String jsonCategoryListResponse = WorldMealNetworkUtils
                            .getResponseFromHttpUrl(categoryListRequestUrl);

                    CategoryListResponse response = MealDbJsonParser
                            .parseCategoryList(jsonCategoryListResponse);

                    if (response != null && response.getCategoryList().length != 0) {
                        mDownloadedCategoryEntry.postValue(response.getCategoryList());
                    }
                } catch (Exception e) {
                    // Server probably invalid
                    e.printStackTrace();
                }
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

    void fetchAreaMealList(final String areaName) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL areaMealRequestUrl = WorldMealNetworkUtils.getAreaMealUrl(areaName);

                    String jsonAreaMealResponse = WorldMealNetworkUtils
                            .getResponseFromHttpUrl(areaMealRequestUrl);

                    AreaMealResponse response = MealDbJsonParser
                            .paresAreaMealList(jsonAreaMealResponse, areaName);

                    if (response != null && response.getAreaMeal().length != 0) {
                        mDownloadedAreaMealEntry.postValue(response.getAreaMeal());
                    }
                } catch (Exception e) {
                    // Server probably invalid
                    e.printStackTrace();
                }
            }
        });
    }

    void fetchCategoryMealList(final String categoryName) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    URL categoryMealRequestUrl = WorldMealNetworkUtils.getCategoryMealUrl(categoryName);

                    String jsonCategoryMealResponse = WorldMealNetworkUtils
                            .getResponseFromHttpUrl(categoryMealRequestUrl);

                    CategoryMealResponse response = MealDbJsonParser
                            .parseCategoryMealList(jsonCategoryMealResponse, categoryName);

                    if (response != null && response.getCategoryMeal().length != 0) {
                        mDownloadedCategoryMealEntry.postValue(response.getCategoryMeal());
                    }
                } catch (Exception e) {
                    // Server probably invalid
                    e.printStackTrace();
                }
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
