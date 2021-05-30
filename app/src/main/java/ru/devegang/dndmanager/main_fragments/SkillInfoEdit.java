package ru.devegang.dndmanager.main_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Attribute;
import ru.devegang.dndmanager.entities.Skill;
import ru.devegang.dndmanager.entities.rolls.Modes;
import ru.devegang.dndmanager.entities.rolls.RollingFormula;
import ru.devegang.dndmanager.networking.NetworkService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillInfoEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillInfoEdit extends Fragment {

    SharedPreferences preferences;

    EditText skillName;
    EditText skillDefinition;
    EditText skillDescription;
    EditText value;


   CheckBox addAttr;

   CheckBox changeValue;

   CheckBox canBeTrained;
   Spinner coeff;

    Spinner attr;
    List<String> attributesNames;
    List<Attribute> attributes;
    ArrayAdapter<String> adapterAttributes;

    Button saveButton;

    long skillId;
    Skill skill;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SkillInfoEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SkillInfoEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static SkillInfoEdit newInstance(String param1, String param2) {
        SkillInfoEdit fragment = new SkillInfoEdit();
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
        View rootView =  inflater.inflate(R.layout.fragment_skill_info_edit, container, false);
        getActivity().setTitle("Редактирование");

        skillName = (EditText) rootView.findViewById(R.id.etEditSkillName);
        skillDefinition = (EditText)rootView.findViewById(R.id.etEditSkillDefinition);
        skillDescription = (EditText)rootView.findViewById(R.id.etEditSkillDescription);
        value = (EditText) rootView.findViewById(R.id.etEditSkillValue);

        changeValue = (CheckBox) rootView.findViewById(R.id.cbEditSkillChangeValue);

        value.setVisibility(View.GONE);

        changeValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    value.setVisibility(View.VISIBLE);
                } else {
                    value.setVisibility(View.GONE);
                }
            }
        });

        attr = (Spinner) rootView.findViewById(R.id.spinnerSkillAttributeChoise);

        canBeTrained = (CheckBox) rootView.findViewById(R.id.cbSkillEditCanBeTrained);
        coeff = (Spinner) rootView.findViewById(R.id.spinnerSkillEditCoeff);
        coeff.setVisibility(View.GONE);

        canBeTrained.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    coeff.setVisibility(View.VISIBLE);
                } else {
                    coeff.setVisibility(View.GONE);
                }
            }
        });
        visibility(false);


       addAttr = (CheckBox) rootView.findViewById(R.id.cbSkillEditAddAttr);
       addAttr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

               visibility(b);
           }
       });

        attributes = new ArrayList<>();
        attributesNames = new ArrayList<>();
        skill = new Skill();

        preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
        long characterID = preferences.getLong("CHARACTER_ID", -1);
        if(characterID!= -1) {
            skill.setCharacterID(characterID);

        }
        skillId = getArguments().getLong("SkillID");
        if(skillId>0) {
            skill.setId(skillId);
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .getSkill(skillId)
                    .enqueue(new Callback<Skill>() {
                        @Override
                        public void onResponse(Call<Skill> call, Response<Skill> response) {
                            setSkillInf(response.body());
                        }

                        @Override
                        public void onFailure(Call<Skill> call, Throwable t) {

                        }
                    });
        } else {

        }

        saveButton = (Button) rootView.findViewById(R.id.bSaveSkill);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skill.setName(skillName.getText().toString());
                skill.setDefinition(skillDefinition.getText().toString());
                skill.setDescription(skillDescription.getText().toString());
                long attrId;
//                Attribute curAttr = null;
                if(attr.getVisibility() == View.VISIBLE){
                    attrId = attributes.get(attr.getSelectedItemPosition()).getId();
                }  else {
                    attrId = -1;
                }
                skill.setValue(-1);
                if(value.getVisibility() == View.VISIBLE) {
                    skill.setValue(Integer.parseInt(value.getText().toString()));
                }
                skill.setCanBeTrained(canBeTrained.isChecked());
                skill.setTrainCoefficient(-1);
                if(skill.isCanBeTrained()) {
                    skill.setTrainCoefficient(coeff.getSelectedItemPosition() - 1);
                }


                if(skillId > 0) {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .updateSkill(skillId,skill)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()) {
                                        if(attrId > 0) {
                                            NetworkService.getInstance()
                                                    .getRestCharacterAPIv2()
                                                    .addAttributeToSkill(skillId, attrId)
                                                    .enqueue(new Callback<Void>() {
                                                        @Override
                                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                                            Navigation.findNavController(getView()).popBackStack();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Void> call, Throwable t) {
                                                            Toast.makeText(getContext(),"Smth wrong", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                        } else {
                                            Navigation.findNavController(getView()).popBackStack();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });


                } else {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .createSkill(skill.getCharacterID(),skill)
                            .enqueue(new Callback<Skill>() {
                                @Override
                                public void onResponse(Call<Skill> call, Response<Skill> response) {
                                    if(attrId > 0) {
                                        NetworkService.getInstance()
                                                .getRestCharacterAPIv2()
                                                .addAttributeToSkill(skillId, attrId)
                                                .enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        Navigation.findNavController(getView()).popBackStack();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {
                                                        Toast.makeText(getContext(),"Smth wrong", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                    } else {
                                        Navigation.findNavController(getView()).popBackStack();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Skill> call, Throwable t) {
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
            attr.setVisibility(View.GONE);

        } else {
            if(attributesNames.isEmpty()) {
                preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
                long characterID = preferences.getLong("CharacterID", -1);
                if(characterID != -1) {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .getCharacterAttributes(characterID)
                            .enqueue(new Callback<List<Attribute>>() {
                                @Override
                                public void onResponse(Call<List<Attribute>> call, Response<List<Attribute>> response) {

                                    if(response.isSuccessful()){
                                        attributes.addAll(response.body());
                                        for (Attribute attribute : attributes) {
                                            attributesNames.add(attribute.getName());
                                        }
                                        adapterAttributes = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, attributesNames);
                                        attr.setAdapter(adapterAttributes);
                                        attr.setVisibility(View.VISIBLE);

                                    } else {
                                        attr.setVisibility(View.GONE);
                                        Toast.makeText(getContext(),"Атрибуты не найдены", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Attribute>> call, Throwable t) {

                                }
                            });
                }

            } else {
                attr.setVisibility(View.VISIBLE);
            }

            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .getSkillAttribute(skillId)
                    .enqueue(new Callback<Attribute>() {
                        @Override
                        public void onResponse(Call<Attribute> call, Response<Attribute> response) {
                            if(response.isSuccessful()) {
                                int pos = attributes.indexOf(response.body());
                                attr.setSelection(pos);
                            }
                        }

                        @Override
                        public void onFailure(Call<Attribute> call, Throwable t) {

                        }
                    });

        }
    }


    private void setSkillInf(Skill skill) {
        skillName.setText(skill.getName());
        skillDefinition.setText(skill.getDefinition());
        skillDescription.setText(skill.getDescription());
        canBeTrained.setChecked(skill.isCanBeTrained());
        if(skill.isCanBeTrained()) coeff.setVisibility(View.VISIBLE);
        coeff.setSelection(skill.getTrainCoefficient() + 1);
    }
}