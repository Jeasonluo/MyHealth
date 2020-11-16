package com.jeasonlyx.myhealth.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jeasonlyx.myhealth.R;
import com.jeasonlyx.myhealth.data.Checklist;

public class ChecklistAdapter extends ListAdapter<Checklist, ChecklistAdapter.ChecklistHolder> {


    // Changed from RecyclerView.Adapter to ListAdapter, and data would be store in ListAdapter
    //private List<Checklist> checklists = new ArrayList<>();
    private OnItemClickListener listener;

    public ChecklistAdapter() {
        super(DIFF_CALLBACK);
    }

    // Need to pass it to super class
    private static final DiffUtil.ItemCallback<Checklist> DIFF_CALLBACK = new DiffUtil.ItemCallback<Checklist>() {
        @Override
        public boolean areItemsTheSame(@NonNull Checklist oldItem, @NonNull Checklist newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Checklist oldItem, @NonNull Checklist newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ChecklistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checklist_item, parent, false);

        return new ChecklistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistHolder holder, int position) {
        //Checklist checklist = checklists.get(position);

        Checklist checklist = getItem(position); // getItem replaces the list
        holder.textView_name.setText(checklist.getName());
        holder.textView_timeFrequency.setText(String.valueOf(checklist.getTimes()) + " times per " + checklist.getFrequency());
        holder.textView_category.setText(String.valueOf(checklist.getCategory()));
        holder.textView_notes.setText(checklist.getNote());
        holder.ratingBar.setNumStars(checklist.getTimes());
        holder.ratingBar.setRating(checklist.getCompleted());
        holder.ratingBar.setStepSize(1.0f);
    }

    /*
    // Not needed after ListAdapter replaced by getItemCount()
    @Override
    public int getItemCount() {
        return checklists.size();
    }*/


    /*
    // Not needed after ListAdapter
    public void setChecklists(List<Checklist> checklists){
        //this.checklists = checklists;
        notifyDataSetChanged(); // this will refresh the whole list

        // Along with other methods would only reflect to changed item with animation
        //notifyItemInserted(int position)
    }
    */

    // For getting Object from position
    public Checklist getChecklistAt(int position){
        return getItem(position);
    }

    class ChecklistHolder extends RecyclerView.ViewHolder{
        private TextView textView_name;
        private TextView textView_timeFrequency;
        private TextView textView_category;
        private TextView textView_notes;
        private RatingBar ratingBar;
        private Button record_button;

        public ChecklistHolder(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.textView_checkItem_name);
            textView_timeFrequency = itemView.findViewById(R.id.textView_checkItem_timeFrequency);
            textView_category = itemView.findViewById(R.id.textView_checkItem_category);
            textView_notes = itemView.findViewById(R.id.textView_checkItem_notes);
            ratingBar = itemView.findViewById(R.id.ratingBar_CheckItem);
            record_button = itemView.findViewById(R.id.recordTask_CheckItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =  getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
            /*
            ratingBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ratingBar.getRating() < ratingBar.getNumStars()) {
                        ratingBar.setRating(2);
                    }
                    listener.onRatingBarClick();
                }
            });*/

            record_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =  getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        final Checklist checklist = getItem(position);
                        Context host_Activity = v.getContext();

                        // dialog of confirming recording
                        AlertDialog alert = new AlertDialog.Builder(host_Activity)
                                .setTitle("Record Confirmation")
                                .setMessage("Do you want to record: \n" + checklist.toString())
                                .setPositiveButton("Record", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int completed = (int)ratingBar.getRating();
                                        int max = (int)ratingBar.getNumStars();
                                        if(completed < max) {
                                            // do foreground adding stars
                                            ratingBar.setRating(completed + 1.0f);
                                        }
                                        // do background database
                                        listener.onRecordClick(checklist);

                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .create();

                        alert.show();
                    }
                }
            });
        }
    }

    // Force using the method inside
    public interface OnItemClickListener{
        void onItemClick(Checklist checklist);
        // Here, can add more method for different use, onClick method for example
        void onRecordClick(Checklist checklist);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
