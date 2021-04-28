package ru.devegang.dndmanager.main_fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.devegang.dndmanager.R;

public class SpellsViewHolder extends RecyclerView.ViewHolder {

    TextView spellName;
    TextView spellDescription;

    public SpellsViewHolder(@NonNull View itemView) {
        super(itemView);

        spellDescription = (TextView) itemView.findViewById(R.id.tvSpellDescription);
        spellName = (TextView) itemView.findViewById(R.id.tvSpellName);
    }
}
