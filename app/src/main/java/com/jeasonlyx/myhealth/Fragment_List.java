package com.jeasonlyx.myhealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jeasonlyx.myhealth.adapters.ChecklistAdapter;
import com.jeasonlyx.myhealth.data.Checklist;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_List extends Fragment {

    private MyHealthViewModel viewModel;

    public static final int ADD_CHECKITEM_REQUEST = 166;
    public static final int Edit_CHECKITEM_REQUEST = 168;

    private String current_category = "Default";

    private ChecklistAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private FragmentActivity host_Activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        host_Activity = requireActivity();
        setHasOptionsMenu(true);

        coordinatorLayout = view.findViewById(R.id.main_activity_coordinatorLayout);
        RecyclerView recyclerView = view.findViewById(R.id.recycleView_checklists);
        recyclerView.setLayoutManager(new LinearLayoutManager(host_Activity));
        // Set to true when adapter size not affect recycleView size --> improve performance
        recyclerView.setHasFixedSize(true);

        adapter = new ChecklistAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton floating_add = view.findViewById(R.id.floating_checkItem_add);
        floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(host_Activity, AddEditChecklistActivity.class);
                startActivityForResult(intent, ADD_CHECKITEM_REQUEST); // Set with Unique request Code
            }
        });

        viewModel = new ViewModelProvider(host_Activity,
                new ViewModelProvider.AndroidViewModelFactory(host_Activity.getApplication()))
                .get(MyHealthViewModel.class);
        viewModel.getAllChecklist().observe(this, new Observer<List<Checklist>>() {
            @Override
            public void onChanged(List<Checklist> checklists) {
                //adapter.setChecklists(checklists);

                // ListAdapter method
                if(current_category.equals("Default"))
                {
                    adapter.submitList(checklists);
                    Toast.makeText(host_Activity,
                            "Checklist Refreshed", Toast.LENGTH_SHORT).show();
                }
                else{
                    adapter.submitList(viewModel.getCategorizedChecklist(current_category));
                    Toast.makeText(host_Activity,
                            current_category +" Refreshed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT/*|ItemTouchHelper.*/) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Checklist item = adapter.getChecklistAt(position);

                String message = "You are going to delete:\n"
                        + "Name: " +  item.getName() + "\n"
                        + "Category: " + item.getCategory() + "\n"
                        + "Repeat: " + item.getTimes() + " times per " + item.getFrequency() + "\n"
                        + "Note: " + item.getNote() + "\n";

                viewModel.delete(item);

                AlertDialog.Builder alert = new AlertDialog.Builder(host_Activity);

                alert.setTitle("Task Deletion")
                        .setMessage(message)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showSnackBar(item);
                                Toast.makeText(host_Activity, "Item Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                viewModel.insert(item);
                                refreshChecklist();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ChecklistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Checklist checklist) {
                Intent intent = new Intent(host_Activity, AddEditChecklistActivity.class);

                intent.putExtra(AddEditChecklistActivity.EXTRA_ID, checklist.getId());
                intent.putExtra(AddEditChecklistActivity.EXTRA_NAME, checklist.getName());
                intent.putExtra(AddEditChecklistActivity.EXTRA_TIMES, checklist.getTimes());
                intent.putExtra(AddEditChecklistActivity.EXTRA_FREQUENCY, checklist.getFrequency());
                intent.putExtra(AddEditChecklistActivity.EXTRA_CATEGORY, checklist.getCategory());
                intent.putExtra(AddEditChecklistActivity.EXTRA_NOTES, checklist.getNote());
                startActivityForResult(intent, Edit_CHECKITEM_REQUEST);
            }

            @Override
            public void onRatingBarClick() {
                Toast.makeText(host_Activity, "RatingBar Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }



    // For handling StartActivityForResult method
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_CHECKITEM_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(AddEditChecklistActivity.EXTRA_NAME);
            int times = data.getIntExtra(AddEditChecklistActivity.EXTRA_TIMES, 1);
            int completed = data.getIntExtra(AddEditChecklistActivity.EXTRA_COMPLETED, 0);
            String frequency = data.getStringExtra(AddEditChecklistActivity.EXTRA_FREQUENCY);
            String category = data.getStringExtra(AddEditChecklistActivity.EXTRA_CATEGORY);
            String notes = data.getStringExtra(AddEditChecklistActivity.EXTRA_NOTES);

            Checklist checklist = new Checklist(name, times, completed, frequency, category, notes);
            viewModel.insert(checklist);

            Toast.makeText(host_Activity, "CheckItem Added", Toast.LENGTH_SHORT).show();
            return;
        }else if(requestCode == Edit_CHECKITEM_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditChecklistActivity.EXTRA_ID, -1);

            if(id == -1){
                Toast.makeText(host_Activity, "Item Not Able to Update", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(AddEditChecklistActivity.EXTRA_NAME);
            int times = data.getIntExtra(AddEditChecklistActivity.EXTRA_TIMES, 1);
            int completed = data.getIntExtra(AddEditChecklistActivity.EXTRA_COMPLETED, 0);
            String frequency = data.getStringExtra(AddEditChecklistActivity.EXTRA_FREQUENCY);
            String category = data.getStringExtra(AddEditChecklistActivity.EXTRA_CATEGORY);
            String notes = data.getStringExtra(AddEditChecklistActivity.EXTRA_NOTES);

            Checklist checklist = new Checklist(name, times, completed, frequency, category, notes);
            checklist.setId(id);  // Vital to make update
            viewModel.update(checklist);

            Toast.makeText(host_Activity, "CheckItem Updated", Toast.LENGTH_SHORT).show();
            return;

        } else{
                Toast.makeText(host_Activity, "Not Saved", Toast.LENGTH_SHORT).show();
        }

    }

    //
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            /*case R.id.delete_all_checklist:
                viewModel.deleteAllChecklist();
                Toast.makeText(host_Activity, "All checklists Deleted", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.category_Default:
                current_category = item.getTitle().toString();
                adapter.submitList(viewModel.getAllChecklist().getValue());
                return true;
            case R.id.category_Medicine:
            case R.id.category_Diet:
            case R.id.category_Exercise:
            case R.id.category_ToBeCompleted:
            case R.id.category_Daily:
            case R.id.category_Weekly:
            case R.id.category_Monthly:
                current_category = item.getTitle().toString();
                adapter.submitList(viewModel.getCategorizedChecklist(current_category));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshChecklist(List<Checklist> checklists){
        // ListAdapter method
        if(current_category.equals("Default"))
        {
            adapter.submitList(checklists);
            Toast.makeText(host_Activity,
                    "Checklist Refreshed", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.submitList(viewModel.getCategorizedChecklist(current_category));
            Toast.makeText(host_Activity,
                    current_category +" Refreshed", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshChecklist(){
        refreshChecklist(viewModel.getAllChecklist().getValue());
    }


    private void showSnackBar(final Checklist item) {
        //Toast.makeText(host_Activity, "Snack Bar Clicked", Toast.LENGTH_SHORT).show();

        Snackbar snackbar = Snackbar.make(coordinatorLayout, "You have Deleted Item", Snackbar.LENGTH_INDEFINITE)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.insert(item);
                        refreshChecklist();
                        Snackbar.make(coordinatorLayout, "Task restored", Snackbar.LENGTH_LONG).show();
                    }
                })
                .setActionTextColor(Color.MAGENTA);  //  Set color for Action text

        // Set Color for the text in SnackBar
        View snackView = snackbar.getView();
        TextView textView = snackView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.GREEN);

        snackbar.show();
    }
}