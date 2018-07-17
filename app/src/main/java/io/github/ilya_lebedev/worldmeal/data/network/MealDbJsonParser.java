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
import io.github.ilya_lebedev.worldmeal.data.network.response.AreaListResponse;

public class MealDbJsonParser {

    private static final String MDB_MEALS = "meals";

    private static final String MDB_STR_AREA = "strArea";

    @Nullable
    static AreaListResponse parseAreaList(final String areaListJsonStr) throws JSONException {
        JSONObject areaListJson = new JSONObject(areaListJsonStr);

        AreaEntry[] areaEntries = areaListFromJson(areaListJson);

        return new AreaListResponse(areaEntries);
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

}
