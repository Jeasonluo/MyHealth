package com.jeasonlyx.myhealth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.jeasonlyx.myhealth.HomeAdapter.HOME_ITEMS;
import static com.jeasonlyx.myhealth.HomeAdapter.HOME_TITLE;

public class Fragment_Home extends Fragment {

    private FragmentActivity host_Activity;
    private RecyclerView recyclerView;

    List<HomeAdapter.TestItems> items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        host_Activity = requireActivity();
        items = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            HomeAdapter.TestItems item = new HomeAdapter.TestItems("Disease " + i, HOME_TITLE);
            items.add(item);
        }
        for (int i = 1; i <= 5; i++) {
            HomeAdapter.TestItems item = new HomeAdapter.TestItems("Disease " + i,
                    "This is the test content for disease " + i, HOME_ITEMS);
            items.add(item);
        }
        Toast.makeText(host_Activity, "On Create Called", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.fragment_home_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(host_Activity));
        // Set to true when adapter size not affect recycleView size --> improve performance
        recyclerView.setHasFixedSize(true);

        HomeAdapter adapter = new HomeAdapter(items);
        recyclerView.setAdapter(adapter);

        return view;
    }


}
