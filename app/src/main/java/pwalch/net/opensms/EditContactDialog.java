package pwalch.net.opensms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import pwalch.net.opensms.storage.XmlParser;
import pwalch.net.opensms.structures.Contact;

/**
 * Created by pierre on 13.09.14.
 */
public class EditContactDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.edit_contact)
                .setPositiveButton(R.string.confirm_edit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancel_edit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog dialog = builder.create();
        final EditText input = new EditText(dialog.getContext());
        input.setMinLines(1);
        input.setTextColor(Color.BLACK);

        String contactName = getArguments().getString(ContactActivity.CONTACT_NAME_EXTRA);
        input.setText(contactName);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        dialog.setView(input);

        return dialog;
    }
}
