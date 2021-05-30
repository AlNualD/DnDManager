package ru.devegang.dndmanager.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.rolls.Modes;
import ru.devegang.dndmanager.entities.rolls.RollingFormula;
import ru.devegang.dndmanager.entities.rolls.RollingResult;
import ru.devegang.dndmanager.networking.NetworkService;

public class MakeRollDialog extends DialogFragment {


    EditText dicesAmount;
    EditText dice;
    EditText modif;

    Spinner spinnerModes;

    TextView textViewResult;

    Button bRoll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_make_roll, container,false);

        dicesAmount = (EditText) view.findViewById(R.id.etMRDicesAmount);
        dice = (EditText) view.findViewById(R.id.etMRDice);
        modif = (EditText) view.findViewById(R.id.etMRModif);
        spinnerModes = (Spinner) view.findViewById(R.id.sMRMode);
        spinnerModes.setSelection(1);

        textViewResult = (TextView) view.findViewById(R.id.tvMRResult);
        textViewResult.setVisibility(View.GONE);

        bRoll = (Button) view.findViewById(R.id.bMRRoll);
        bRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewResult.setText(R.string.loading_str);
                textViewResult.setVisibility(View.VISIBLE);
                Modes mod = Modes.modByInt(spinnerModes.getSelectedItemPosition() - 1);
                int diceA = Integer.parseInt(dicesAmount.getText().toString());
                int diceCur = Integer.parseInt(dice.getText().toString());
                int modifCur = Integer.parseInt(modif.getText().toString());
                RollingFormula formula = new RollingFormula(mod,diceA,diceCur,modifCur);

                NetworkService.getInstance()
                        .getRestRollingAPIv2()
                        .simpleRoll(formula.toString()).enqueue(new Callback<RollingResult>() {
                    @Override
                    public void onResponse(Call<RollingResult> call, Response<RollingResult> response) {
                        if(response.isSuccessful()) {
                            textViewResult.setText(String.valueOf(response.body().getResult()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RollingResult> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }
}
