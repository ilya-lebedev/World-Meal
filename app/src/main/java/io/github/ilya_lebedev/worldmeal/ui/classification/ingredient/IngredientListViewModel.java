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

package io.github.ilya_lebedev.worldmeal.ui.classification.ingredient;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.data.WorldMealRepository;
import io.github.ilya_lebedev.worldmeal.data.database.IngredientEntry;

/**
 * IngredientListViewModel
 */
public class IngredientListViewModel extends ViewModel {

    private final LiveData<List<IngredientEntry>> mIngredientList;
    private final WorldMealRepository mRepository;

    public IngredientListViewModel(WorldMealRepository repository) {
        mRepository = repository;
        mIngredientList = mRepository.getIngredientList();
    }

    public LiveData<List<IngredientEntry>> getIngredientList() {
        return mIngredientList;
    }

}
