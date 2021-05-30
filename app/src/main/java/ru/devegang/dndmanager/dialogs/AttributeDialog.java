package ru.devegang.dndmanager.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.character.AttributesEdit;
import ru.devegang.dndmanager.entities.Attribute;
import ru.devegang.dndmanager.networking.NetworkService;

import static android.content.Context.MODE_PRIVATE;

public class AttributeDialog extends DialogFragment {

    ImageButton okButton;
    ImageButton cancelButton;
    EditText name;
    EditText value;
    TextView modif;
    CheckBox cbIsTrained;

    long attrId = -1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_attribute, container, false);

        okButton = (ImageButton) view.findViewById(R.id.ibAEsaveAttr);
        cancelButton = (ImageButton) view.findViewById(R.id.ibAEcancel);


        name = (EditText) view.findViewById(R.id.edAEattrName);
        value = (EditText) view.findViewById(R.id.edAEattrValue);
        modif = (TextView) view.findViewById(R.id.tvAEattrModif);
        cbIsTrained = (CheckBox) view.findViewById(R.id.cbAEisTrained);

        if(!getTag().isEmpty()) {
            attrId = Long.parseLong(getTag());
            if(attrId > 0) {
                NetworkService.getInstance()
                        .getRestCharacterAPIv2()
                        .getAttribute(attrId)
                        .enqueue(new Callback<Attribute>() {
                            @Override
                            public void onResponse(Call<Attribute> call, Response<Attribute> response) {
                                if(response.isSuccessful()) {
                                    setAttributeInf(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<Attribute> call, Throwable t) {

                            }
                        });
            }
        }

        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!value.getText().toString().equals("")) {
                    int val = Integer.parseInt(value.getText().toString());
                    int res = Attribute.countModification(val);
                    modif.setText(String.valueOf(res));
                }
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAttr();
                getDialog().cancel();
            }
        });

        return view;
    }



    private void setAttributeInf(Attribute attribute) {
        name.setText(attribute.getName());
        value.setText(String.valueOf(attribute.getAmount()));
        cbIsTrained.setChecked(attribute.isTrainedSaveRoll());
    }

    private void  saveAttr() {
        Attribute attribute =  new Attribute();
        attribute.setName(name.getText().toString());
        attribute.setAmount(Integer.parseInt(value.getText().toString()));
        attribute.setTrainedSaveRoll(cbIsTrained.isChecked());

        if(attrId > 0) {
            attribute.setId(attrId);
        }

        if(attribute.checkAttribute()) {
            //all good
            ((AttributesEdit) getTargetFragment()).saveAttribute(attribute);

        } else {
            Toast.makeText(getContext(),"Invalid data",Toast.LENGTH_SHORT).show();
        }
    }
}
