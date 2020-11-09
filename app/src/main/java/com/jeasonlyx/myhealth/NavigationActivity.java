package com.jeasonlyx.myhealth;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView nav_view = findViewById(R.id.nav_bottom_navigation);
        nav_view.setOnNavigationItemSelectedListener(navListener);


        // Initial it with a fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_fragment_container, new Fragment_Home()).commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch(menuItem.getItemId()){
                case R.id.nav_menu_home:
                    fragment = new Fragment_Home();
                    break;
                case R.id.nav_menu_checklist:
                    fragment = new Fragment_List();
                    break;
                case R.id.nav_menu_search:
                    fragment = new Fragment_Search();
                    break;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_fragment_container, fragment).commit();

            //Toast.makeText(NavigationActivity.this, fragment.toString(), Toast.LENGTH_SHORT).show();
            return true;
        }
    };
}
