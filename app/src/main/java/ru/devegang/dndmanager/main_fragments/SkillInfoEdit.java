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
import ru.devegang.dndmanager.entities.Skill;
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

    Button saveButton;

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
        skillName = (EditText) rootView.findViewById(R.id.etEditSkillName);
        skillDefinition = (EditText)rootView.findViewById(R.id.etEditSkillDefinition);
        skillDescription = (EditText)rootView.findViewById(R.id.etEditSkillDescription);

        skill = new Skill();


        long skillId = getArguments().getLong("SkillID");
        if(skillId>0) {
            skill.setId(skillId);
            NetworkService.getInstance()
                    .getRestCharacterAPI()
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
            preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
            long characterID = preferences.getLong("CHARACTER_ID", -1);
            if(characterID!= -1) {
                skill.setCharacterID(characterID);
            } else {
                Toast.makeText(getContext(),"To create skill choose character first", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.skillsList);
            }
        }

        saveButton = (Button) rootView.findViewById(R.id.bSaveSkill);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skill.setName(skillName.getText().toString());
                skill.setDefinition(skillDefinition.getText().toString());
                skill.setDescription(skillDescription.getText().toString());
                if(skillId > 0) {
                    NetworkService.getInstance()
                            .getRestCharacterAPI()
                            .updateSkill(skillId,skill)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(getContext(),"SUCCESS! wrong", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getView()).navigate(R.id.skillsList);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(),"Smth wrong", Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    NetworkService.getInstance()
                            .getRestCharacterAPI()
                            .createSkill(skill.getCharacterID(),skill)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(getContext(),"SUCCESS! wrong", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getView()).navigate(R.id.skillsList);

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

    private void setSkillInf(Skill skill) {
        skillName.setText(skill.getName());
        skillDefinition.setText(skill.getDefinition());
        skillDescription.setText(skill.getDescription());
    }
}