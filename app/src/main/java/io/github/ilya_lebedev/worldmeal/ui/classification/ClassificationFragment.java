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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.R;
import io.github.ilya_lebedev.worldmeal.data.database.AreaEntry;
import io.github.ilya_lebedev.worldmeal.data.database.CategoryEntry;
import io.github.ilya_lebedev.worldmeal.ui.classification.area.AreaListViewModel;
import io.github.ilya_lebedev.worldmeal.ui.classification.area.AreaViewModelFactory;
import io.github.ilya_lebedev.worldmeal.ui.classification.category.CategoryListViewModel;
import io.github.ilya_lebedev.worldmeal.ui.classification.category.CategoryViewModelFactory;
import io.github.ilya_lebedev.worldmeal.utilities.WorldMealInjectorUtils;

/**
 * ClassificationFragment
 */
public class ClassificationFragment extends Fragment
        implements ClassificationListAdapter.ClassificationListAdapterOnClickHandler {

    private static final String ARG_CLASSIFICATION_TYPE = "";
    public static final int CLASSIFICATION_TYPE_AREA = 1;
    public static final int CLASSIFICATION_TYPE_CATEGORY = 2;
    public static final int CLASSIFICATION_TYPE_INGREDIENT = 3;

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private ClassificationListAdapter mListAdapter;

    private int mClassificationType;

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
                // TODO

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
        // TODO
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
