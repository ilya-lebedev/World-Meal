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

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.ilya_lebedev.worldmeal.R;

/**
 * ClassificationPagerAdapter
 */
public class ClassificationPagerAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 3;

    private Context mContext;

    public ClassificationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return ClassificationFragment
                        .newInstance(ClassificationFragment.CLASSIFICATION_TYPE_AREA);
            case 0:
                return ClassificationFragment
                        .newInstance(ClassificationFragment.CLASSIFICATION_TYPE_CATEGORY);
            case 2:
                return ClassificationFragment
                        .newInstance(ClassificationFragment.CLASSIFICATION_TYPE_INGREDIENT);
            default:
                throw new IllegalArgumentException("There is no item for position " + position);
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return mContext.getString(R.string.tab_areas);
            case 0:
                return mContext.getString(R.string.tab_categories);
            case 2:
                return mContext.getString(R.string.tab_ingredients);
            default:
                throw new IllegalArgumentException("There is no title for position " + position);
        }
    }

}
