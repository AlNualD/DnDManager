package ru.devegang.dndmanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    NavController navController;

    BottomNavigationView bottomNavigationView;

    Toolbar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("HeroesTavern");

        navController = Navigation.findNavController(this,R.id.fragment_main_host);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_menu);

      //  topbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }



}