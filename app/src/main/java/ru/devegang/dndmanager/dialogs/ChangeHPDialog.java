package ru.devegang.dndmanager.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.character.CharacterInfoFull;
import ru.devegang.dndmanager.networking.NetworkService;

public class ChangeHPDialog extends DialogFragment {


    SeekBar seekBarHP;
    TextView textViewHP;

    ImageButton ok;
    ImageButton cancel;

    int fullHp = 0;
    int curHp = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_change_hp,container,false);

        seekBarHP = (SeekBar) view.findViewById(R.id.sbCHP);
        textViewHP = (TextView) view.findViewById(R.id.tvCHP);
        ok = (ImageButton) view.findViewById(R.id.ibCHPok);
        cancel = (ImageButton) view.findViewById(R.id.ibCHPcancel);

        if (getTag() != null) {
            String inf[] = getTag().split("/");

            curHp = Integer.parseInt(inf[0]);
            fullHp = Integer.parseInt(inf[1]);

            seekBarHP.setMax(fullHp);
            seekBarHP.setProgress(curHp);
            String val = curHp +"/" +fullHp;
            textViewHP.setText(val);

        }
        seekBarHP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                curHp = i;
                String val = curHp + "/" +fullHp;
                {
                    textViewHP.setText(val);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
                long character_id = preferences.getLong("CharacterID", -1);
                if(character_id != -1) {
                    NetworkService.getInstance()
                            .getRestCharacterAPIv2()
                            .changeHp(character_id,curHp)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()) {
//                                      updateHP();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });

                    updateHP();
                }
            }
        });

        return view;

    }

    private  void updateHP() {
        ((CharacterInfoFull)getTargetFragment()).setupNewHP(curHp);
        getDialog().cancel();
    }
}
