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

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.MobileAds;

import io.github.ilya_lebedev.worldmeal.R;

/**
 * ClassificationActivity
 */
public class ClassificationActivity extends AppCompatActivity {

    private ClassificationPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPagerAdapter = new ClassificationPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
    }

}
