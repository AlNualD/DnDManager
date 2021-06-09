package ru.devegang.dndmanager.main_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Spell;
import ru.devegang.dndmanager.networking.NetworkService;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpellsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpellsList extends Fragment {

    private RecyclerView recyclerView;
    private SpellsRecyclerViewAdapter adapter;
    private FloatingActionButton addSpell;
    private SharedPreferences preferences;

    List<Spell> spells;
    List<Spell> full;

    boolean favFlag = false;




    long characterID;

    public SpellsList() {
        // Required empty public constructor
    }


    public static SpellsList newInstance(String param1, String param2) {
        SpellsList fragment = new SpellsList();

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
        inflater.inflate(R.menu.favorite_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.fav_menu_item) {
            if(favFlag) {
                item.setIcon(R.drawable.ic_favorite_star_outline);
                favFlag=false;
            } else {
                item.setIcon(R.drawable.ic_favorite_star);
                favFlag = true;
            }
            adapter.setFavFlag(favFlag);
            recyclerView.swapAdapter(adapter,true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_spells_list, container, false);
        getActivity().setTitle("Список заклинаний");

        favFlag = false;
        getActivity().invalidateOptionsMenu();

        preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        characterID = preferences.getLong("CharacterID", -1);
        final int resID = characterID == -1 ? R.id.spellInfo2 : R.id.spellInfo;

        recyclerView = (RecyclerView)rootView.findViewById(R.id.rvSpellsList);
        spells = new ArrayList<>();
        full = new ArrayList<>();
        adapter = new SpellsRecyclerViewAdapter(this,spells, resID);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        addSpell = (FloatingActionButton)rootView.findViewById(R.id.fabAddSpell);
        addSpell.setActivated(false);
        addSpell.setVisibility(View.GONE);

        if (characterID>0) {
            addSpell.setActivated(true);
            addSpell.setVisibility(View.VISIBLE);
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .getCharacterSpells(characterID)
                    .enqueue(new Callback<List<Spell>>() {
                        @Override
                        public void onResponse(Call<List<Spell>> call, Response<List<Spell>> response) {
                            if(response.code() == HttpStatus.OK.value()) {
                                List<Spell> responseList = response.body();
                                responseList.sort(new Comparator<Spell>() {
                                    @Override
                                    public int compare(Spell s1, Spell s2) {
                                        return (int) (s1.getId() - s2.getId());
                                    }
                                });
                                spells.addAll(responseList);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(),"Its empty here", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Spell>> call, Throwable t) {

                        }
                    });

        } else {

            preferences = getActivity().getSharedPreferences("USER_INF", MODE_PRIVATE);
            long id = preferences.getLong("USER_ID",-1);

            if(id != -1){
                NetworkService.getInstance()
                        .getRestCharacterAPIv2()
                        .getUserSpells(id)
                        .enqueue(new Callback<List<Spell>>() {
                            @Override
                            public void onResponse(Call<List<Spell>> call, Response<List<Spell>> response) {
                                if(response.code() == HttpStatus.OK.value()) {
                                    spells.addAll(response.body());
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(),"Its empty here", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Spell>> call, Throwable t) {
                                Toast.makeText(getContext(),"Error in loading", Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        }
        addSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("SpellID", -1);
//                Navigation.findNavController(view).navigate(R.id.spellsList, bundle);
                Navigation.findNavController(view).navigate(R.id.spellInfoEdit,bundle);
            }
        });



        return rootView;
    }

}