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

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import io.github.ilya_lebedev.worldmeal.utilities.WorldMealInjectorUtils;

/**
 * WorldMealFirebaseJobService
 */
public class WorldMealFirebaseJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        WorldMealNetworkDataSource networkDataSource =
                WorldMealInjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchMealOfDay();

        jobFinished(job, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

}
