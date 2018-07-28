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

import java.util.Date;
import java.util.List;

import io.github.ilya_lebedev.worldmeal.AppExecutors;
import io.github.ilya_lebedev.worldmeal.data.database.AreaEntry;
import io.github.ilya_lebedev.worldmeal.data.database.AreaMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.IngredientEntry;
import io.github.ilya_lebedev.worldmeal.data.database.IngredientMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.MealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.MealOfDayEntry;
import io.github.ilya_lebedev.worldmeal.data.database.WorldMealDao;
import io.github.ilya_lebedev.worldmeal.data.network.WorldMealNetworkDataSource;
import io.github.ilya_lebedev.worldmeal.utilities.WorldMealDateUtils;

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

    private boolean mInitialized = false;

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

        LiveData<IngredientEntry[]> networkIngredientListData = mNetworkDataSource
                .getCurrentIngredientList();
        networkIngredientListData.observeForever(new Observer<IngredientEntry[]>() {
            @Override
            public void onChanged(@Nullable final IngredientEntry[] ingredientEntries) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mWorldMealDao.bulkInsert(ingredientEntries);
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

        LiveData<CategoryMealEntry[]> networkCategoryMealData =
                mNetworkDataSource.getCurrentCategoryMealList();
        networkCategoryMealData.observeForever(new Observer<CategoryMealEntry[]>() {
            @Override
            public void onChanged(@Nullable final CategoryMealEntry[] categoryMealEntries) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mWorldMealDao.bulkInsert(categoryMealEntries);
                    }
                });
            }
        });

        LiveData<IngredientMealEntry[]> networkIngredientMealData =
                mNetworkDataSource.getCurrentIngredientMealList();
        networkIngredientMealData.observeForever(new Observer<IngredientMealEntry[]>() {
            @Override
            public void onChanged(@Nullable final IngredientMealEntry[] ingredientMealEntries) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mWorldMealDao.bulkInsert(ingredientMealEntries);
                    }
                });
            }
        });

        LiveData<MealEntry> networkMealData = mNetworkDataSource.getCurrentMeal();
        networkMealData.observeForever(new Observer<MealEntry>() {
            @Override
            public void onChanged(@Nullable final MealEntry mealEntry) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mWorldMealDao.insert(mealEntry);
                    }
                });
            }
        });

        LiveData<MealOfDayEntry> networkMealOfDayData = mNetworkDataSource.getCurrentMealOfDay();
        networkMealOfDayData.observeForever(new Observer<MealOfDayEntry>() {
            @Override
            public void onChanged(@Nullable final MealOfDayEntry mealOfDayEntry) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        deleteOldMealOfDay();
                        mWorldMealDao.insert(mealOfDayEntry);
                    }
                });
            }
        });
    }

    private synchronized void initializeMealOfDayData() {
        if (mInitialized) return;
        mInitialized = true;

        mNetworkDataSource.scheduleRecurringFetchMealOfDaySync();

        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isMealOfDayFetchNeeded()) {
                    startFetchMealOfDay();
                }
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

            sInstance.initializeMealOfDayData();
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

    public LiveData<List<IngredientEntry>> getIngredientList() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isIngredientListFetchNeeded()) {
                    startFetchIngredientList();
                }
            }
        });

        return mWorldMealDao.getIngredientList();
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

    public LiveData<List<CategoryMealEntry>> getCategoryMealList(final String categoryName) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isCategoryMealListFetchNeeded(categoryName)) {
                    startFetchCategoryMealList(categoryName);
                }
            }
        });

        return mWorldMealDao.getCategoryMeal(categoryName);
    }

    public LiveData<List<IngredientMealEntry>> getIngredientMealList(final String ingredientName) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isIngredientMealListFetchNeeded(ingredientName)) {
                    startFetchIngredientMealList(ingredientName);
                }
            }
        });

        return mWorldMealDao.getIngredientMeal(ingredientName);
    }

    public LiveData<MealEntry> getMeal(final long mealId) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isMealFetchNeeded(mealId)) {
                    startFetchMeal(mealId);
                }
            }
        });

        return mWorldMealDao.getMeal(mealId);
    }

    public MealOfDayEntry getMealOfDay() {
        return mWorldMealDao.getMealOfDay();
    }

    private boolean isAreaListFetchNeeded() {
        int areaCount = mWorldMealDao.countAllArea();
        return (areaCount == 0);
    }

    private boolean isCategoryListFetchNeeded() {
        int categoryCount = mWorldMealDao.countAllCategory();
        return (categoryCount ==0);
    }

    private boolean isIngredientListFetchNeeded() {
        int ingredientCount = mWorldMealDao.countAllIngredient();
        return (ingredientCount == 0);
    }

    private boolean isAreaMealListFetchNeeded(String areaName) {
        int areaMealCount = mWorldMealDao.countAllAreaMeal(areaName);
        return (areaMealCount == 0);
    }

    private boolean isCategoryMealListFetchNeeded(String categoryName) {
        int categoryMealCount = mWorldMealDao.countAllCategoryMeal(categoryName);
        return (categoryMealCount == 0);
    }

    private boolean isIngredientMealListFetchNeeded(String ingredientName) {
        int ingredientMealCount = mWorldMealDao.countAllIngredientMeal(ingredientName);
        return (ingredientMealCount == 0);
    }

    private boolean isMealFetchNeeded(long mealId) {
        int mealCount = mWorldMealDao.countMeal(mealId);
        return (mealCount == 0);
    }

    private boolean isMealOfDayFetchNeeded() {
        int mealOfDayCount = mWorldMealDao.countAllMealOfDay();
        return (mealOfDayCount == 0);
    }

    private void deleteOldMealOfDay() {
        Date today = WorldMealDateUtils.getNormalizedUtcDateForToday();
        mWorldMealDao.deleteOldMealOfDay(today);
    }

    private void startFetchAreaList() {
        mNetworkDataSource.startFetchAreaList();
    }

    private void startFetchCategoryList() {
        mNetworkDataSource.startFetchCategoryList();
    }

    private void startFetchIngredientList() {
        mNetworkDataSource.startFetchIngredientList();
    }

    private void startFetchAreaMealList(String areaName) {
        mNetworkDataSource.startFetchAreaMealList(areaName);
    }

    private void startFetchCategoryMealList(String categoryName) {
        mNetworkDataSource.startFetchCategoryMealList(categoryName);
    }

    private void startFetchIngredientMealList(String ingredientName) {
        mNetworkDataSource.startFetchIngredientMealList(ingredientName);
    }

    private void startFetchMeal(long mealId) {
        mNetworkDataSource.startFetchMeal(mealId);
    }

    private void startFetchMealOfDay() {
        mNetworkDataSource.startFetchMealOfDay();
    }

}
