package ru.devegang.dndmanager.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.devegang.dndmanager.R;

public class RollResultDialog extends DialogFragment {

    TextView tvResult;
    Button okButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_save_roll_result, container, false);

        tvResult = (TextView) view.findViewById(R.id.tvSaveRollResult);
        okButton = (Button) view.findViewById(R.id.bSRROK);

        tvResult.setText(getTag());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        return view;
    }

}
