package ru.devegang.dndmanager.main_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Skill;
import ru.devegang.dndmanager.entities.Spell;
import ru.devegang.dndmanager.networking.NetworkService;

public class SpellsRecyclerViewAdapter extends RecyclerView.Adapter<SpellsViewHolder> {
    private List<Spell> spells;
    private List<Spell> favorites;
    private LayoutInflater layoutInflater;
    private WeakReference<SpellsList> spellsListWeakReference;
    final  int resID;
    private boolean favFlag = false;

    public void setFavFlag(boolean favFlag) {
        this.favFlag = favFlag;
        if(favFlag && favorites == null) {
            extractFavorites();
        }
    }

    public void extractFavorites() {
        favorites = new LinkedList<>();
        for (Spell spell : spells) {
            if(spell.getFavorite()) {
                favorites.add(spell);
            }
        }
    }

    public SpellsRecyclerViewAdapter(SpellsList spellsList, List<Spell>spells, int resID) {
        this.spells = spells;
        spellsListWeakReference = new WeakReference<>(spellsList);
        layoutInflater = LayoutInflater.from(spellsList.getContext());
        this.resID = resID;
    }

    @NonNull
    @Override
    public SpellsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  item = layoutInflater.inflate(R.layout.card_spell,parent,false);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRecyclerItemClick((RecyclerView)parent,view);
            }
        });
        return new SpellsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull SpellsViewHolder holder, int position) {

        List<Spell> spells;
        if(favFlag) {
            spells = favorites;
        } else {
            spells = this.spells;
        }
        Spell spell = this.spells.get(position);
        holder.spellName.setText(spell.getName());
        holder.spellDescription.setText(spell.getDefinition());
        if(spell.getFavorite()) {
            holder.favButton.setImageResource(R.drawable.ic_favorite_star);
        } else {
            holder.favButton.setImageResource(R.drawable.ic_favorite_star_outline);
        }

        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spell.setFavorite(!spell.getFavorite());
                NetworkService.getInstance()
                        .getRestCharacterAPIv2()
                        .setFavoriteSpell(spell.getId(),spell.getFavorite())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                notifyItemChanged(position);
                                if(favorites != null) {
                                    if(spell.getFavorite()) {
                                        favorites.add(spell);
                                    } else {
                                        favorites.removeIf(x -> x.getId() == spell.getId());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return favFlag ? favorites.size() : spells.size();
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        Spell spell;
        if(favFlag) {
            spell = this.favorites.get(itemPosition);
        } else {
            spell = this.spells.get(itemPosition);
        }
        SpellsList spellsList = spellsListWeakReference.get();
        if(spellsList != null) {
            Toast.makeText(spellsList.getContext(), spell.getName(), Toast.LENGTH_LONG).show();


            Bundle bundle = new Bundle();
            bundle.putLong("SpellID",spell.getId());
            Navigation.findNavController(spellsList.getView()).navigate(resID, bundle);

        }
    }
}
