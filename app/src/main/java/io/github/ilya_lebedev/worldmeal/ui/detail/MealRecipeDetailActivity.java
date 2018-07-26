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

package io.github.ilya_lebedev.worldmeal.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.github.ilya_lebedev.worldmeal.R;
import io.github.ilya_lebedev.worldmeal.data.database.MealEntry;
import io.github.ilya_lebedev.worldmeal.utilities.WorldMealInjectorUtils;

/**
 * MealRecipeDetailActivity
 */
public class MealRecipeDetailActivity extends AppCompatActivity {

    private static final String EXTRA_MEAL_ID = "meal_id";


    private ProgressBar mLoadingIndicator;
    private View mMainContent;

    private ImageView mThumbnail;
    private TextView mName;
    private TextView mCategory;
    private TextView mArea;
    private TextView mInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_recipe_detail);

        Intent intent = getIntent();
        long mealId = intent.getLongExtra(EXTRA_MEAL_ID, -1);

        mMainContent = findViewById(R.id.sv_main_content);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mThumbnail = findViewById(R.id.iv_thumbnail);
        mName = findViewById(R.id.tv_name);
        mCategory = findViewById(R.id.tv_category);
        mArea = findViewById(R.id.tv_area);
        mInstructions = findViewById(R.id.tv_instructions);

        MealViewModelFactory factory = WorldMealInjectorUtils
                .provideMealViewModelFactory(this, mealId);
        MealViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(MealViewModel.class);
        viewModel.getMeal().observe(this, new Observer<MealEntry>() {
            @Override
            public void onChanged(@Nullable MealEntry mealEntry) {
                if (mealEntry != null) {
                    showDataView();
                    bindMealToUI(mealEntry);
                } else {
                    showLoading();
                }
            }
        });
    }

    private void bindMealToUI(MealEntry mealEntry) {
        Picasso.with(this).load(Uri.parse(mealEntry.getThumbnail())).into(mThumbnail);
        mName.setText(mealEntry.getName());
        mCategory.setText(mealEntry.getCategory());
        mArea.setText(mealEntry.getArea());
        mInstructions.setText(mealEntry.getInstructions());
    }

    /**
     * Make the View for the data visible and hide the loading indicator.
     */
    private void showDataView() {
        // Hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Show the data
        mMainContent.setVisibility(View.VISIBLE);
    }

    /**
     * Make the loading indicator visible and hide the data view.
     */
    private void showLoading() {
        // Hide the data
        mMainContent.setVisibility(View.INVISIBLE);
        // Show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public static Intent getStartIntent(Context context, long mealId) {
        Intent startIntent = new Intent(context, MealRecipeDetailActivity.class);
        startIntent.putExtra(EXTRA_MEAL_ID, mealId);
        return startIntent;
    }

}
