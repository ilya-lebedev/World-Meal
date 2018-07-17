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

package io.github.ilya_lebedev.worldmeal;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * AppExecutors is the global executor pools for the whole application.
 */
public class AppExecutors {

    private static final int NETWORK_IO_EXECUTOR_NUM_THREADS = 3;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;

    // Executors
    private final Executor mainThread;
    private final Executor diskIO;
    private final Executor networkIO;

    private AppExecutors(Executor mainThread, Executor diskIO, Executor networkIO) {
        this.mainThread = mainThread;
        this.diskIO = diskIO;
        this.networkIO = networkIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(
                        new MainThreadExecutor(),
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(NETWORK_IO_EXECUTOR_NUM_THREADS));
            }
        }
        return sInstance;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    /**
     * MainThreadExecutor
     */
    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
