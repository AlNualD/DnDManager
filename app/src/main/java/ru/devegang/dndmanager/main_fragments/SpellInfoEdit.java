package ru.devegang.dndmanager.main_fragments;

import android.content.Context;
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
import ru.devegang.dndmanager.entities.Spell;
import ru.devegang.dndmanager.networking.NetworkService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpellInfoEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpellInfoEdit extends Fragment {

    EditText spellName;
    EditText spellDefinition;
    EditText spellDescription;

    Button saveButton;

    Spell spell;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SpellInfoEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpellInfoEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static SpellInfoEdit newInstance(String param1, String param2) {
        SpellInfoEdit fragment = new SpellInfoEdit();
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
        View rootView =  inflater.inflate(R.layout.fragment_spell_info_edit, container, false);

        spellName = (EditText)rootView.findViewById(R.id.etEditSpellName);
        spellDefinition = (EditText) rootView.findViewById(R.id.etEditSpellDefinition);
        spellDescription = (EditText) rootView.findViewById(R.id.etEditSpellDescription);

        spell = new Spell();

        long spellID = getArguments().getLong("SpellID");
        if (spellID>0) {
            spell.setId(spellID);
            NetworkService.getInstance()
                    .getRestCharacterAPI()
                    .getSpell(spellID)
                    .enqueue(new Callback<Spell>() {
                        @Override
                        public void onResponse(Call<Spell> call, Response<Spell> response) {
                            setSpellInf(response.body());
                        }

                        @Override
                        public void onFailure(Call<Spell> call, Throwable t) {

                        }
                    });
        } else {
            SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
            long characterID = preferences.getLong("CHARACTER_ID", -1);
            if(characterID != -1) {
                spell.setCharacterID(characterID);
            } else {
                Toast.makeText(getContext(),"To create skill choose character first", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.skillsList);
            }

        }

        saveButton = (Button) rootView.findViewById(R.id.bSaveSpell);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spell.setName(spellName.getText().toString());
                spell.setDefinition(spellDefinition.getText().toString());
                spell.setDescription(spellDescription.getText().toString());
                if(spellID > 0) {
                    NetworkService.getInstance()
                            .getRestCharacterAPI()
                            .updateSpell(spellID,spell)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(getContext(),"SUCCESS! wrong", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getView()).navigate(R.id.spellsList);

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(),"Smth wrong", Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    NetworkService.getInstance()
                            .getRestCharacterAPI()
                            .createSpell(spell.getCharacterID(),spell)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(getContext(),"SUCCESS! wrong", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getView()).navigate(R.id.spellsList);

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(),"Smth wrong", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });

        return rootView;
    }

    private void setSpellInf(Spell spell) {
        spellName.setText(spell.getName());
        spellDefinition.setText(spell.getDefinition());
        spellDescription.setText(spell.getDescription());
    }
}