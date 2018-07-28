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

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import io.github.ilya_lebedev.worldmeal.R;
import io.github.ilya_lebedev.worldmeal.data.WorldMealRepository;
import io.github.ilya_lebedev.worldmeal.data.database.MealOfDayEntry;
import io.github.ilya_lebedev.worldmeal.utilities.WorldMealInjectorUtils;

/**
 * MealWidgetProvider
 */
public class MealWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MealWidgetService.startUpdateMealWidgets(context);
    }

    public static void updateMealWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = getMealRemoteView(context);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getMealRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.meal_widget);

        WorldMealRepository repository = WorldMealInjectorUtils.provideRepository(context);
        MealOfDayEntry mealOfDayEntry = repository.getMealOfDay();

        views.setTextViewText(R.id.tv_name, mealOfDayEntry.getName());
        views.setTextViewText(R.id.tv_area, mealOfDayEntry.getArea());
        views.setTextViewText(R.id.tv_category, mealOfDayEntry.getCategory());

        return views;
    }

}
