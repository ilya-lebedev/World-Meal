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

package io.github.ilya_lebedev.worldmeal.ui.list.category;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.data.WorldMealRepository;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryMealEntry;

/**
 * CategoryMealViewModel
 */
public class CategoryMealViewModel extends ViewModel {

    private final WorldMealRepository mRepository;
    private final String mCategoryName;
    private final LiveData<List<CategoryMealEntry>> mCategoryMeal;

    public CategoryMealViewModel(WorldMealRepository repository, String categoryName) {
        mRepository = repository;
        mCategoryName = categoryName;
        mCategoryMeal = mRepository.getCategoryMealList(mCategoryName);
    }

    public final LiveData<List<CategoryMealEntry>> getCategoryMeal() {
        return mCategoryMeal;
    }

}
