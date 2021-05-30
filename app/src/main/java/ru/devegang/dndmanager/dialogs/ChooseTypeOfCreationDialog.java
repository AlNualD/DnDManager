package ru.devegang.dndmanager.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.character.CharacterActivity;
import ru.devegang.dndmanager.main_fragments.CharactersList;

public class ChooseTypeOfCreationDialog extends DialogFragment {
    String title = "Выберите шаблон";
    String message = "Создать простого персонажа или использовать базовые правила DnD?";
    String button1str = "Использовать шаблон";
    String   button2str = "Обычное создание";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(button1str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((CharacterActivity) getActivity()).useTemplateClicked();
                    }
                });
        builder.setNegativeButton(button2str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((CharacterActivity) getActivity()).simpleCreationClicked();
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }
}
