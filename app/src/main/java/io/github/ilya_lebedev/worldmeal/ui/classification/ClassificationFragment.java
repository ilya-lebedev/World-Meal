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

package io.github.ilya_lebedev.worldmeal.ui.classification;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.R;
import io.github.ilya_lebedev.worldmeal.data.database.AreaEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryEntry;
import io.github.ilya_lebedev.worldmeal.data.database.IngredientEntry;
import io.github.ilya_lebedev.worldmeal.ui.classification.area.AreaListViewModel;
import io.github.ilya_lebedev.worldmeal.ui.classification.area.AreaViewModelFactory;
import io.github.ilya_lebedev.worldmeal.ui.classification.category.CategoryListViewModel;
import io.github.ilya_lebedev.worldmeal.ui.classification.category.CategoryViewModelFactory;
import io.github.ilya_lebedev.worldmeal.ui.classification.ingredient.IngredientListViewModel;
import io.github.ilya_lebedev.worldmeal.ui.classification.ingredient.IngredientViewModelFactory;
import io.github.ilya_lebedev.worldmeal.ui.list.MealListActivity;
import io.github.ilya_lebedev.worldmeal.utilities.WorldMealInjectorUtils;

/**
 * ClassificationFragment
 */
public class ClassificationFragment extends Fragment
        implements ClassificationListAdapter.ClassificationListAdapterOnClickHandler {

    private static final String ARG_CLASSIFICATION_TYPE = "classification_type";
    public static final int CLASSIFICATION_TYPE_AREA = 1;
    public static final int CLASSIFICATION_TYPE_CATEGORY = 2;
    public static final int CLASSIFICATION_TYPE_INGREDIENT = 3;

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private ClassificationListAdapter mListAdapter;

    private int mClassificationType;

    private FirebaseAnalytics mFirebaseAnalytics;

    public ClassificationFragment() {
        // Required empty public constructor
    }

    /**
     * Used to create a new instance of this fragment.
     *
     * @param classificationType area, category or ingredient
     * @return A new instance of fragment ClassificationFragment.
     */
    public static ClassificationFragment newInstance(int classificationType) {
        ClassificationFragment fragment = new ClassificationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLASSIFICATION_TYPE, classificationType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClassificationType = getArguments().getInt(ARG_CLASSIFICATION_TYPE);
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    private void initializeData() {
        switch (mClassificationType) {

            case CLASSIFICATION_TYPE_AREA: {
                AreaViewModelFactory factory = WorldMealInjectorUtils
                        .provideAreaViewModelFactory(getContext().getApplicationContext());
                AreaListViewModel viewModel =
                        ViewModelProviders.of(this, factory).get(AreaListViewModel.class);
                viewModel.getAreaList().observe(this, new Observer<List<AreaEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<AreaEntry> areaEntries) {
                        mListAdapter.swapClassificationEntries(areaEntries);
                        if (areaEntries != null && areaEntries.size() != 0) {
                            showDataView();
                        } else {
                            showLoading();
                        }
                    }
                });

                break;
            }

            case CLASSIFICATION_TYPE_CATEGORY: {
                CategoryViewModelFactory factory = WorldMealInjectorUtils
                        .provideCategoryViewModelFactory(getContext().getApplicationContext());
                CategoryListViewModel viewModel =
                        ViewModelProviders.of(this, factory).get(CategoryListViewModel.class);
                viewModel.getCategoryList().observe(this, new Observer<List<CategoryEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                        mListAdapter.swapClassificationEntries(categoryEntries);
                        if (categoryEntries != null && categoryEntries.size() != 0) {
                            showDataView();
                        } else {
                            showLoading();
                        }
                    }
                });

                break;
            }

            case CLASSIFICATION_TYPE_INGREDIENT: {
                IngredientViewModelFactory factory = WorldMealInjectorUtils
                        .provideIngredientViewModelFactory(getContext().getApplicationContext());
                IngredientListViewModel viewModel = ViewModelProviders.of(this, factory)
                        .get(IngredientListViewModel.class);
                viewModel.getIngredientList().observe(this, new Observer<List<IngredientEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<IngredientEntry> ingredientEntries) {
                        mListAdapter.swapClassificationEntries(ingredientEntries);
                        if (ingredientEntries != null && ingredientEntries.size() != 0) {
                            showDataView();
                        } else {
                            showLoading();
                        }
                    }
                });

                break;
            }

            default:
                throw new IllegalArgumentException("Unsupported classification type "
                        + mClassificationType);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.classification_list, container, false);

        AdView mAdView = view.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mRecyclerView = view.findViewById(R.id.rv_classification);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);

        mListAdapter = new ClassificationListAdapter(getContext(), this);

        final GridLayoutManager layoutManager = new GridLayoutManager(
                getContext(),
                getResources().getInteger(R.integer.classification_grid_span_count));

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mListAdapter);
        showLoading();

        initializeData();

        return view;
    }

    @Override
    public void onClick(String classificationEntryName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, classificationEntryName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "list_item");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, classificationEntryName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        startMealListActivity(classificationEntryName);
    }

    private void startMealListActivity(String classificationEntryName) {
        int classificationType;
        switch (mClassificationType) {
            case CLASSIFICATION_TYPE_AREA:
                classificationType = MealListActivity.CLASSIFICATION_TYPE_AREA;
                break;
            case CLASSIFICATION_TYPE_CATEGORY:
                classificationType = MealListActivity.CLASSIFICATION_TYPE_CATEGORY;
                break;
            case CLASSIFICATION_TYPE_INGREDIENT:
                classificationType = MealListActivity.CLASSIFICATION_TYPE_INGREDIENT;
                break;
            default:
                throw new IllegalArgumentException("Unsupported classification type "
                        + mClassificationType);
        }

        Intent startIntent = MealListActivity.generateStartIntent(getContext(),
                classificationType, classificationEntryName);
        startActivity(startIntent);
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

}
