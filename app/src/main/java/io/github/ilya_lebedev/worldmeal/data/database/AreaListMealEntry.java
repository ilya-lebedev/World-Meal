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
 * AreaListMealEntry
 */
@Entity(tableName = "area_list_meal")
public class AreaListMealEntry {

    @PrimaryKey
    private long id;
    private String name;
    private String thumbnail;
    private String area;

    /**
     * This constructor is used by Room to create AreaListMealEntry and by MealDbJsonParser.
     *
     * @param id List meal id
     * @param name List meal name
     * @param thumbnail List meal thumbnail
     * @param area List meal area
     */
    public AreaListMealEntry(long id, String name, String thumbnail, String area) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.area = area;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
