package com.example.a0411_0127122_t2_v3usafe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChangeInfoDialog extends AppCompatDialogFragment {
    private TextInputEditText txtPw, txtInput;
    private TextInputLayout tvInput;
    private UsernameDialogueInputListener userNameListener;
    private PasswordDialogueInputListener pwListener;
    private String title;

    public ChangeInfoDialog(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialogue_input, null);

        txtInput = view.findViewById(R.id.txt_input);
        txtPw = view.findViewById(R.id.txt_password);
        tvInput = view.findViewById(R.id.tv_input);

        String t = "Change " + this.title;
        String prompt = "Enter a New " + this.title;
        tvInput.setHint(prompt);

        if(this.title == "Username") {
            builder.setView(view)
                    .setTitle(t)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String input = txtInput.getText().toString();
                            String pw = txtPw.getText().toString();
                            userNameListener.applyUserName(input, pw);
                        }
                    });
        }else{
            builder.setView(view)
                    .setTitle(t)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String input = txtInput.getText().toString();
                            String pw = txtPw.getText().toString();
                            pwListener.applyPw(input, pw);
                        }
                    });
        }

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            userNameListener = (UsernameDialogueInputListener) context;
            pwListener = (PasswordDialogueInputListener) context;

        }catch (ClassCastException e1) {
            throw new ClassCastException(context.toString() +
                        "must implement DialogueInputListener");
        }
    }

    public interface UsernameDialogueInputListener{
        void applyUserName(String input, String pw);
    }

    public interface PasswordDialogueInputListener{
        void applyPw(String input, String pw);
    }
}
