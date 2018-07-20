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

package io.github.ilya_lebedev.worldmeal.ui.classification.category;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.data.WorldMealRepository;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryEntry;

/**
 * CategoryListViewModel
 */
public class CategoryListViewModel extends ViewModel {

    private final LiveData<List<CategoryEntry>> mCategoryList;
    private final WorldMealRepository mRepository;

    public CategoryListViewModel(WorldMealRepository repository) {
        mRepository = repository;
        mCategoryList = repository.getCategoryList();
    }

    public LiveData<List<CategoryEntry>> getCategoryList() {
        return mCategoryList;
    }

}
