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

package io.github.ilya_lebedev.worldmeal.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * AreaListMealDao
 */
@Dao
public interface AreaListMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(AreaListMealEntry... areaMeals);

    @Query("SELECT * FROM area_list_meal WHERE area == :area")
    LiveData<List<AreaListMealEntry>> getAreaListMeal(String area);

    @Query("DELETE FROM area_list_meal WHERE area == :area")
    void deleteAreaListMealByArea(String area);

    @Query("DELETE FROM area_list_meal")
    void deleteAllAreaListMeal();

}
