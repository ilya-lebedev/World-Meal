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

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * WorldMealDatabase
 */
@Database(entities =
        {
                AreaEntry.class,
                AreaMealEntry.class,
                CategoryEntry.class,
                CategoryMealEntry.class,
                IngredientEntry.class,
                IngredientMealEntry.class,
                MealEntry.class,
                MealIngredientEntry.class,
                MealOfDayEntry.class
        },
        version = 1)
@TypeConverters(DateConverter.class)
public abstract class WorldMealDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "world_meal";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile WorldMealDatabase sInstance;

    // DAO
    public abstract WorldMealDao worldMealDao();

    public static WorldMealDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            WorldMealDatabase.class, DATABASE_NAME).build();
                }
            }
        }

        return sInstance;
    }

}
