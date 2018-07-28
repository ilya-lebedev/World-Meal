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

import java.util.Date;

import io.github.ilya_lebedev.worldmeal.data.database.AreaEntry;
import io.github.ilya_lebedev.worldmeal.data.database.AreaMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.IngredientEntry;
import io.github.ilya_lebedev.worldmeal.data.database.IngredientMealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.MealEntry;
import io.github.ilya_lebedev.worldmeal.data.database.MealOfDayEntry;
import io.github.ilya_lebedev.worldmeal.data.network.response.AreaListResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.AreaMealResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.CategoryListResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.CategoryMealResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.IngredientListResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.IngredientMealResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.MealOfDayResponse;
import io.github.ilya_lebedev.worldmeal.data.network.response.MealResponse;
import io.github.ilya_lebedev.worldmeal.utilities.WorldMealDateUtils;

public class MealDbJsonParser {

    private static final String MDB_MEALS = "meals";

    private static final String MDB_STR_AREA = "strArea";

    private static final String MDB_STR_CATEGORY = "strCategory";

    private static final String MDB_STR_INGREDIENT = "strIngredient";

    private static final String MDB_STR_MEAL = "strMeal";
    private static final String MDB_STR_MEAL_THUMB = "strMealThumb";
    private static final String MDB_ID_MEAL = "idMeal";
    private static final String MDB_STR_INSTRUCTIONS = "strInstructions";
    private static final String MDB_STR_YOUTUBE = "strYoutube";
    private static final String MDB_STR_SOURCE = "strSource";

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
    static IngredientListResponse parseIngredientList(final String ingredientListJsonStr)
            throws JSONException {
        JSONObject ingredientListJson = new JSONObject(ingredientListJsonStr);

        IngredientEntry[] ingredientEntries = ingredientListFromJson(ingredientListJson);

        return new IngredientListResponse(ingredientEntries);
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

    @Nullable
    static MealResponse parseMeal(final String mealJsonStr) throws JSONException {
        JSONObject mealListJson = new JSONObject(mealJsonStr);

        MealEntry mealEntry = mealEntryFromJson(mealListJson);

        return new MealResponse(mealEntry);
    }

    @Nullable
    static MealOfDayResponse parseMealOfDay(final String mealOfDayJsonStr) throws JSONException {
        JSONObject mealOfDayListJson = new JSONObject(mealOfDayJsonStr);

        MealOfDayEntry mealOfDayEntry = mealOfDayEntryFromJson(mealOfDayListJson);

        return new MealOfDayResponse(mealOfDayEntry);
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

    private static IngredientEntry[] ingredientListFromJson(final JSONObject ingredientListJson)
            throws JSONException {
        JSONArray ingredientJsonArray = ingredientListJson.getJSONArray(MDB_MEALS);

        IngredientEntry[] ingredientEntries = new IngredientEntry[ingredientJsonArray.length()];

        for (int i = 0; i < ingredientJsonArray.length(); i++) {
            JSONObject ingredientJson = ingredientJsonArray.getJSONObject(i);

            IngredientEntry ingredient = ingredientEntryFromJson(ingredientJson);

            ingredientEntries[i] = ingredient;
        }

        return ingredientEntries;
    }

    private static IngredientEntry ingredientEntryFromJson(final JSONObject ingredientJson)
            throws JSONException {
        String ingredientName = ingredientJson.getString(MDB_STR_INGREDIENT);

        return new IngredientEntry(ingredientName);
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

    private static MealEntry mealEntryFromJson(final JSONObject mealListJson) throws JSONException {
        JSONArray mealJsonArray = mealListJson.getJSONArray(MDB_MEALS);

        JSONObject mealJson = mealJsonArray.getJSONObject(0);

        long mealId = mealJson.getLong(MDB_ID_MEAL);
        String name = mealJson.getString(MDB_STR_MEAL);
        String category = mealJson.getString(MDB_STR_CATEGORY);
        String area = mealJson.getString(MDB_STR_AREA);
        String thumbnail = mealJson.getString(MDB_STR_MEAL_THUMB);
        String instructions = mealJson.getString(MDB_STR_INSTRUCTIONS);
        String youtubeUrl = mealJson.getString(MDB_STR_YOUTUBE);

        return new MealEntry(mealId, name, category, area, instructions, thumbnail, youtubeUrl);
    }

    private static MealOfDayEntry mealOfDayEntryFromJson(final JSONObject mealOfDayListJson)
            throws JSONException {
        JSONArray mealOfDayJsonArray = mealOfDayListJson.getJSONArray(MDB_MEALS);

        JSONObject mealOfDayJson = mealOfDayJsonArray.getJSONObject(0);

        long id = mealOfDayJson.getLong(MDB_ID_MEAL);
        String name = mealOfDayJson.getString(MDB_STR_MEAL);
        String category = mealOfDayJson.getString(MDB_STR_CATEGORY);
        String area = mealOfDayJson.getString(MDB_STR_AREA);
        String thumbnail = mealOfDayJson.getString(MDB_STR_MEAL_THUMB);
        String source = mealOfDayJson.getString(MDB_STR_SOURCE);
        Date date = WorldMealDateUtils.getNormalizedUtcDateForToday();

        return new MealOfDayEntry(id, name, category, area, thumbnail, source, date);
    }

}
