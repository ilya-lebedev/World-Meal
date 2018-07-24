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

package io.github.ilya_lebedev.worldmeal.data.network;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.ilya_lebedev.worldmeal.data.database.AreaEntry;
import io.github.ilya_lebedev.worldmeal.data.database.AreaMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.IngredientMealEntry;
import io.github.ilya_lebedev.worldmeal.data.network.response.AreaListResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.AreaMealResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.CategoryListResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.CategoryMealResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.IngredientMealResponse;

public class MealDbJsonParser {

    private static final String MDB_MEALS = "meals";

    private static final String MDB_STR_AREA = "strArea";

    private static final String MDB_STR_CATEGORY = "strCategory";

    private static final String MDB_STR_MEAL = "strMeal";
    private static final String MDB_STR_MEAL_THUMB = "strMealThumb";
    private static final String MDB_ID_MEAL = "idMeal";

    @Nullable
    static AreaListResponse parseAreaList(final String areaListJsonStr) throws JSONException {
        JSONObject areaListJson = new JSONObject(areaListJsonStr);

        AreaEntry[] areaEntries = areaListFromJson(areaListJson);

        return new AreaListResponse(areaEntries);
    }

    @Nullable
    static CategoryListResponse parseCategoryList(final String categoryListJsonStr) throws JSONException{
        JSONObject categoryListJson = new JSONObject(categoryListJsonStr);

        CategoryEntry[] categoryEntries = categoryListFromJson(categoryListJson);

        return new CategoryListResponse(categoryEntries);
    }

    @Nullable
    static AreaMealResponse parseAreaMealList(final String areaMealJsonStr, String areaName)
            throws JSONException {
        JSONObject areaMealListJson = new JSONObject(areaMealJsonStr);

        AreaMealEntry[] areaMealEntries = areaMealListFromJson(areaMealListJson, areaName);

        return new AreaMealResponse(areaMealEntries);
    }

    @Nullable
    static CategoryMealResponse parseCategoryMealList(final String categoryMealJsonStr,
                                                      String categoryName) throws JSONException {
        JSONObject categoryMealListJson = new JSONObject(categoryMealJsonStr);

        CategoryMealEntry[] categoryMealEntries =
                categoryMealListFromJson(categoryMealListJson, categoryName);

        return new CategoryMealResponse(categoryMealEntries);
    }

    @Nullable
    static IngredientMealResponse parseIngredientMealList
            (final String ingredientMealJsonStr, String ingredientName) throws JSONException {
        JSONObject ingredientMealListJson = new JSONObject(ingredientMealJsonStr);

        IngredientMealEntry[] ingredientMealEntries =
                ingredientMealListFromJson(ingredientMealListJson, ingredientName);

        return new IngredientMealResponse(ingredientMealEntries);
    }

    private static AreaEntry[] areaListFromJson(final JSONObject areaListJson) throws JSONException {
        JSONArray areaJsonArray = areaListJson.getJSONArray(MDB_MEALS);

        AreaEntry[] areaEntries = new AreaEntry[areaJsonArray.length()];

        for (int i = 0; i < areaJsonArray.length(); i++) {
            JSONObject areaJson = areaJsonArray.getJSONObject(i);

            AreaEntry area = areaEntryFromJson(areaJson);

            areaEntries[i] = area;
        }

        return areaEntries;
    }

    private static AreaEntry areaEntryFromJson(final JSONObject areaJson) throws JSONException {
        String areaName = areaJson.getString(MDB_STR_AREA);

        return new AreaEntry(areaName);
    }

    private static CategoryEntry[] categoryListFromJson(final JSONObject categoryListJson) throws JSONException {
        JSONArray categoryJsonArray = categoryListJson.getJSONArray(MDB_MEALS);

        CategoryEntry[] categoryEntries = new CategoryEntry[categoryJsonArray.length()];

        for (int i = 0; i < categoryJsonArray.length(); i++) {
            JSONObject categoryJson = categoryJsonArray.getJSONObject(i);

            CategoryEntry category = categoryEntryFromJson(categoryJson);

            categoryEntries[i] = category;
        }

        return categoryEntries;
    }

