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
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * MealOfDayEntry
 */
@Entity(tableName = "meal_of_day", indices = {@Index(value = {"date"}, unique = true)})
public class MealOfDayEntry {

    @PrimaryKey
    private long id;
    private String name;
    private String category;
    private String area;
    private String thumbnail;
    private String source;
    private Date date;

    /**
     * MealOfDayEntry
     *
     * @param id Meal id
     * @param name Meal name
     * @param category Meal category
     * @param area Meal area
     * @param thumbnail Meal thumbnail url
     * @param source Meal source url
     * @param date Date
     */
    public MealOfDayEntry(long id, String name, String category, String area,
                          String thumbnail, String source, Date date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.thumbnail = thumbnail;
        this.source = source;
        this.date = date;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
