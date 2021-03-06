package ru.devegang.dndmanager.main_fragments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.devegang.dndmanager.R;

public class SpellsViewHolder extends RecyclerView.ViewHolder {

    TextView spellName;
    TextView spellDescription;
    ImageButton favButton;

    public SpellsViewHolder(@NonNull View itemView) {
        super(itemView);

        spellDescription = (TextView) itemView.findViewById(R.id.tvSpellDescription);
        spellName = (TextView) itemView.findViewById(R.id.tvSpellName);
        favButton = (ImageButton) itemView.findViewById(R.id.ibSpellCardFavorite);
    }
}
