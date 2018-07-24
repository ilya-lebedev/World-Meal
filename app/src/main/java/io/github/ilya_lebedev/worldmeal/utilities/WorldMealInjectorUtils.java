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

package io.github.ilya_lebedev.worldmeal.utilities;

import android.content.Context;

import io.github.ilya_lebedev.worldmeal.AppExecutors;
import io.github.ilya_lebedev.worldmeal.data.WorldMealRepository;
import io.github.ilya_lebedev.worldmeal.data.database.WorldMealDatabase;
import io.github.ilya_lebedev.worldmeal.data.network.WorldMealNetworkDataSource;
import io.github.ilya_lebedev.worldmeal.ui.classification.area.AreaViewModelFactory;
import io.github.ilya_lebedev.worldmeal.ui.classification.category.CategoryViewModelFactory;
import io.github.ilya_lebedev.worldmeal.ui.list.area.AreaMealViewModelFactory;
import io.github.ilya_lebedev.worldmeal.ui.list.category.CategoryMealViewModelFactory;

/**
 * WorldMealInjectorUtils provides static methods to inject
 * the various classes needed for WorldMeal app
 */
public class WorldMealInjectorUtils {

    public static WorldMealRepository provideRepository(Context context) {
        WorldMealDatabase database = WorldMealDatabase.getInstance(context.getApplicationContext());
        AppExecutors appExecutors = AppExecutors.getInstance();
        WorldMealNetworkDataSource networkDataSource =
                WorldMealNetworkDataSource.getInstance(context.getApplicationContext(), appExecutors);

        return WorldMealRepository.getInstance(database.worldMealDao(), networkDataSource, appExecutors);
    }

    public static WorldMealNetworkDataSource provideNetworkDataSource(Context context) {
        // This call to provide repository is necessary if the app starts from a service - in this
        // case the repository will not exist unless it is specifically created.
        provideRepository(context.getApplicationContext());
        AppExecutors appExecutors = AppExecutors.getInstance();

        return WorldMealNetworkDataSource.getInstance(context.getApplicationContext(), appExecutors);
    }

    public static AreaViewModelFactory provideAreaViewModelFactory(Context context) {
        WorldMealRepository repository = provideRepository(context);
        return new AreaViewModelFactory(repository);
    }

    public static CategoryViewModelFactory provideCategoryViewModelFactory(Context context) {
        WorldMealRepository repository = provideRepository(context);
        return new CategoryViewModelFactory(repository);
    }

    public static AreaMealViewModelFactory provideAreaMealViewModelFactory(Context context,
                                                                           String areaName) {
        WorldMealRepository repository = provideRepository(context);
        return new AreaMealViewModelFactory(repository, areaName);
    }

    public static CategoryMealViewModelFactory provideCategoryMealViewModelFactory(Context context,
                                                                               String categoryName) {
        WorldMealRepository repository = provideRepository(context);
        return new CategoryMealViewModelFactory(repository, categoryName);
    }

}
