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

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * WorldMealNetworkUtils uses to communicate with the MealDb server.
 */
public class WorldMealNetworkUtils {

    private static final String TAG = WorldMealNetworkUtils.class.getSimpleName();

    private static final String SCANNER_DELIMITER = "\\A";

    private static final String MEAL_DB_BASE_URL = "https://www.themealdb.com/api/json/v1";

    private static final String API_KEY_PATH = "1"; // TODO BuildConfig

    private static final String LIST_PATH = "list.php";
    private static final String FILTER_PATH = "filter.php";

    private static final String AREA_PARAM = "a";
    private static final String CATEGORY_PARAM = "c";
    private static final String INGREDIENT_PARAM = "i";

    private static final String LIST_VALUE = "list";

    /* This is utility class and we don't need to instantiate it */
    private WorldMealNetworkUtils() {}

    static URL getAreaListUrl() {
        Uri areaListUri = Uri.parse(MEAL_DB_BASE_URL).buildUpon()
                .appendPath(API_KEY_PATH)
                .appendPath(LIST_PATH)
                .appendQueryParameter(AREA_PARAM, LIST_VALUE)
                .build();

        return buildUrlFromUri(areaListUri);
    }

    static URL getCategoryListUrl() {
        Uri categoryListUri = Uri.parse(MEAL_DB_BASE_URL).buildUpon()
                .appendPath(API_KEY_PATH)
                .appendPath(LIST_PATH)
                .appendQueryParameter(CATEGORY_PARAM, LIST_VALUE)
                .build();

        return buildUrlFromUri(categoryListUri);
    }

    static URL getAreaMealUrl(String areaName) {
        Uri areaMealUri = Uri.parse(MEAL_DB_BASE_URL).buildUpon()
                .appendPath(API_KEY_PATH)
                .appendPath(FILTER_PATH)
                .appendQueryParameter(AREA_PARAM, areaName)
                .build();

        return buildUrlFromUri(areaMealUri);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter(SCANNER_DELIMITER);

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    private static URL buildUrlFromUri(Uri uri) {

        try {
            URL url = new URL(uri.toString());
            Log.v(TAG, "URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

}
