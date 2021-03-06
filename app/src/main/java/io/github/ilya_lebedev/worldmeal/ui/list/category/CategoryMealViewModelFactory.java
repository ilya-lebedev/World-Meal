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

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import io.github.ilya_lebedev.worldmeal.data.WorldMealRepository;

/**
 * CategoryMealViewModelFactory
 */
public class CategoryMealViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final WorldMealRepository mRepository;
    private final String mCategoryName;

    public CategoryMealViewModelFactory(WorldMealRepository repository, String categoryName) {
        mRepository = repository;
        mCategoryName = categoryName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CategoryMealViewModel(mRepository, mCategoryName);
    }

}
