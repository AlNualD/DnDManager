package ru.devegang.dndmanager.main_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.networking.NetworkService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterInfo extends Fragment {

    private TextView characterName;
    private TextView characterClass;
    private TextView characterRace;
    private TextView characterLvl;
    private TextView characterMoney;
    private  TextView characterAlignment;

    private ProgressBar characterHealth;



    public CharacterInfo() {
        // Required empty public constructor
    }


    public static CharacterInfo newInstance(String param1, String param2) {
        CharacterInfo fragment = new CharacterInfo();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_character_info, container, false);

        characterName = (TextView)rootView.findViewById(R.id.tvICharacterName);
        characterRace = (TextView)rootView.findViewById(R.id.tvICharacterRace);
        characterClass = (TextView)rootView.findViewById(R.id.tvICharacterClass);
        characterAlignment = (TextView)rootView.findViewById(R.id.tvICharacterAlignment);
        characterLvl = (TextView)rootView.findViewById(R.id.tvILvl);
        characterMoney = (TextView)rootView.findViewById(R.id.tvICharacterMoney);
        characterHealth = (ProgressBar) rootView.findViewById(R.id.pbHealth);


        //characterName.setText(String.valueOf(getArguments().getLong("CharacterID")));

        long id = getArguments().getLong("CharacterID");
        if(id>0) {
            NetworkService.getInstance()
                    .getRestCharacterAPI()
                    .getCharacter(id)
                    .enqueue(new Callback<Character>() {
                        @Override
                        public void onResponse(Call<Character> call, Response<Character> response) {
                            SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
                            preferences.edit().putLong("CHARACTER_ID", response.body().getId()).apply();
                            setCharacterInf(response.body());
                        }

                        @Override
                        public void onFailure(Call<Character> call, Throwable t) {
                            Toast.makeText(getContext(),"SmthWrong",Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView()).navigate(R.id.charactersList);
                        }
                    });
        }


        return rootView;
    }

    void setCharacterInf(Character character) {
        characterName.setText(character.getName());
        characterRace.setText(character.getRace());
        characterClass.setText(character.getClassC());
        characterLvl.setText(String.valueOf(character.getLvl()));
        characterMoney.setText(String.valueOf(character.getMoney()));
        characterHealth.setMax(character.getHp_max());
        characterHealth.setProgress(character.getHp_cur());
        characterAlignment.setText(character.getAlignment());

    }

}