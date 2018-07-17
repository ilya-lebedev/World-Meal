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
import android.arch.persistence.room.PrimaryKey;

/**
 * CategoryListMealEntry
 */
@Entity(tableName = "category_list_meal")
public class CategoryListMealEntry {

    @PrimaryKey
    private long id;
    private String name;
    private String thumbnail;
    private String category;

    /**
     * This constructor is used by Room to create AreaMealEntry and by MealDbJsonParser.
     *
     * @param id List meal id
     * @param name List meal name
     * @param thumbnail List meal thumbnail
     * @param category List meal category
     */
    public CategoryListMealEntry(long id, String name, String thumbnail, String category) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
