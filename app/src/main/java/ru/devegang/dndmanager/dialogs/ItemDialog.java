package ru.devegang.dndmanager.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.character.InventoryList;
import ru.devegang.dndmanager.entities.Item;
import ru.devegang.dndmanager.entities.rolls.Modes;
import ru.devegang.dndmanager.entities.rolls.RollingFormula;
import ru.devegang.dndmanager.networking.NetworkService;

public class ItemDialog extends DialogFragment {
    ImageButton okButton;
    ImageButton cancelButton;
    EditText name;

    EditText dicesAmount;
    EditText dice;
    EditText modif;
    TextView tvd;
    CheckBox isEnableFormula;


    EditText description;
    EditText weight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_item, container, false);

        okButton = (ImageButton) view.findViewById(R.id.ibDIsaveItem);
        cancelButton = (ImageButton) view.findViewById(R.id.ibDIcancel);


        name = (EditText) view.findViewById(R.id.edDIname);



        dicesAmount = (EditText) view.findViewById(R.id.etDIDicesAmount);
        dice = (EditText) view.findViewById(R.id.etDIDice);
        modif = (EditText) view.findViewById(R.id.etDIModif);
        tvd = (TextView) view.findViewById(R.id.tvDId);



        visibility(false);

        if(!getTag().isEmpty()) {
            long itemId = Long.parseLong(getTag());
            if(itemId> 0) {
                NetworkService.getInstance()
                        .getRestCharacterAPIv2()
                        .getItem(itemId)
                        .enqueue(new Callback<Item>() {
                            @Override
                            public void onResponse(Call<Item> call, Response<Item> response) {
                                if(response.isSuccessful()) {
                                    setItemInf(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<Item> call, Throwable t) {

                            }
                        });
            }

        }

        isEnableFormula = (CheckBox) view.findViewById(R.id.cbIDactivateFormula);

        isEnableFormula.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                visibility(b);
            }
        });


        weight = (EditText) view.findViewById(R.id.etDIweight);
        description = (EditText) view.findViewById(R.id.etDIdescription);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
                getDialog().cancel();
            }
        });

        return view;
    }


    private  void visibility(boolean f) {
        if(!f) {
            dicesAmount.setVisibility(View.GONE);
            dice.setVisibility(View.GONE);
            modif.setVisibility(View.GONE);
            tvd.setVisibility(View.GONE);

        } else {
            dicesAmount.setVisibility(View.VISIBLE);
            dice.setVisibility(View.VISIBLE);
            modif.setVisibility(View.VISIBLE);
            tvd.setVisibility(View.VISIBLE);
        }
    }


    private void setItemInf(Item itemInf) {
        name.setText(itemInf.getName());
        description.setText(itemInf.getDefinition());
        if(itemInf.getFormula().isEmpty()) {
            visibility(false);
        } else {
            visibility(true);
            RollingFormula rollingFormula = RollingFormula.gerRollingFormula(itemInf.getFormula());
            dicesAmount.setText(String.valueOf(rollingFormula.getDicesAmount()));
            dice.setText(String.valueOf(rollingFormula.getDice()));
            modif.setText(String.valueOf(rollingFormula.getModification()));
        }
        weight.setText(String.valueOf(itemInf.getWeight()));
    }



    private void saveItem() {
        Item item = new Item();
        if(!getTag().isEmpty()) {
            item.setId(Integer.parseInt(getTag()));
        }
        item.setName(name.getText().toString());
        item.setDefinition(description.getText().toString());
        item.setFormula("");
        if(isEnableFormula.isChecked()) {
            Modes curMod = Modes.NORMAL;
            int diceA = Integer.parseInt(dicesAmount.getText().toString());
            int diceCur = Integer.parseInt(dice.getText().toString());
            int modifCur = Integer.parseInt(modif.getText().toString());
            RollingFormula formula = new RollingFormula(curMod,diceA,diceCur,modifCur);
            item.setFormula(formula.toString());
        }

        String number = weight.getText().toString();
        int val = number.isEmpty()? 0 : Integer.parseInt(number);
        item.setWeight(val);

        ((InventoryList) getTargetFragment()).saveItem(item);


    }

}
