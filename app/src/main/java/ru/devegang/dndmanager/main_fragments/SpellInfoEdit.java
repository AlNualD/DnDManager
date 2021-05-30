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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Spell;
import ru.devegang.dndmanager.entities.rolls.Modes;
import ru.devegang.dndmanager.entities.rolls.RollingFormula;
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


    EditText dicesAmount;
    EditText dice;
    EditText modif;
    TextView tvd;
    CheckBox isEnableFormula;



    Button saveButton;

    Spell spell;



    public SpellInfoEdit() {
        // Required empty public constructor
    }

    public static SpellInfoEdit newInstance(String param1, String param2) {
        SpellInfoEdit fragment = new SpellInfoEdit();

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
        View rootView =  inflater.inflate(R.layout.fragment_spell_info_edit, container, false);
        getActivity().setTitle("Редактирование");

        spellName = (EditText)rootView.findViewById(R.id.etEditSpellName);
        spellDefinition = (EditText) rootView.findViewById(R.id.etEditSpellDefinition);
        spellDescription = (EditText) rootView.findViewById(R.id.etEditSpellDescription);


        dicesAmount = (EditText) rootView.findViewById(R.id.etEditSpellDicesAmount);
        dice = (EditText) rootView.findViewById(R.id.etSpellEditDice);
        modif = (EditText) rootView.findViewById(R.id.etSpellEditModif);
        tvd = (TextView) rootView.findViewById(R.id.tvSpellEditd);
        isEnableFormula =(CheckBox) rootView.findViewById(R.id.cbEditSpellactivateFormula);

        isEnableFormula.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                visibility(b);
            }
        });


        spell = new Spell();

        long spellID = getArguments().getLong("SpellID");
        if (spellID>0) {
            spell.setId(spellID);
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
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
            long characterID = preferences.getLong("CharacterID", -1);
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
                if(isEnableFormula.isChecked()) {
                    Modes curMod = Modes.NORMAL;
                    int diceA = Integer.parseInt(dicesAmount.getText().toString());
                    int diceCur = Integer.parseInt(dice.getText().toString());
                    int modifCur = Integer.parseInt(modif.getText().toString());
                    RollingFormula formula = new RollingFormula(curMod,diceA,diceCur,modifCur);
                    spell.setFormula(formula.toString());
                }
                if(spellID > 0) {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .updateSpell(spellID,spell)
                            .enqueue(new Callback<Spell>() {
                                @Override
                                public void onResponse(Call<Spell> call, Response<Spell> response) {
                                    Navigation.findNavController(getView()).popBackStack();

                                }

                                @Override
                                public void onFailure(Call<Spell> call, Throwable t) {
                                    Toast.makeText(getContext(),"Smth wrong", Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .createSpell(spell.getCharacterID(),spell)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(getContext(),"SUCCESS! wrong", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getView()).popBackStack();

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getContext(),"Smth wrong", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });

        return rootView;
    }


    private  void visibility(boolean f) {
        if(!f) {
            dicesAmount.setVisibility(View.GONE);
            dice.setVisibility(View.GONE);
            modif.setVisibility(View.GONE);
            tvd.setVisibility(View.GONE);

        } else {
            dicesAmount.setVisibility(View.VISIBLE);
            dice.setVisibility(View.VISIBLE);
            modif.setVisibility(View.VISIBLE);
            tvd.setVisibility(View.VISIBLE);
        }
    }

    private void setSpellInf(Spell spell) {
        spellName.setText(spell.getName());
        spellDefinition.setText(spell.getDefinition());
        spellDescription.setText(spell.getDescription());
        RollingFormula rollingFormula = RollingFormula.gerRollingFormula(spell.getFormula());
        dicesAmount.setText(String.valueOf(rollingFormula.getDicesAmount()));
        dice.setText(String.valueOf(rollingFormula.getDice()));
        modif.setText(String.valueOf(rollingFormula.getModification()));

    }
}