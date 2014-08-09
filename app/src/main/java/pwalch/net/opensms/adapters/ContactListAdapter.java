package pwalch.net.opensms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pwalch.net.opensms.R;
import pwalch.net.opensms.structures.Contact;

/**
 * Created by pierre on 02/08/14.
 */
public class ContactListAdapter extends ArrayAdapter<Contact> {
    public ContactListAdapter(Context context, List<Contact> contactList) {
        super(context, R.layout.contact_text, contactList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(getContext()).inflate(
                R.layout.contact_text, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.contact_text_view);
        Contact contact = getItem(position);
        textView.setText(contact.getName());

        return rowView;
    }
}