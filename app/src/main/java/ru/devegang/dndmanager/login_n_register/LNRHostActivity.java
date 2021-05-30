package ru.devegang.dndmanager.login_n_register;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.lang.ref.WeakReference;

import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.User;
import ru.devegang.dndmanager.networking.AuthStatus;

public class LNRHostActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnr_host);

        navController = Navigation.findNavController(this,R.id.fragment_lnr_host);
        //TODO check this
        SharedPreferences preferences = this.getSharedPreferences("USER_INF", MODE_PRIVATE);
        long id = preferences.getLong("USER_ID", -1);
        if(id!= -1) {
            navController.navigate(R.id.mainActivity);
        }

    }






}
