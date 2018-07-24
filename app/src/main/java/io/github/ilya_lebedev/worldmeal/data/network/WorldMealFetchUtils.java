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

import android.content.Context;
import android.content.Intent;

import io.github.ilya_lebedev.worldmeal.utilities.WorldMealInjectorUtils;

/**
 * WorldMealFetchUtils
 */
public class WorldMealFetchUtils {

    public static final String TAG = "WorldMealFetchUtils";

    public static final String ACTION_FETCH_AREA_LIST = "fetch_area_list";
    public static final String ACTION_FETCH_CATEGORY_LIST = "fetch_category_list";
    public static final String ACTION_FETCH_AREA_MEAL_LIST = "fetch_area_meal_list";
    public static final String ACTION_FETCH_CATEGORY_MEAL_LIST = "fetch_category_meal_list";

    private static final String EXTRA_AREA_NAME = "area_name";
    private static final String EXTRA_CATEGORY_NAME = "category_name";

    public static void fetch(Context context, Intent fetchIntent) {
        WorldMealNetworkDataSource networkDataSource =
                WorldMealInjectorUtils.provideNetworkDataSource(context.getApplicationContext());
        String action = fetchIntent.getAction();

        if (ACTION_FETCH_AREA_LIST.equals(action)) {
            networkDataSource.fetchAreaList();
        } else if (ACTION_FETCH_CATEGORY_LIST.equals(action)) {
            networkDataSource.fetchCategoryList();
        } else if (ACTION_FETCH_AREA_MEAL_LIST.equals(action)) {
            String areaName = fetchIntent.getStringExtra(EXTRA_AREA_NAME);
            networkDataSource.fetchAreaMealList(areaName);
        } else if (ACTION_FETCH_CATEGORY_MEAL_LIST.equals(action)) {
            String categoryName = fetchIntent.getStringExtra(EXTRA_CATEGORY_NAME);
            networkDataSource.fetchCategoryMealList(categoryName);
        } else {
            throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }

    public static void startFetchAreaList(Context context) {
        Intent fetchIntent = new Intent(context.getApplicationContext(),
                WorldMealSyncIntentService.class);
        fetchIntent.setAction(ACTION_FETCH_AREA_LIST);
        context.startService(fetchIntent);
    }

    public static void startFetchCategoryList(Context context) {
        Intent fetchIntent = new Intent(context.getApplicationContext(),
                WorldMealSyncIntentService.class);
        fetchIntent.setAction(ACTION_FETCH_CATEGORY_LIST);
        context.startService(fetchIntent);
    }

    public static void startFetchAreaMealList(Context context, String areaName) {
        Intent fetchIntent = new Intent(context.getApplicationContext(),
                WorldMealSyncIntentService.class);
        fetchIntent.setAction(ACTION_FETCH_AREA_MEAL_LIST);
        fetchIntent.putExtra(EXTRA_AREA_NAME, areaName);
        context.startService(fetchIntent);
    }

    public static void startFetchCategoryMealList(Context context, String categoryName) {
        Intent fetchIntent = new Intent(context.getApplicationContext(),
                WorldMealSyncIntentService.class);
        fetchIntent.setAction(ACTION_FETCH_CATEGORY_MEAL_LIST);
        fetchIntent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        context.startService(fetchIntent);
    }

}
