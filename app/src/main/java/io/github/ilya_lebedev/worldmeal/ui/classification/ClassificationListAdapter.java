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
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.ilya_lebedev.worldmeal.R;
import io.github.ilya_lebedev.worldmeal.data.database.ClassificationEntry;

/**
 * ClassificationListAdapter
 */
public class ClassificationListAdapter extends
        RecyclerView.Adapter<ClassificationListAdapter.ClassificationListAdapterViewHolder> {

    private final Context mContext;
    private final ClassificationListAdapterOnClickHandler mClickHandler;

    private List<? extends ClassificationEntry> mClassificationEntries;

    /**
     * Creates a ClassificationListAdapter.
     *
     * @param context Used to talk to the UI and app resources
     * @param onClickHandler Click handler for this adapter. Called When an item is clicked.
     */
    ClassificationListAdapter(Context context, ClassificationListAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mClickHandler = onClickHandler;
    }

    @Override
    public ClassificationListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_classification, parent, false);
        view.setFocusable(true);

        return new ClassificationListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassificationListAdapterViewHolder holder, int position) {
        ClassificationEntry currentEntry = mClassificationEntries.get(position);

        String entryName = currentEntry.getName();

        holder.entryNameTv.setText(entryName);
    }

    @Override
    public int getItemCount() {
        if (mClassificationEntries == null) {
            return 0;
        }

        return mClassificationEntries.size();
    }

    void swapClassificationEntries(final List<? extends ClassificationEntry> newEntries) {
        if (mClassificationEntries == null) {
            mClassificationEntries = newEntries;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mClassificationEntries.size();
                }

                @Override
                public int getNewListSize() {
                    return newEntries.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mClassificationEntries.get(oldItemPosition).getId() ==
                            newEntries.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ClassificationEntry oldEntry = mClassificationEntries.get(oldItemPosition);
                    ClassificationEntry newEntry = newEntries.get(newItemPosition);
                    return oldEntry.getId() == oldEntry.getId() &&
                            oldEntry.getName().equals(newEntry.getName());
                }
            });

            mClassificationEntries = newEntries;
            result.dispatchUpdatesTo(this);
        }
    }

    /**
     * ClassificationListAdapterOnClickHandler
     * The interface that receives onClick messages.
     */
    public interface ClassificationListAdapterOnClickHandler {
        void onClick(String classificationEntryName);
    }

    class ClassificationListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView entryNameTv;

        public ClassificationListAdapterViewHolder(View itemView) {
            super(itemView);

            entryNameTv = itemView.findViewById(R.id.tv_item_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String entryName = mClassificationEntries.get(adapterPosition).getName();
            mClickHandler.onClick(entryName);
        }

    }

}
