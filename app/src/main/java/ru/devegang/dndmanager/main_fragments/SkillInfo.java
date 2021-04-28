package ru.devegang.dndmanager.main_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Skill;
import ru.devegang.dndmanager.networking.NetworkService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillInfo extends Fragment {

    private TextView skillName;
    private  TextView skillDefinition;
    private TextView skillDescription;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SkillInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SkillInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static SkillInfo newInstance(String param1, String param2) {
        SkillInfo fragment = new SkillInfo();
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
        View rootView = inflater.inflate(R.layout.fragment_skill_info, container, false);
        skillName = (TextView)rootView.findViewById(R.id.tvInfoSkillName);
        skillDefinition = (TextView)rootView.findViewById(R.id.tvInfoSkillDefinition);
        skillDescription = (TextView)rootView.findViewById(R.id.tvInfoSkillDescription);

        long id = getArguments().getLong("SkillID");
        if(id>0) {
            NetworkService.getInstance()
                    .getRestCharacterAPI()
                    .getSkill(id)
                    .enqueue(new Callback<Skill>() {
                        @Override
                        public void onResponse(Call<Skill> call, Response<Skill> response) {
                            setSkillInf(response.body());
                        }

                        @Override
                        public void onFailure(Call<Skill> call, Throwable t) {
                            Toast.makeText(getContext(),"SmthWrong",Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView()).navigate(R.id.skillsList);
                        }
                    });
        }

        return rootView;
    }

    private void setSkillInf(Skill skill) {
        skillName.setText(skill.getName());
        skillDefinition.setText(skill.getDefinition());
        skillDescription.setText(skill.getDescription());
    }
}