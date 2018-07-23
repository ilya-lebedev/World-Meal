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

package io.github.ilya_lebedev.worldmeal.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.AppExecutors;
import io.github.ilya_lebedev.worldmeal.data.database.AreaEntry;
import io.github.ilya_lebedev.worldmeal.data.database.AreaMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryEntry;
import io.github.ilya_lebedev.worldmeal.data.database.WorldMealDao;
import io.github.ilya_lebedev.worldmeal.data.network.WorldMealNetworkDataSource;

/**
 * WorldMealRepository
 */
public class WorldMealRepository {

    // For singleton instantiation
    private static final Object LOCK = new Object();
    private static WorldMealRepository sInstance;

    private final WorldMealDao mWorldMealDao;
    private final WorldMealNetworkDataSource mNetworkDataSource;
    private final AppExecutors mAppExecutors;

    private WorldMealRepository(WorldMealDao worldMealDao,
                                WorldMealNetworkDataSource networkDataSource,
                                AppExecutors appExecutors) {
        mWorldMealDao = worldMealDao;
        mNetworkDataSource = networkDataSource;
        mAppExecutors = appExecutors;

        initializeNetworkDataObservers();
    }

    private void initializeNetworkDataObservers() {
        LiveData<AreaEntry[]> networkAreaListData = mNetworkDataSource.getCurrentAreaList();
        networkAreaListData.observeForever(new Observer<AreaEntry[]>() {
            @Override
            public void onChanged(@Nullable final AreaEntry[] areaEntries) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mWorldMealDao.bulkInsert(areaEntries);
                    }
                });
            }
        });

        LiveData<CategoryEntry[]> networkCategoryListData = mNetworkDataSource.getCurrentCategoryList();
        networkCategoryListData.observeForever(new Observer<CategoryEntry[]>() {
            @Override
            public void onChanged(@Nullable final CategoryEntry[] categoryEntries) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mWorldMealDao.bulkInsert(categoryEntries);
                    }
                });
            }
        });

        LiveData<AreaMealEntry[]> networkAreaMealData = mNetworkDataSource.getCurrentAreaMealList();
        networkAreaMealData.observeForever(new Observer<AreaMealEntry[]>() {
            @Override
            public void onChanged(@Nullable final AreaMealEntry[] areaMealEntries) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mWorldMealDao.bulkInsert(areaMealEntries);
                    }
                });
            }
        });
    }

    public static WorldMealRepository getInstance(WorldMealDao worldMealDao,
                                           WorldMealNetworkDataSource networkDataSource,
                                           AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WorldMealRepository(worldMealDao, networkDataSource, appExecutors);
            }
        }

        return sInstance;
    }

    public LiveData<List<AreaEntry>> getAreaList() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isAreaListFetchNeeded()) {
                    startFetchAreaList();
                }
            }
        });

        return mWorldMealDao.getAreaList();
    }

    public LiveData<List<CategoryEntry>> getCategoryList() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isCategoryListFetchNeeded()) {
                    startFetchCategoryList();
                }
            }
        });

        return mWorldMealDao.getCategoryList();
    }

    public LiveData<List<AreaMealEntry>> getAreaMealList(final String areaName) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isAreaMealListFetchNeeded(areaName)) {
                    startFetchAreaMealList(areaName);
                }
            }
        });

        return mWorldMealDao.getAreaMeal(areaName);
    }

    private boolean isAreaListFetchNeeded() {
        int areaCount = mWorldMealDao.countAllArea();
        return (areaCount == 0);
    }

    private boolean isCategoryListFetchNeeded() {
        int categoryCount = mWorldMealDao.countAllCategory();
        return (categoryCount ==0);
    }

    private boolean isAreaMealListFetchNeeded(String areaName) {
        int areaMealCount = mWorldMealDao.countAllAreaMeal(areaName);
        return (areaMealCount == 0);
    }

    private void startFetchAreaList() {
        mNetworkDataSource.startFetchAreaList();
    }

    private void startFetchCategoryList() {
        mNetworkDataSource.startFetchCategoryList();
    }

    private void startFetchAreaMealList(String areaName) {
        mNetworkDataSource.startFetchAreaMealList(areaName);
    }

}
