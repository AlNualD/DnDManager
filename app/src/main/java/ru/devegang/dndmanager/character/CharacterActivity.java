package ru.devegang.dndmanager.character;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.ChooseTypeOfCreationDialog;
import ru.devegang.dndmanager.networking.NetworkService;

public class CharacterActivity extends AppCompatActivity {


    NavController navController;
    long id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_character_host);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navController = Navigation.findNavController(this,R.id.fragment_character_host);


        id = getIntent().getExtras().getLong("CharacterID", -1);
        if(id == -1) {
            FragmentManager manager = getSupportFragmentManager();
            ChooseTypeOfCreationDialog dialog = new ChooseTypeOfCreationDialog();
            dialog.show(manager,"ccDialog");
        } else {
            SharedPreferences preferences = getSharedPreferences("CHARACTER", MODE_PRIVATE);
            preferences.edit().putLong("CharacterID", id).apply();

            navController.navigate(R.id.characterInfoFull);
        }





    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.character_info_menu,menu);
//        return true;
//    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        SharedPreferences preferences = getSharedPreferences("CHARACTER", MODE_PRIVATE);
        long id = preferences.getLong("CharacterID", -1);
        switch (itemID) {
            case android.R.id.home:
                //Navigation.findNavController(this, R.id.fragment_main_host).navigate(R.id.charactersList);
                if(navController.getCurrentDestination().getId() == R.id.characterInfoFull) {
                    navController.navigate(R.id.mainActivity2);
                } else {
                    navController.popBackStack();
                }
                break;

            case R.id.actionEdit:

                if(id != -1) {
                Bundle bundle = new Bundle();
                bundle.putLong("CharacterID", id);
                navController.navigate(R.id.characterInfoEdit,bundle);
                 }

                break;
            case R.id.actionDelete:
                if (id != -1) {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .deleteCharacter(id)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Deleted", Toast.LENGTH_SHORT).show();
                                        navController.navigate(R.id.mainActivity2);
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Not deleted", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(),"Not deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void useTemplateClicked() {
        navController.navigate(R.id.characterCreateDnD1);
    }

    public void simpleCreationClicked() {
        Bundle bundle = new Bundle();
        bundle.putLong("CharacterID",id);
        navController.navigate(R.id.characterInfoEdit,bundle);
    }


    public void onInventoryClick() {

    }



}
