package ru.devegang.dndmanager.main_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.networking.NetworkService;

public class CharacterCreateDnD1 extends Fragment {

    EditText  cName;
    EditText cAlignment;
    EditText cDescription;
    Spinner spinnerRace;
    Spinner spinnerClass;

    Button buttonNext;
    Button buttonCancel;



    List<String> races;
    List<String> classes;

    ArrayAdapter<String> adapterRace;
    ArrayAdapter<String> adapterClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_character_creatednd1,container,false);

        getActivity().setTitle("Редактирование");

        cName = (EditText) rootView.findViewById(R.id.etDnD5CharacterName);
        cAlignment = (EditText) rootView.findViewById(R.id.etDnD5CharacterAlignment);
        cDescription = (EditText) rootView.findViewById(R.id.etDnD5CharacterDescription);
        spinnerClass = (Spinner) rootView.findViewById(R.id.spinnerDnD5Class);
        spinnerRace = (Spinner) rootView.findViewById(R.id.spinnerDnD5Race);
        buttonCancel = (Button) rootView.findViewById(R.id.buttonDnD5Cancel);
        buttonNext = (Button) rootView.findViewById(R.id.buttonDnD5Next);

        NetworkService.getInstance()
                .getRestDnD5APIv2()
                .getListOfRaces()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if(response.isSuccessful()) {
                            races = response.body();
                            adapterRace = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, response.body()
                            );
                            spinnerRace.setAdapter(adapterRace);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {

                    }
                });

        NetworkService.getInstance().getRestDnD5APIv2()
                .getListOfClasses()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if(response.isSuccessful()) {
                            classes = response.body();
                            adapterClass = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, response.body());
                            spinnerClass.setAdapter(adapterClass);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {

                    }
                });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.charactersList);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Character character = new Character();
                character.setName(cName.getText().toString());
                character.setAlignment(cAlignment.getText().toString());
                character.setHp_max(1);
                character.setHp_cur(1);
                character.setLvl(1);
                String chosenRace = spinnerRace.getSelectedItem().toString();
                String chosenClass = spinnerClass.getSelectedItem().toString();
                character.setRace(chosenRace);
                character.setClassC(chosenClass);
                character.setDescription(cDescription.getText().toString());

//                Toast.makeText(getContext(),"race" + chosenRace + "class" + chosenClass, Toast.LENGTH_SHORT ).show();

                SharedPreferences preferences = getActivity().getSharedPreferences("USER_INF", Context.MODE_PRIVATE);
                long id = preferences.getLong("USER_ID",-1);
                if(id != -1 && checkCharacter(character)) {
                    NetworkService.getInstance()
                            .getRestDnD5APIv2()
                            .createDnDCharacterStep1(id,character)
                            .enqueue(new Callback<Character>() {
                                @Override
                                public void onResponse(Call<Character> call, Response<Character> response) {
                                    if(response.isSuccessful()) {
                                        long character_id = response.body().getId();
                                        NetworkService.getInstance()
                                                .getRestDnD5APIv2()
                                                .createDnDCharacterStep2(character_id,chosenRace,chosenClass)
                                                .enqueue(new Callback<Character>() {
                                                    @Override
                                                    public void onResponse(Call<Character> call, Response<Character> response) {
                                                        if(response.isSuccessful()) {
//                                                            Bundle bundle = new Bundle();
//                                                            bundle.putLong("CharacterID", response.body().getId());
                                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
                                                            sharedPreferences.edit().putLong("CharacterID", response.body().getId());
                                                            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("BASIC_COST",Context.MODE_PRIVATE);
                                                            sharedPreferences2.edit().putInt(String.valueOf(character.getId()),-1).apply();
                                                            Navigation.findNavController(getView()).navigate(R.id.mainActivity2);
                                                        } else {
                                                            Toast.makeText(getContext(),"Ooops, smth went wrong", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Character> call, Throwable t) {
                                                        Toast.makeText(getContext(),"Ooops, smth went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onFailure(Call<Character> call, Throwable t) {

                                }
                            });

                } else {
                    Toast.makeText(getContext(), "smt wrong with character", Toast.LENGTH_SHORT).show();
                }


            }
        });



        return rootView;
    }


    public  boolean checkCharacter(Character character) {
        return !character.getName().isEmpty();
    }
}
