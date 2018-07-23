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

package io.github.ilya_lebedev.worldmeal.ui.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.R;
import io.github.ilya_lebedev.worldmeal.data.database.AreaMealEntry;
import io.github.ilya_lebedev.worldmeal.ui.list.area.AreaMealViewModel;
import io.github.ilya_lebedev.worldmeal.ui.list.area.AreaMealViewModelFactory;
import io.github.ilya_lebedev.worldmeal.utilities.WorldMealInjectorUtils;

public class MealListActivity extends AppCompatActivity implements MealListAdapter.OnClickHandler {

    private static final String EXTRA_CLASSIFICATION_TYPE = "classification_type";
    private static final String EXTRA_CLASSIFICATION_ENTRY_NAME = "classification_entry_name";

    public static final int CLASSIFICATION_TYPE_AREA = 1;
    public static final int CLASSIFICATION_TYPE_CATEGORY = 2;
    public static final int CLASSIFICATION_TYPE_INGREDIENT = 3;

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private MealListAdapter mMealListAdapter;

    private int mClassificationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

        mClassificationType = getIntent()
                .getIntExtra(EXTRA_CLASSIFICATION_TYPE, -1);
        String classificationEntryName = getIntent()
                .getStringExtra(EXTRA_CLASSIFICATION_ENTRY_NAME);

        mRecyclerView = findViewById(R.id.rv_meals);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMealListAdapter = new MealListAdapter(this, this);

        final GridLayoutManager layoutManager = new GridLayoutManager(
                this,
                getResources().getInteger(R.integer.meal_grid_span_count));

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mMealListAdapter);
        showLoading();

        initializeData(classificationEntryName);
    }

    @Override
    public void onClick(long mealId) {
        // TODO
    }

    private void initializeData(String classificationEntryName) {
        switch (mClassificationType) {

            case CLASSIFICATION_TYPE_AREA: {
                AreaMealViewModelFactory factory = WorldMealInjectorUtils
                        .provideAreaMealViewModelFactory(this, classificationEntryName);
                AreaMealViewModel viewModel = ViewModelProviders.of(this, factory)
                        .get(AreaMealViewModel.class);
                viewModel.getAreaMeal().observe(this, new Observer<List<AreaMealEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<AreaMealEntry> areaMealEntries) {
                        mMealListAdapter.swapMealEntries(areaMealEntries);
                        if (areaMealEntries != null && areaMealEntries.size() != 0) {
                            showDataView();
                        } else {
                            showLoading();
                        }
                    }
                });

                break;
            }

            case CLASSIFICATION_TYPE_CATEGORY: {
                // TODO

                break;
            }

            case CLASSIFICATION_TYPE_INGREDIENT: {
                // TODO

                break;
            }

            default:
                throw new IllegalArgumentException("Unsupported classification type "
                        + mClassificationType);
        }
    }

    /**
     * Make the View for the data visible and hide the loading indicator.
     */
    private void showDataView() {
        // Hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Show the data
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Make the loading indicator visible and hide the data view.
     */
    private void showLoading() {
        // Hide the data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public static Intent generateStartIntent(Context packageContext, int classificationType,
                                             String classificationEntryName) {
        Intent startIntent = new Intent(packageContext, MealListActivity.class);
        startIntent.putExtra(EXTRA_CLASSIFICATION_TYPE, classificationType);
        startIntent.putExtra(EXTRA_CLASSIFICATION_ENTRY_NAME, classificationEntryName);
        return startIntent;
    }

}
