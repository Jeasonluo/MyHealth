package com.jeasonlyx.myhealth.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jeasonlyx.myhealth.R;
import com.jeasonlyx.myhealth.data.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder> {

    public ReminderAdapter() {
        super(DIFF_CALLBACK);
    }

    // Compare if items are difference
    private static DiffUtil.ItemCallback<Reminder> DIFF_CALLBACK = new DiffUtil.ItemCallback<Reminder>() {
        @Override
        public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);
        return new ReminderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {

        Reminder reminder = getItem(position);

        // set content
        holder.time_info.setText(reminder.getTimeString());
        holder.repeat_info.setText(reminder.getRepeatString());
        holder.date_info.setText(reminder.getDateString());
    }

    public Reminder getReminderAt(int position){
        return (Reminder)getItem(position);
    }

    public List<Reminder> getCurrentList(){
        int N = getItemCount();
        List<Reminder> reminderList = new ArrayList<>();

        for(int i = 0; i < N; i++){
            reminderList.add(getItem(i));
        }
        return reminderList;
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder{

        private TextView time_info;
        private TextView date_info;
        private TextView repeat_info;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            time_info = itemView.findViewById(R.id.reminder_time_info);
            repeat_info = itemView.findViewById(R.id.reminder_repeat_info);
            date_info = itemView.findViewById(R.id.reminder_date_info);

            // Listener for item here

        }
    }
}
