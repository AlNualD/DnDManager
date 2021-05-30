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
import java.util.List;

import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Spell;

public class SpellsRecyclerViewAdapter extends RecyclerView.Adapter<SpellsViewHolder> {
    private List<Spell> spells;
    private LayoutInflater layoutInflater;
    private WeakReference<SpellsList> spellsListWeakReference;
    final  int resID;

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

        Spell spell = this.spells.get(position);
        holder.spellName.setText(spell.getName());
        holder.spellDescription.setText(spell.getDefinition());
    }

    @Override
    public int getItemCount() {
        return spells.size();
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        Spell spell = this.spells.get(itemPosition);
        SpellsList spellsList = spellsListWeakReference.get();
        if(spellsList != null) {
            Toast.makeText(spellsList.getContext(), spell.getName(), Toast.LENGTH_LONG).show();


            Bundle bundle = new Bundle();
            bundle.putLong("SpellID",spell.getId());
            Navigation.findNavController(spellsList.getView()).navigate(resID, bundle);

        }
    }
}
