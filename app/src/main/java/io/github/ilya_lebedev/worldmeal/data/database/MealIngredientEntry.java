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

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * MealIngredientEntry
 */
@Entity(tableName = "meal_ingredient")
public class MealIngredientEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int indexNumber;
    private String name;
    private String measure;
    private long mealId;

    /**
     * This constructor is used by Room to create MealIngredientEntries
     *
     * @param id Meal ingredient id
     * @param indexNumber Meal ingredient index number
     * @param name Meal ingredient name
     * @param measure Meal ingredient measure
     * @param mealId Meal ingredient meal id
     */
    public MealIngredientEntry(long id, int indexNumber, String name, String measure, long mealId) {
        this.id = id;
        this.indexNumber = indexNumber;
        this.name = name;
        this.measure = measure;
        this.mealId = mealId;
    }

    /**
     * This constructor is used by MealDbJsonParser.
     *
     * @param indexNumber Meal ingredient index number
     * @param name Meal ingredient name
     * @param measure Meal ingredient measure
     * @param mealId Meal ingredient meal id
     */
    public MealIngredientEntry(int indexNumber, String name, String measure, long mealId) {
        this.indexNumber = indexNumber;
        this.name = name;
        this.measure = measure;
        this.mealId = mealId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
    }

}
