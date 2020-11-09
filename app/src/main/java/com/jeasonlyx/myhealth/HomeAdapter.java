package com.jeasonlyx.myhealth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class HomeAdapter extends RecyclerView.Adapter {

    public static final int HOME_TITLE = 1234;
    public static final int HOME_ITEMS = 2345;

    private List<TestItems> items;
    public HomeAdapter(List<TestItems> list) {
        items = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if(viewType == HOME_TITLE){
            holder = new HomeTitleViewHolder(LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.home_item_title, parent,false));
        } else{
            holder = new HomeItemViewHolder(LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.home_item, parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == HOME_TITLE){
            // assign values
            TestItems item = items.get(position);
            ((HomeTitleViewHolder)holder).setUpViewHolder(item);
        }else{
            // assign values for item
            TestItems item = items.get(position);
            ((HomeItemViewHolder)holder).setUpViewHolder(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    class HomeTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        public HomeTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_home_title_content);
        }

        private void setUpViewHolder(TestItems item){
            title.setText(item.title);
        }
    }

    class HomeItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;
        private ImageButton video;


        public HomeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_home_title);
            content = itemView.findViewById(R.id.item_home_content);
            video = itemView.findViewById(R.id.item_home_button);
        }

        private void setUpViewHolder(TestItems item){
            title.setText(item.title);
            content.setText(item.content);
            video.setVisibility(View.VISIBLE);
        }
    }

    public static class TestItems{
        String title;
        String content;
        int type;

        public TestItems(String title, int type) {
            this.title = title;
            this.type = type;
        }

        public TestItems(String title, String content, int type) {
            this.title = title;
            this.content = content;
            this.type = type;
        }
    }

}
