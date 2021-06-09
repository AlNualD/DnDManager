package ru.devegang.dndmanager.character;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.ChangeHPDialog;
import ru.devegang.dndmanager.dialogs.MakeRollDialog;
import ru.devegang.dndmanager.dialogs.RollResultDialog;
import ru.devegang.dndmanager.entities.Attribute;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.networking.NetworkService;

import static android.content.Context.MODE_PRIVATE;

public class CharacterInfoFull extends Fragment {

    public CharacterInfoFull() {

    }

    CircleImageView picture;
    TextView cName;
    TextView cRace;
    TextView cClass;
    TextView cLvl;
    TextView cAlignment;
    ProgressBar pbHealth;
    TextView cHealth;
    EditText cDescription;

    LinearLayout layoutHealth;

    RecyclerView attributesRecyclerView;
    AttributesRecyclerViewAdapter adapter;
    List<Attribute> attributes;

    Button inventory;
    Button spells;
    Button skills;
    Button saveDescription;

    Character character = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.character_info_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        long id = preferences.getLong("CharacterID", -1);

        switch (item.getItemId()) {
            case R.id.actionEdit:

                Bundle bundle = new Bundle();
                bundle.putLong("CharacterID", id);
                Navigation.findNavController(getView()).navigate(R.id.characterInfoEdit,bundle);
                break;
            case R.id.actionAddAttr:
                Navigation.findNavController(getView()).navigate(R.id.attributesEdit);

                break;
            case R.id.actionDelete:
                if(id!= -1) {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .deleteCharacter(id)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()) {
                                        Toast.makeText(getContext(),"Удалено", Toast.LENGTH_SHORT).show();
                                        preferences.edit().putLong("CharacterID", -1).apply();
                                        Navigation.findNavController(getView()).navigate(R.id.mainActivity2);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                }
                break;
            case R.id.actionLvlUp:
                if(id!=-1) {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .lvlUp(id)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()) {
                                        loadCharacter(id);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    loadCharacter(id);
                                }
                            });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void  loadCharacter(long id) {
        NetworkService.getInstance()
                .getRestCharacterAPIv2()
                .getCharacter(id)
                .enqueue(new Callback<Character>() {
                    @Override
                    public void onResponse(Call<Character> call, Response<Character> response) {
                        if(response.isSuccessful()) {
                            character = response.body();
                            setCharacterInf(response.body());
                            SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
                            preferences.edit().putInt("CharacterProfBonus",response.body().getProfBonus()).apply();
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<Character> call, Throwable t) {
                    }
                });

        NetworkService.getInstance()
                .getRestCharacterAPIv2()
                .getCharacterAttributes(id)
                .enqueue(new Callback<List<Attribute>>() {
                    @Override
                    public void onResponse(Call<List<Attribute>> call, Response<List<Attribute>> response) {
                        if(response.isSuccessful()) {
                            attributes.clear();
                            attributes.addAll(response.body());
                            attributes.sort(new Comparator<Attribute>() {
                                @Override
                                public int compare(Attribute a1, Attribute a2) {
                                    return (int) (a1.getId() - a2.getId());
                                }
                            });
                            attributesRecyclerView.getAdapter().notifyDataSetChanged();
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Attribute>> call, Throwable t) {
                        Toast.makeText(getContext(),"Opps< smth went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        long id = preferences.getLong("CharacterID", -1);

//        Long id = getArguments().getLong("CharacterID",-1);
        if(id != -1) {
            System.out.print("All good");
            loadCharacter(id);
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_character_info_full,container,false);

        getActivity().setTitle("Персонаж");


        inventory = (Button) rootView.findViewById(R.id.buttonOpenInventory);
        skills = (Button) rootView.findViewById(R.id.buttonOpenSkills);
        spells = (Button) rootView.findViewById(R.id.buttonOpenSpells);


        cName = (TextView) rootView.findViewById(R.id.tvCIFname);
        cRace = (TextView) rootView.findViewById(R.id.tvCIFrace);
        cClass = (TextView) rootView.findViewById(R.id.tvCIFclass);
        cHealth = (TextView) rootView.findViewById(R.id.tvCIFhealth);
        cLvl = (TextView) rootView.findViewById(R.id.tvCIFlvl);
        cAlignment = (TextView) rootView.findViewById(R.id.tvCIFalignment);
        cDescription = (EditText) rootView.findViewById(R.id.etCharacterInfDescription);
        saveDescription = (Button) rootView.findViewById(R.id.bCIFsaveDesc);
        saveDescription.setActivated(false);

        cDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDescription.setActivated(true);
            }
        });

        saveDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!character.getDescription().equals(cDescription.getText().toString())) {
                    character.setDescription(cDescription.getText().toString());
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .updateCharacter(character.getId(), character)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(getContext(), "Сохранено!", Toast.LENGTH_SHORT).show();
                                    saveDescription.setActivated(false);
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                }
            }
        });


        pbHealth = (ProgressBar) rootView.findViewById(R.id.pbCIFhealth);

        layoutHealth = (LinearLayout) rootView.findViewById(R.id.llHealthLayout);

        picture = (CircleImageView) rootView.findViewById(R.id.civCIFpicture);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRollingDialog();
            }
        });

        attributesRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvCIFattributes);
        attributes = new LinkedList<>();
        adapter = new AttributesRecyclerViewAdapter(this, attributes);
        attributesRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        attributesRecyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        long id = preferences.getLong("CharacterID", -1);

//        Long id = getArguments().getLong("CharacterID",-1);
        if(id != -1) {
            System.out.print("All good");
            loadCharacter(id);
        }

        layoutHealth.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showHPdialog();
                return false;
            }
        });


        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.inventoryList);
            }
        });

        spells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id != -1) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("CharacterID", id);
                    Navigation.findNavController(view).navigate(R.id.spellsListCharacter,bundle);
                }
            }
        });
        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id != -1) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("CharacterID", id);
                    Navigation.findNavController(view).navigate(R.id.skillsListCharacter,bundle);
                }
            }
        });



        return rootView;
    }

    private void showRollingDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        MakeRollDialog makeRollDialog = new MakeRollDialog();
        makeRollDialog.setCancelable(true);
        makeRollDialog.setTargetFragment(this,111);
        makeRollDialog.show(fragmentManager,"");
    }

    private void showHPdialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        ChangeHPDialog changeHPDialog = new ChangeHPDialog();
        changeHPDialog.setCancelable(true);
        changeHPDialog.setTargetFragment(this,111);
        changeHPDialog.show(fragmentManager,cHealth.getText().toString());
    }

    public void setupNewHP(int cur) {
        character.setHp_cur(cur);
        pbHealth.setProgress(cur);
        cHealth.setText(cur + "/" + pbHealth.getMax());
    }

    private void setCharacterInf(Character character) {
        cName.setText(character.getName());
        cClass.setText(character.getClassC());
        cRace.setText(character.getRace());
        cAlignment.setText("Мировоззрение: " + character.getAlignment());
        cLvl.setText(String.valueOf(character.getLvl()));
        cHealth.setText(character.getHp_cur() + "/" + character.getHp_max());
        pbHealth.setMax(character.getHp_max());
        pbHealth.setProgress(character.getHp_cur());
        cDescription.setText(character.getDescription());
    }

    private void loadCharacterInf() {

    }

}
