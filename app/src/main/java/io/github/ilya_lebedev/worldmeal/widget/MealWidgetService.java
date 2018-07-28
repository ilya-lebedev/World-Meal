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

package io.github.ilya_lebedev.worldmeal.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * MealWidgetService
 */
public class MealWidgetService extends IntentService {

    public MealWidgetService() {
        super("MealWidgetService");
    }

    public static void startUpdateMealWidgets(Context context) {
        Intent startIntent = new Intent(context, MealWidgetService.class);
        context.startService(startIntent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        updateMealWidgets();
    }

    private void updateMealWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, MealWidgetProvider.class));
        MealWidgetProvider.updateMealWidgets(this, appWidgetManager, appWidgetIds);
    }

}
