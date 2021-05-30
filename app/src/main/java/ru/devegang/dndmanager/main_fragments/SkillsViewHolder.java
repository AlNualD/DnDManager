package ru.devegang.dndmanager.main_fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.devegang.dndmanager.R;

public class SkillsViewHolder extends RecyclerView.ViewHolder {

    TextView skillName;
    TextView skillDescription;
    TextView formula;
    TextView value;
    CardView cardVal;
    public SkillsViewHolder(@NonNull View itemView) {
        super(itemView);

        skillName = (TextView) itemView.findViewById(R.id.tvSkillName);
        skillDescription = (TextView) itemView.findViewById(R.id.tvSkillShortDescription);
        value = (TextView) itemView.findViewById(R.id.tvCardSkillValue);
        cardVal = (CardView) itemView.findViewById(R.id.cvCardValue);

    }
}
