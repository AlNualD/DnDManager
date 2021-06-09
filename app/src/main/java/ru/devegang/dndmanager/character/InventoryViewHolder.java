package ru.devegang.dndmanager.character;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.devegang.dndmanager.R;

public class InventoryViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView formula;
    TextView weight;
    TextView description;
    CardView card;
    ImageButton favButton;

    public InventoryViewHolder(@NonNull View itemView) {
        super(itemView);

        card = (CardView) itemView.findViewById(R.id.cardItem);
        name = (TextView) itemView.findViewById(R.id.tvItemName);
        formula =(TextView) itemView.findViewById(R.id.tvItemFormula);
        weight = (TextView) itemView.findViewById(R.id.tvItemWeight);
        description = (TextView) itemView.findViewById(R.id.tvItemDefinition);
        favButton = (ImageButton) itemView.findViewById(R.id.ibItemCardFavorite);
    }
}
