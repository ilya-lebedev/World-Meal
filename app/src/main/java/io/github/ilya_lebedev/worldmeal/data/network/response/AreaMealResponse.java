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

package io.github.ilya_lebedev.worldmeal.data.network.response;

import android.support.annotation.NonNull;

import io.github.ilya_lebedev.worldmeal.data.database.AreaMealEntry;

/**
 * Response from the backend. Contains the area meal list.
 */
public class AreaMealResponse {

    @NonNull
    private final AreaMealEntry[] mAreaMeal;

    public AreaMealResponse(@NonNull final AreaMealEntry[] areaMeal) {
        mAreaMeal = areaMeal;
    }

    public AreaMealEntry[] getAreaMeal() {
        return mAreaMeal;
    }

}
