package ru.devegang.dndmanager.main_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CharacterInfoEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CharacterInfoEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterInfoEdit newInstance(String param1, String param2) {
        CharacterInfoEdit fragment = new CharacterInfoEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_character_info_edit, container, false);

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
                    .getRestCharacterAPI()
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

                if(id != -1) {
                    NetworkService.getInstance()
                            .getRestCharacterAPI()
                            .postCharacter(id,character)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Navigation.findNavController(getView()).navigate(R.id.charactersList);

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(),"Smth wrong", Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    System.out.print("Cant get prefs");
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