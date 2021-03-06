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

package io.github.ilya_lebedev.worldmeal.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * CategoryEntry
 */
@Entity(tableName = "category", indices = {@Index(value = {"name"}, unique = true)})
public class CategoryEntry extends ClassificationEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    /**
     * This constructor is used by Room to create CategoryEntries .
     *
     * @param id Category id
     * @param name Category name
     */
    public CategoryEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * This constructor is used by MealDbJsonParser.
     *
     * @param name Category name
     */
    @Ignore
    public CategoryEntry(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
