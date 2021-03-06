package pwalch.net.opensms.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import pwalch.net.opensms.R;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 31.07.14.
 */
public class MessageListAdapter extends ArrayAdapter<Message> {

    public MessageListAdapter(Context context, List<Message> messageList) {
        super(context, R.layout.message_text, messageList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(getContext()).inflate(R.layout.message_text, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.message_text);

        Message message = getItem(position);
        textView.setText(message.getText());

        switch (message.getDirection()) {
            case ME_TO_YOU: {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.RED);
                LinearLayout layout = (LinearLayout) textView.getParent();
                layout.setGravity(Gravity.RIGHT);
                break;
            }

            case YOU_TO_ME: {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.BLUE);
                LinearLayout layout = (LinearLayout) textView.getParent();
                layout.setGravity(Gravity.LEFT);
                break;
            }
        }

        return rowView;
    }

}
