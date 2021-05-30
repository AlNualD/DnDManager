package ru.devegang.dndmanager.main_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.RollResultDialog;
import ru.devegang.dndmanager.entities.Skill;
import ru.devegang.dndmanager.entities.rolls.Modes;
import ru.devegang.dndmanager.entities.rolls.RollingFormula;
import ru.devegang.dndmanager.entities.rolls.RollingResult;
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
    private TextView value;
    ConstraintLayout layout;






    long skillID;


    public SkillInfo() {
        // Required empty public constructor
    }


    public static SkillInfo newInstance(String param1, String param2) {
        SkillInfo fragment = new SkillInfo();

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
        inflater.inflate(R.menu.edit_delete_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
        long characterId = preferences.getLong("CharacterID", -1);

        switch (item.getItemId()) {

            case R.id.basicEditItem:
                Bundle bundle = new Bundle();
                bundle.putLong("SkillID", skillID);
                if(characterId == -1) {
                    Navigation.findNavController(getView()).navigate(R.id.skillInfoEdit2,bundle);
                } else {
                    Navigation.findNavController(getView()).navigate(R.id.skillInfoEdit,bundle);
                }
                break;
            case R.id.basicDeleteItem:
                NetworkService.getInstance()
                        .getRestCharacterAPIv2()
                        .deleteSpell(skillID)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()) {
                                    Navigation.findNavController(getView()).popBackStack();
                                } else {
                                    Toast.makeText(getContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_skill_info, container, false);
        getActivity().setTitle("Навык");

        skillName = (TextView)rootView.findViewById(R.id.tvInfoSkillName);
        skillDefinition = (TextView)rootView.findViewById(R.id.tvInfoSkillDefinition);
        skillDescription = (TextView)rootView.findViewById(R.id.tvInfoSkillDescription);
        value = (TextView) rootView.findViewById(R.id.tvSkillValue);
        layout = (ConstraintLayout) rootView.findViewById(R.id.clSkillInfo);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                RollingFormula rollingFormula = new RollingFormula(Modes.NORMAL, 1,20,0);
                PopupMenu popupMenu = new PopupMenu(getContext(),layout);
                popupMenu.inflate(R.menu.rolling_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menuRollNegative:
                                rollingFormula.setMode(Modes.DISADVANTAGE);
                                break;
                            case R.id.menuRollNormal:
                                rollingFormula.setMode(Modes.NORMAL);
                                break;
                            case R.id.menuRollPositive:
                                rollingFormula.setMode(Modes.ADVANTAGE);
                                break;
                        }
                        makeRoll(rollingFormula);
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });


        skillID = getArguments().getLong("SkillID");
        if(skillID>0) {
            NetworkService.getInstance()
                    .getRestCharacterAPI()
                    .getSkill(skillID)
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

    private void makeRoll(RollingFormula rollingFormula) {

        String modif = value.getText().toString();
        if(!modif.isEmpty()){
            rollingFormula.setModification(Integer.parseInt(modif));
        }

        NetworkService.getInstance()
                .getRestRollingAPIv2()
                .simpleRoll(rollingFormula.toString())
                .enqueue(new Callback<RollingResult>() {
                    @Override
                    public void onResponse(Call<RollingResult> call, Response<RollingResult> response) {
                        if(response.isSuccessful()) showDialog(response.body().getResult());
                    }

                    @Override
                    public void onFailure(Call<RollingResult> call, Throwable t) {

                    }
                });
    }

    private void showDialog(int res) {
        FragmentManager fragmentManager = getParentFragmentManager();
        RollResultDialog resultDialog = new RollResultDialog();
        resultDialog.setCancelable(true);
        resultDialog.setTargetFragment(this,111);
        resultDialog.show(fragmentManager,String.valueOf(res));
    }

    private void setSkillInf(Skill skill) {
        skillName.setText(skill.getName());
        skillDefinition.setText(skill.getDefinition());
        skillDescription.setText(skill.getDescription());
        value.setText(String.valueOf(skill.getValue()));
    }
}