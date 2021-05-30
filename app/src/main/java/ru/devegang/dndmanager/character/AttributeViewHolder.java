package ru.devegang.dndmanager.character;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.devegang.dndmanager.R;

public class AttributeViewHolder extends RecyclerView.ViewHolder  {

    TextView AttrName;
    TextView AttrValue;
    TextView AttrModif;
    CardView card;


    public AttributeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.card = (CardView) itemView.findViewById(R.id.cardAttribute);
        this.AttrName = (TextView) itemView.findViewById(R.id.tvAttrName);
        this.AttrValue = (TextView) itemView.findViewById(R.id.tvAttrValue);
        this.AttrModif = (TextView) itemView.findViewById(R.id.tvAttrModif);
    }
}
