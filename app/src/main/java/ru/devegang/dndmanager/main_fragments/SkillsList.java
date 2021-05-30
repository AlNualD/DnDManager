package ru.devegang.dndmanager.main_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.HTTP;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Skill;
import ru.devegang.dndmanager.networking.NetworkService;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillsList extends Fragment {

    private RecyclerView recyclerView;
    private SkillsRecyclerViewAdapter adapter;
    private FloatingActionButton addButton;
    private SharedPreferences preferences;

    List<Skill> skills;



    long characterID;

    public SkillsList() {
        // Required empty public constructor
    }


    public static SkillsList newInstance(String param1, String param2) {
        SkillsList fragment = new SkillsList();
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
        View rootView = inflater.inflate(R.layout.fragment_skills_list, container, false);
        getActivity().setTitle("Список навыков");

        preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        characterID = preferences.getLong("CharacterID", -1);

        final int resID = characterID==-1 ? R.id.skillInfo2 : R.id.skillInfo;
        recyclerView = (RecyclerView)rootView.findViewById(R.id.rvSkillsList);
        skills = new ArrayList<>();
        adapter = new SkillsRecyclerViewAdapter(this,skills, resID);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        addButton = (FloatingActionButton)rootView.findViewById(R.id.fabAddSkill);
        addButton.setActivated(false);
        addButton.setVisibility(View.GONE);




        if (characterID>0) {
            addButton.setActivated(true);
            addButton.setVisibility(View.VISIBLE);
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .getCharacterSkills(characterID)
                    .enqueue(new Callback<List<Skill>>() {
                        @Override
                        public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                            if(response.code() == HttpStatus.OK.value()) {
                                List<Skill> responseList = response.body();
                                skills.addAll(responseList);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(),"Its empty here", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Skill>> call, Throwable t) {

                        }
                    });

        } else {

            preferences = getActivity().getSharedPreferences("USER_INF", MODE_PRIVATE);
            long id = preferences.getLong("USER_ID",-1);

            if(id != -1){
                NetworkService.getInstance()
                        .getRestCharacterAPI()
                        .getUserSkills(id)
                        .enqueue(new Callback<List<Skill>>() {
                            @Override
                            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                                if(response.code() == HttpStatus.OK.value()) {
                                    skills.addAll(response.body());
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(),"Its empty here", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Skill>> call, Throwable t) {
                                Toast.makeText(getContext(),"Error in loading", Toast.LENGTH_SHORT).show();
                            }
                        });
        }


    }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("SkillID", -1);
                Navigation.findNavController(view).navigate(R.id.skillInfoEdit, bundle);
            }
        });
        return rootView;
    }
}