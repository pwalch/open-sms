package pwalch.net.opensms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ContactActivity extends Activity {

    public static final String CONTACT_NAME_EXTRA = "contactName";
    public static final String CONTACT_NUMBER_EXTRA = "contactPhoneNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        EditText phoneNumberView = (EditText) findViewById(R.id.contact_phone_number);
        phoneNumberView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
//        phoneNumberView.addTextChangedListener(new TextWatcher() {
//            private boolean isReentring = false;
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                // Do nothing
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                // Do nothing
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (!isReentring) {
//                    isReentring = true;
//                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//                    try {
//                        Phonenumber.PhoneNumber actualPhoneNumber = phoneUtil.parse(editable.toString(),
//                                "ZZ");
//                        String formattedPhoneNumber =
//                                phoneUtil.format(actualPhoneNumber,
//                                        PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
//                        editable.clear();
//                        editable.insert(0, formattedPhoneNumber);
//                        Log.i("tag", "Formatted phone number : " + formattedPhoneNumber);
//                    } catch (NumberParseException e) {
//                        // Do nothing, not an international number
//                    }
//                    isReentring = false;
//                }
//            }
//        });

        Button button = (Button) findViewById(R.id.contact_add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText contactNameView = (EditText) findViewById(R.id.contact_name);
                EditText contactPhoneNumberView = (EditText) findViewById(R.id.contact_phone_number);

                String contactName = contactNameView.getText().toString();
                String contactPhoneNumber = contactPhoneNumberView.getText().toString();

                if (!contactName.isEmpty() && !contactPhoneNumber.isEmpty()
                        && PhoneNumberUtils.isGlobalPhoneNumber(contactPhoneNumber)) {
                    Intent intent = new Intent(getBaseContext(), SmsActivity.class);
                    intent.putExtra(CONTACT_NAME_EXTRA, contactName);
                    intent.putExtra(CONTACT_NUMBER_EXTRA, contactPhoneNumber);

                    contactNameView.setText("");
                    contactPhoneNumberView.setText("");

                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Contact is invalid!",
                                   Toast.LENGTH_SHORT).show();
                }
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
