package com.example.assignment2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

//Used to popout the dialog for update information
public class Dialog extends AppCompatDialogFragment {
    EditText newAddress;
    DialogListener listener;
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity(). getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        newAddress = view.findViewById(R.id.newAddressInput);

        builder.setView(view)
                .setTitle("Enter the new updated address")
                //cancel button
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                })
                //update button
                .setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String updatedAddress = newAddress.getText().toString();
                        listener.text(updatedAddress); //calls the text method to update the database
                    }
                });


        return builder.create();

    }

    public void onAttach (Context context){
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }

    public interface DialogListener{
        void text(String newAddress);
    }
}
