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

package io.github.ilya_lebedev.worldmeal.ui.list;

import android.content.Context;
import android.net.Uri;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.R;
import io.github.ilya_lebedev.worldmeal.data.database.ClassificationMealEntry;

/**
 * MealListAdapter
 */
public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealListAdapterViewHolder> {

    private final Context mContext;
    private final OnClickHandler mClickHandler;

    private List<? extends ClassificationMealEntry> mMealEntries;

    /**
     * Creates a MealListAdapter.
     *
     * @param context Used to talk to the UI and app resources
     * @param onClickHandler Click handler for this adapter. Called When an item is clicked.
     */
    MealListAdapter(Context context, OnClickHandler onClickHandler) {
        mContext = context;
        mClickHandler = onClickHandler;
    }

    @Override
    public MealListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_classification_meal, parent, false);
        view.setFocusable(true);

        return new MealListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealListAdapterViewHolder holder, int position) {
        ClassificationMealEntry currentEntry = mMealEntries.get(position);

        String thumbnail = currentEntry.getThumbnail();
        String name = currentEntry.getName();
        String thumbnailA11y = mContext.getString(R.string.a11y_meal_thumbnail, name);
        String nameA11y = mContext.getString(R.string.a11y_meal_name, name);

        Picasso.with(mContext).load(Uri.parse(thumbnail)).into(holder.thumbnail);
        holder.thumbnail.setContentDescription(thumbnailA11y);
        holder.mealName.setText(name);
        holder.mealName.setContentDescription(nameA11y);
    }

    @Override
    public int getItemCount() {
        if (mMealEntries == null) {
            return 0;
        }

        return mMealEntries.size();
    }

    void swapMealEntries(final List<? extends ClassificationMealEntry> newMealEntries) {
        if (mMealEntries == null) {
            mMealEntries = newMealEntries;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mMealEntries.size();
                }

                @Override
                public int getNewListSize() {
                    return newMealEntries.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mMealEntries.get(oldItemPosition).getId() ==
                            newMealEntries.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ClassificationMealEntry oldEntry = mMealEntries.get(oldItemPosition);
                    ClassificationMealEntry newEntry = newMealEntries.get(newItemPosition);
                    return oldEntry.getId() == newEntry.getId() &&
                            oldEntry.getName().equals(newEntry.getName());
                }
            });

            mMealEntries = newMealEntries;
            result.dispatchUpdatesTo(this);
        }
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface OnClickHandler {
        void onClick(long mealId);
    }

    /**
     * MealListAdapterViewHolder
     */
    class MealListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView thumbnail;
        final TextView mealName;

        public MealListAdapterViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.iv_meal_thumbnail);
            mealName = itemView.findViewById(R.id.tv_meal_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            long mealId = mMealEntries.get(adapterPosition).getId();
            mClickHandler.onClick(mealId);
        }

    }

}
