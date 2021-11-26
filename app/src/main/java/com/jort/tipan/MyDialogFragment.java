package com.jort.tipan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
    String argMsg;

    public void setTextToPass(String msg) {this.argMsg = msg;}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage("Your appointment reference number is " + argMsg + ". An SMS text message will be sent to your provided mobile number. Thanks");
//        dlgAlert.setTitle("App Title");
        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                //dismiss the dialog
            }
        });
        return dlgAlert.create();
    }
}
