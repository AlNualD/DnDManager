package ru.devegang.dndmanager.character;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.networking.NetworkService;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterInfoEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterInfoEdit extends Fragment {

    private EditText characterName;
    private EditText characterRace;
    private EditText characterClass;
    private EditText characterAlignment;
    private EditText characterLvl;
    private EditText characterHealth;
    private EditText characterMoney;

    private Button saveButton;

    private SharedPreferences preferences;

    private Character character;



    public CharacterInfoEdit() {
        // Required empty public constructor
    }


    public static CharacterInfoEdit newInstance() {
        CharacterInfoEdit fragment = new CharacterInfoEdit();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_character_info_edit, container, false);
        getActivity().setTitle("Редактирование");

        characterName = (EditText) rootView.findViewById(R.id.etEditCharacterName);
        characterClass = (EditText) rootView.findViewById(R.id.etEditCharacterClass);
        characterRace = (EditText) rootView.findViewById(R.id.etEditCharacterRace);
        characterAlignment = (EditText) rootView.findViewById(R.id.etEditCharacterAlignment);
        characterHealth = (EditText) rootView.findViewById(R.id.etEditCharacterHealth);
        characterLvl = (EditText) rootView.findViewById(R.id.etEditCharacterLvl);
        characterMoney = (EditText) rootView.findViewById(R.id.etEditCharacterMoney);

        character = new Character();

        long characterId= getArguments().getLong("CharacterID");

        if(characterId != -1) {
            //edit
            character.setId(characterId);
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .getCharacter(characterId)
                    .enqueue(new Callback<Character>() {
                        @Override
                        public void onResponse(Call<Character> call, Response<Character> response) {
                            setCharacterInf(response.body());
                        }

                        @Override
                        public void onFailure(Call<Character> call, Throwable t) {

                        }
                    });


        }

        saveButton = (Button) rootView.findViewById(R.id.buttonEditCharacterSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                character.setName(characterName.getText().toString());
                character.setClassC(characterClass.getText().toString());
                character.setRace(characterRace.getText().toString());
                character.setLvl(Integer.parseInt(characterLvl.getText().toString()));
                character.setHp_max(Integer.parseInt(characterHealth.getText().toString()));
                character.setHp_cur(character.getHp_max());
                character.setMoney(Double.parseDouble(characterMoney.getText().toString()));
                character.setAlignment(characterAlignment.getText().toString());

                preferences = getActivity().getSharedPreferences("USER_INF", MODE_PRIVATE);
                long id = preferences.getLong("USER_ID",-1);

                if(characterId==-1){

                    if(id != -1) {
                        NetworkService.getInstance()
                                .getRestCharacterAPIv2()
                                .createCharacter(id, character)
                                .enqueue(new Callback<Character>() {
                                    @Override
                                    public void onResponse(Call<Character> call, Response<Character> response) {
                                        if(response.isSuccessful()) {
                                            SharedPreferences preferencesCharacter = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
                                            preferencesCharacter.edit().putLong("CharacterID", response.body().getId());
                                            Navigation.findNavController(getView()).navigate(R.id.characterInfoFull);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Character> call, Throwable t) {

                                    }
                                });
                    }
                } else {

                        NetworkService.getInstance()
                                .getRestCharacterAPIv2()
                                .updateCharacter(characterId, character)
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Navigation.findNavController(getView()).navigate(R.id.mainActivity2);

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getContext(), "Smth wrong", Toast.LENGTH_SHORT).show();

                                    }
                                });

                }


            }
        });


        return rootView;
    }

    void setCharacterInf(Character character) {
        characterName.setText(character.getName());
        characterRace.setText(character.getRace());
        characterClass.setText(character.getClassC());
        characterLvl.setText(String.valueOf(character.getLvl()));
        characterMoney.setText(String.valueOf(character.getMoney()));
        characterHealth.setText(String.valueOf(character.getHp_max()));
        characterAlignment.setText(character.getAlignment());

    }
}