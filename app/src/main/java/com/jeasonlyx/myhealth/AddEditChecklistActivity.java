package com.jeasonlyx.myhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddEditChecklistActivity extends AppCompatActivity {

    private EditText editText_name;
    //private NumberPicker numberPicker_times;
    private Spinner spinner_times;
    private Spinner spinner_frequency;
    private Spinner spinner_category;
    private EditText editText_notes;

    private TextView reminder_time;
    private TextView reminder_date;
    private TextView reminder_repeat;
    private Button add_reminder;

    private ArrayAdapter<CharSequence> adapter_times;
    private ArrayAdapter<CharSequence> adapter_frequency;
    private ArrayAdapter<CharSequence> adapter_category;

    private CoordinatorLayout coordinatorLayout;
    private ReminderAdapter adapter;


    public static final String EXTRA_ID = "com.jeasonlyx.myhealth.EXTRA_ID";
    public static final String EXTRA_NAME = "com.jeasonlyx.myhealth.EXTRA_NAME";
    public static final String EXTRA_TIMES = "com.jeasonlyx.myhealth.EXTRA_TIMES";
    public static final String EXTRA_FREQUENCY = "com.jeasonlyx.myhealth.EXTRA_FREQUENCY";
    public static final String EXTRA_CATEGORY = "com.jeasonlyx.myhealth.EXTRA_CATEGORY";
    public static final String EXTRA_NOTES = "com.jeasonlyx.myhealth.EXTRA_NOTES";


    private static int hour;
    private static int minute;
    private static int month;
    private static int day;
    private static int year;
    private static int max_reminders;
    private static int repeat_index;

    public static int DIALOG_TIME = 2460;
    public static int DIALOG_DATE = 2020;

    // Test cases
    private List<Reminder> reminders;
    private LiveData<List<Reminder>> live_reminders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_checklist);

        editText_name = findViewById(R.id.editText_name);
        spinner_times = findViewById(R.id.spinner_times);
        spinner_frequency = findViewById(R.id.spinner_frequency);
        spinner_category = findViewById(R.id.spinner_category);
        editText_notes = findViewById(R.id.editText_notes);

        reminder_time = findViewById(R.id.reminder_time);
        reminder_date = findViewById(R.id.reminder_date);
        reminder_repeat = findViewById(R.id.reminder_repeat);
        add_reminder = findViewById(R.id.reminder_add_button);

        // Test Cases
        reminders = new ArrayList<>(Arrays.asList(
                new Reminder(830, 10202020, 1),
                new Reminder(1630, 2082021, 2)));

        add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getItemCount() < max_reminders){
                    // add to list
                    Reminder reminder = new Reminder(Reminder.getTime(hour, minute),
                            Reminder.getEnd_date(month,day,year),
                            spinner_frequency.getSelectedItemPosition());
                    List<Reminder> reminderList = new ArrayList<>(adapter.getCurrentList());
                    reminderList.add(reminder);
                    //reminders.remove(viewHolder.getAdapterPosition());
                    adapter.submitList(reminderList);
                    Toast.makeText(AddEditChecklistActivity.this, String.valueOf(reminderList.size()), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddEditChecklistActivity.this, "Exceed Maximum Reminder", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);

        //String time_temp = hour + ":" + minute;
        reminder_time.setText(Reminder.getTimeString(hour, minute));
        //String date_temp = month + "/" + day + "/" + year;
        reminder_date.setText(Reminder.getDateString(month, day, year));

        /*numberPicker_times = findViewById(R.id.numberPicker_times);
        numberPicker_times.setMinValue(0);
        numberPicker_times.setMaxValue(6);*/

        setUpSpinners();

        // for snake bar
        coordinatorLayout = findViewById(R.id.reminder_coordinator_layout);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_reminder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set to true when adapter size not affect recycleView size --> improve performance
        recyclerView.setHasFixedSize(true);

        adapter = new ReminderAdapter();
        recyclerView.setAdapter(adapter);

        // Test cases
        final List<Reminder> reminders = new ArrayList<>(Arrays.asList(
                new Reminder(830, 10202020, 1),
                new Reminder(1630, 2082021, 2)));

        adapter.submitList(reminders);



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit CheckItem");

            max_reminders = intent.getIntExtra(EXTRA_TIMES, 1);
            repeat_index = adapter_frequency.getPosition(intent.getStringExtra(EXTRA_FREQUENCY));

            editText_name.setText(intent.getStringExtra(EXTRA_NAME));
            //numberPicker_times.setValue(intent.getIntExtra(EXTRA_TIMES, 0));
            spinner_times.setSelection(adapter_times.getPosition(String.valueOf(max_reminders)));
            spinner_frequency.setSelection(repeat_index);
            spinner_category.setSelection(adapter_category.getPosition(intent.getStringExtra(EXTRA_CATEGORY)));
            editText_notes.setText(intent.getStringExtra(EXTRA_NOTES));
            reminder_repeat.setText(Reminder.getRepeatString(repeat_index));
        }else{
            setTitle("Add CheckItem");
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    // remove the item from list and database
                    Reminder reminder = adapter.getReminderAt(viewHolder.getAdapterPosition());
                    List<Reminder> reminderList = new ArrayList<>(adapter.getCurrentList());
                    reminderList.remove(reminder);
                    //reminders.remove(viewHolder.getAdapterPosition());
                    adapter.submitList(reminderList);
                    Toast.makeText(AddEditChecklistActivity.this, "Remove Reminder", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void setUpSpinners(){
        //Integer[] items = new Integer[]{1,2,3,4};
        adapter_times = ArrayAdapter.createFromResource(this,
                R.array.times_lists, android.R.layout.simple_spinner_item);
        adapter_times.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_times.setAdapter(adapter_times);

        AdapterView.OnItemSelectedListener onItemSelectedListener_times = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                max_reminders = Integer.valueOf(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinner_times.setOnItemSelectedListener(onItemSelectedListener_times);


        adapter_frequency = ArrayAdapter.createFromResource(this,
                R.array.frequency_lists, android.R.layout.simple_spinner_item);
        adapter_frequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_frequency.setAdapter(adapter_frequency);

        AdapterView.OnItemSelectedListener onItemSelectedListener_frequency = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                repeat_index = position;
                reminder_repeat.setText(Reminder.getRepeatString(repeat_index));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinner_frequency.setOnItemSelectedListener(onItemSelectedListener_frequency);

        adapter_category = ArrayAdapter.createFromResource(this,
                R.array.category_lists, android.R.layout.simple_spinner_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter_category);

        AdapterView.OnItemSelectedListener onItemSelectedListener_category = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinner_category.setOnItemSelectedListener(onItemSelectedListener_category);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_TIME){
            return new TimePickerDialog(AddEditChecklistActivity.this, onTimeSetListener,
                    hour, minute, true);
        }
        else if(id == DIALOG_DATE){
            return new DatePickerDialog(AddEditChecklistActivity.this, onDateSetListener,
                    year, month, day);
        }
        return null;
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int min) {
            hour = hourOfDay;
            minute = min;
            reminder_time.setText(Reminder.getTimeString(hour, minute));
        }
    };

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int y, int m, int dayOfMonth) {
            year = y;
            month = m + 1; // month start from 0
            day = dayOfMonth;
            reminder_date.setText(Reminder.getDateString(month, day, year));
        }
    };

    public void showTimeDialog(View view){
        showDialog(DIALOG_TIME);
        Toast.makeText(this, "TimePicker", Toast.LENGTH_SHORT).show();
    }

    public void showDateDialog(View view){
        showDialog(DIALOG_DATE);
        Toast.makeText(this, "DatePicker", Toast.LENGTH_SHORT).show();
    }

    public void saveCheckItem(){
        MyHealthViewModel viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(MyHealthViewModel.class);

        String name = editText_name.getText().toString().trim();
        //int times = numberPicker_times.getValue();
        int times = Integer.parseInt(spinner_times.getSelectedItem().toString());
        String frequency = spinner_frequency.getSelectedItem().toString();
        String category = spinner_category.getSelectedItem().toString();
        String notes = editText_notes.getText().toString().trim();

        // get the Intent from MainActivity
        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        // check if name is empty
        if(name.trim().isEmpty()){
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        // check if name is unique (skip update id == -1 means not update)
        if(id == -1 && viewModel.checkNameUniqueness(name) != 0){
            Toast.makeText(this, "Name must be Unique", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data_intent = new Intent();
        data_intent.putExtra(EXTRA_NAME, name);
        data_intent.putExtra(EXTRA_TIMES, times);
        data_intent.putExtra(EXTRA_FREQUENCY, frequency);
        data_intent.putExtra(EXTRA_CATEGORY, category);
        data_intent.putExtra(EXTRA_NOTES, notes);

        // Only if ID is passed, should the intent include ID value
        if(id != -1){
            data_intent.putExtra(EXTRA_ID, id);
        }

        // result for startActivityForResult
        setResult(RESULT_OK, data_intent);
        finish(); // close activity
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_checkItem:
                saveCheckItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}