    private static CategoryEntry categoryEntryFromJson(final JSONObject categoryJson) throws JSONException {
        String categoryName = categoryJson.getString(MDB_STR_CATEGORY);

        return new CategoryEntry(categoryName);
    }

    private static AreaMealEntry[] areaMealListFromJson(final JSONObject areaMealListJson, String areaName)
            throws JSONException {
        JSONArray areaMealJsonArray = areaMealListJson.getJSONArray(MDB_MEALS);

        AreaMealEntry[] areaMealEntries = new AreaMealEntry[areaMealJsonArray.length()];

        for (int i = 0; i < areaMealJsonArray.length(); i++) {
            JSONObject areaMealJson = areaMealJsonArray.getJSONObject(i);

            AreaMealEntry areaMeal = areaMealEntryFromJson(areaMealJson, areaName);

            areaMealEntries[i] = areaMeal;
        }

        return areaMealEntries;
    }

    private static AreaMealEntry areaMealEntryFromJson(final JSONObject areaMealJson, String areaName)
            throws JSONException {
        String mealName = areaMealJson.getString(MDB_STR_MEAL);
        String thumbnail = areaMealJson.getString(MDB_STR_MEAL_THUMB);
        long mealId = areaMealJson.getLong(MDB_ID_MEAL);

        return new AreaMealEntry(mealId, mealName, thumbnail, areaName);
    }

    private static CategoryMealEntry[] categoryMealListFromJson(final JSONObject categoryMealListJson,
                                                                String categoryName) throws JSONException {
        JSONArray categoryMealJsonArray = categoryMealListJson.getJSONArray(MDB_MEALS);

        CategoryMealEntry[] categoryMealEntries = new CategoryMealEntry[categoryMealJsonArray.length()];

        for (int i = 0; i < categoryMealJsonArray.length(); i++) {
            JSONObject categoryMealJson = categoryMealJsonArray.getJSONObject(i);

            CategoryMealEntry categoryMeal = categoryMealEntryFromJson(categoryMealJson, categoryName);

            categoryMealEntries[i] = categoryMeal;
        }

        return categoryMealEntries;
    }

    private static CategoryMealEntry categoryMealEntryFromJson(final JSONObject categoryMealJson,
                                                               String categoryName) throws JSONException {
        String mealName = categoryMealJson.getString(MDB_STR_MEAL);
        String thumbnail = categoryMealJson.getString(MDB_STR_MEAL_THUMB);
        long mealId = categoryMealJson.getLong(MDB_ID_MEAL);

        return new CategoryMealEntry(mealId, mealName, thumbnail, categoryName);
    }

    private static IngredientMealEntry[] ingredientMealListFromJson
            (final JSONObject ingredientMealListJson, String ingredientName) throws JSONException {
        JSONArray ingredientMealJsonArray = ingredientMealListJson.getJSONArray(MDB_MEALS);

        IngredientMealEntry[] ingredientMealEntries =
                new IngredientMealEntry[ingredientMealJsonArray.length()];

        for (int i = 0; i < ingredientMealJsonArray.length(); i++) {
            JSONObject ingredientMealJson = ingredientMealJsonArray.getJSONObject(i);

            IngredientMealEntry ingredientMeal =
                    ingredientMealEntryFromJson(ingredientMealJson, ingredientName);

            ingredientMealEntries[i] = ingredientMeal;
        }

        return ingredientMealEntries;
    }

    private static IngredientMealEntry ingredientMealEntryFromJson
            (final JSONObject ingredientMealJson, String ingredientName) throws JSONException {
        String mealName = ingredientMealJson.getString(MDB_STR_MEAL);
        String thumbnail = ingredientMealJson.getString(MDB_STR_MEAL_THUMB);
        long mealId = ingredientMealJson.getLong(MDB_ID_MEAL);

        return new IngredientMealEntry(mealId, mealName, thumbnail, ingredientName);
    }

}
