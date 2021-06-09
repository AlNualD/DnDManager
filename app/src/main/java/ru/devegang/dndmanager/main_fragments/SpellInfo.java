package ru.devegang.dndmanager.main_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.AttributeDialog;
import ru.devegang.dndmanager.dialogs.RollResultDialog;
import ru.devegang.dndmanager.entities.Spell;
import ru.devegang.dndmanager.entities.rolls.Modes;
import ru.devegang.dndmanager.entities.rolls.RollingFormula;
import ru.devegang.dndmanager.entities.rolls.RollingResult;
import ru.devegang.dndmanager.networking.NetworkService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpellInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpellInfo extends Fragment {


    private TextView spellName;
    private TextView spellDefinition;
    private TextView spellDescription;
    private TextView spellFormula;

    long spellID;

    Spell spell;





    public SpellInfo() {
        // Required empty public constructor
    }


    public static SpellInfo newInstance(String param1, String param2) {
        SpellInfo fragment = new SpellInfo();

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
                bundle.putLong("SpellID", spellID);
                if(characterId == -1) {
                    Navigation.findNavController(getView()).navigate(R.id.spellInfoEdit2,bundle);
                } else {
                    Navigation.findNavController(getView()).navigate(R.id.spellInfoEdit,bundle);
                }
                break;
            case R.id.basicDeleteItem:
                NetworkService.getInstance()
                        .getRestCharacterAPIv2()
                        .deleteSpell(spellID)
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
        View rootView =  inflater.inflate(R.layout.fragment_spell_info, container, false);
        getActivity().setTitle("Заклинание");

        spellName = (TextView) rootView.findViewById(R.id.tvInfoSpellName);
        spellDefinition = (TextView) rootView.findViewById(R.id.tvInfoSpellDefinition);
        spellDescription = (TextView) rootView.findViewById(R.id.tvInfoSpellDescription);
        spellFormula = (TextView)  rootView.findViewById(R.id.tvSpellFormula);


        spellID = getArguments().getLong("SpellID");
        if(spellID>0) {
            NetworkService.getInstance()
                    .getRestCharacterAPI()
                    .getSpell(spellID)
                    .enqueue(new Callback<Spell>() {
                        @Override
                        public void onResponse(Call<Spell> call, Response<Spell> response) {
                            spell = response.body();
                            setSpellInf(response.body());
                        }

                        @Override
                        public void onFailure(Call<Spell> call, Throwable t) {
                            Toast.makeText(getContext(),"SmthWrong",Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView()).navigate(R.id.spellsList);
                        }
                    });
        }

        spellFormula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spell.getFormula() == "") return;
                RollingFormula rollingFormula = RollingFormula.gerRollingFormula(spell.getFormula());
                PopupMenu popupMenu = new PopupMenu(getContext(),spellFormula);
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
            }
        });

        return rootView;
    }

    private void makeRoll(RollingFormula rollingFormula) {
        NetworkService.getInstance()
                .getRestRollingAPIv2()
                .simpleRoll(rollingFormula.toString())
                .enqueue(new Callback<RollingResult>() {
                    @Override
                    public void onResponse(Call<RollingResult> call, Response<RollingResult> response) {
                        if(response.isSuccessful()) {
                            showDialog(response.body().getResult());
                        }
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

    private void setSpellInf(Spell spell) {
        spellFormula.setVisibility(View.VISIBLE);
        spellName.setText(spell.getName());
        spellDefinition.setText(spell.getDefinition());
        spellDescription.setText(spell.getDescription());
        if(spell.getFormula() == null || spell.getFormula().isEmpty()) {
            spellFormula.setText("");
            spellFormula.setVisibility(View.GONE);
        } else {
            spellFormula.setText(RollingFormula.gerRollingFormula(spell.getFormula()).getReadable());
        }

    }
}