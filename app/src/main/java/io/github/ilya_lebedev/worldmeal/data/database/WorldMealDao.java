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
 * WorldMealDao
 */
@Dao
public interface WorldMealDao {

    /*
     * Area
     */

    /**
     * Inserts a list of {@link AreaEntry} into the area table.
     *
     * @param areas A list of areas to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(AreaEntry... areas);

    @Query("SELECT * FROM area")
    LiveData<List<AreaEntry>> getAreaList();

    @Query("DELETE FROM area")
    void deleteAreaList();

    @Query("SELECT COUNT(id) FROM area")
    int countAllArea();

    /*
     * AreaMeal
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(AreaMealEntry... areaMeals);

    @Query("SELECT * FROM area_meal WHERE area == :area")
    LiveData<List<AreaMealEntry>> getAreaMeal(String area);

    @Query("DELETE FROM area_meal WHERE area == :area")
    void deleteAreaMealByArea(String area);

    @Query("DELETE FROM area_meal")
    void deleteAllAreaMeal();

    @Query("SELECT COUNT(id) FROM area_meal WHERE area == :area")
    int countAllAreaMeal(String area);

    /*
     * Category
     */

    /**
     * Inserts a list of {@link CategoryEntry} into the category table.
     *
     * @param categories A list of categories to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(CategoryEntry... categories);

    @Query("SELECT * FROM category")
    LiveData<List<CategoryEntry>> getCategoryList();

    @Query("DELETE FROM category")
    void deleteCategoryList();

    @Query("SELECT COUNT(id) FROM category")
    int countAllCategory();

    /*
     * CategoryMeal
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(CategoryMealEntry... categoryMeals);

    @Query("SELECT * FROM category_meal WHERE category == :category")
    LiveData<List<CategoryMealEntry>> getCategoryMeal(String category);

    @Query("DELETE FROM category_meal WHERE category == :category")
    void deleteCategoryMealByCategory(String category);

    @Query("DELETE FROM category_meal")
    void deleteAllCategoryMeal();

    /*
     * Ingredient
     */

    /**
     * Inserts a list of {@link IngredientEntry} into the ingredient table.
     *
     * @param ingredients A list of ingredients to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(IngredientEntry... ingredients);

    @Query("SELECT * FROM ingredient")
    LiveData<List<IngredientEntry>> getIngredientList();

    @Query("DELETE FROM ingredient")
    void deleteIngredientList();

    @Query("SELECT COUNT(id) FROM ingredient")
    int countAllIngredient();

    /*
     * IngredientMeal
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(IngredientMealEntry... ingredientMeals);

    @Query("SELECT * FROM ingredient_meal WHERE ingredient == :ingredient")
    LiveData<List<IngredientMealEntry>> getIngredientMeal(String ingredient);

    @Query("DELETE FROM ingredient_meal WHERE ingredient == :ingredient")
    void deleteIngredientMealByIngredient(String ingredient);

    @Query("DELETE FROM ingredient_meal")
    void deleteAllIngredientMeal();

    /*
     * Meal
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MealEntry meal);

    @Query("SELECT * FROM meal WHERE id == :id")
    LiveData<MealEntry> getMeal(long id);

    @Query("DELETE FROM meal WHERE id == :id")
    void deleteMeal(long id);

    @Query("DELETE FROM meal")
    void deleteAllMeals();

    /*
     * MealIngredient
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(MealIngredientEntry... mealIngredients);

    @Query("SELECT * FROM meal_ingredient WHERE mealId == :mealId")
    LiveData<List<MealIngredientEntry>> getMealIngredientList(long mealId);

    @Query("DELETE FROM meal_ingredient WHERE mealId == :mealId")
    void deleteMealIngredientById(long mealId);

    @Query("DELETE FROM meal_ingredient")
    void deleteAllMealIngredient();

}
