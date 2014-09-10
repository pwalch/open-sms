package pwalch.net.opensms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pwalch.net.opensms.structures.Contact;


public class ContactActivity extends Activity {

    public static final String CONTACT_NAME_EXTRA = "contactName";
    public static final String CONTACT_NUMBER_EXTRA = "contactPhoneNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Button button = (Button) findViewById(R.id.contact_add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText contactNameView = (EditText) findViewById(R.id.contact_name);
                EditText contactNumberView = (EditText) findViewById(R.id.contact_number);

                Intent intent = new Intent(getBaseContext(), SmsActivity.class);
                intent.putExtra(CONTACT_NAME_EXTRA, contactNameView.getText().toString());
                intent.putExtra(CONTACT_NUMBER_EXTRA, contactNumberView.getText().toString());

                contactNameView.setText("");
                contactNumberView.setText("");

                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
