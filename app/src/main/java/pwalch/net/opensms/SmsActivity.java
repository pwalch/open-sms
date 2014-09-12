package pwalch.net.opensms;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import pwalch.net.opensms.adapters.ContactListAdapter;
import pwalch.net.opensms.adapters.MessageListAdapter;
import pwalch.net.opensms.sms.MessageManager;
import pwalch.net.opensms.storage.Storage;
import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

public class SmsActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final static int SMS_LENGTH = 160;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private Menu mMenu;

    private ListView mContactsView;
    private ListView mConversationView;

    private List<Contact> mContactList;
    private List<Message> mCurrentMessageList;
    private Contact mCurrentContact;

    private Storage getStorage() {
        OpenSmsApplication application = (OpenSmsApplication) getApplicationContext();
        return application.getStorage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContactsView = (ListView) findViewById(R.id.navigation_drawer);

        mConversationView = (ListView) findViewById(R.id.conversation);

        Button button = (Button) findViewById(R.id.send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView messageToSend = (TextView) findViewById(R.id.message_to_send);
                final String messageText = messageToSend.getText().toString();

                if (!messageText.equals("") && messageText.length() <= SMS_LENGTH) {
                    Message message = new Message(((int) new Date().getTime()),
                            Direction.ME_TO_YOU,
                            messageText);
                    messageToSend.setText("");

                    sendMessage(message);
                } else {
                    Toast.makeText(getApplicationContext(), "Message is invalid!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadContactList();
    }

    private void hideControls(boolean isHidden) {
        int visibility = View.VISIBLE;
        if (isHidden) {
            visibility = View.INVISIBLE;
        }

        Button button = (Button) findViewById(R.id.send_button);
        button.setVisibility(visibility);

        EditText messageToSend = (EditText) findViewById(R.id.message_to_send);
        messageToSend.setVisibility(visibility);
    }

    private void sendMessage(Message messageToSend) {
        try {
            getStorage().addMessage(mCurrentContact, messageToSend);
            setContact(mCurrentContact);

            PendingIntent sentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
                                                                  new Intent("SMS_SENT"), 0);
            MessageManager.sendMessage(mCurrentContact.getPhoneNumber(),
                                        messageToSend.getText(),
                                        sentIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(ContactActivity.CONTACT_NAME_EXTRA)
                && extras.containsKey(ContactActivity.CONTACT_NUMBER_EXTRA)) {
                String contactName = extras.getString(ContactActivity.CONTACT_NAME_EXTRA);
                String contactNumber = extras.getString(ContactActivity.CONTACT_NUMBER_EXTRA);

                try {
                    getStorage().addContact(contactName, contactNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (extras.containsKey(MessageManager.CONTACT_PHONE_NUMBER_INTENT_KEY)) {
                loadContactList();
                Contact contact = MessageManager.findContactWithPhoneNumber(
                                    mContactList,
                                    extras.getString(
                                        MessageManager.CONTACT_PHONE_NUMBER_INTENT_KEY));
                if (contact != null) {
                    setContact(contact);
                }
            }
        }
    }

    private void loadContactList() {
        Log.i("tag", "Loading contact list");
        try {
            mContactList = getStorage().retrieveContactList();
            mContactsView.setAdapter(new ContactListAdapter(this, mContactList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setContactFromIndex(int contactIndex) {
        Log.i("tag", "setContact called with index : " + contactIndex);
        if (mContactList != null) {
            setContact(mContactList.get(contactIndex));
        }
    }

    private void setContact(Contact contact) {
        if (mContactList != null) {
            mCurrentContact = contact;
            loadMessageList(contact);
            setSmsBarTitle(contact.getName());
        }
    }

    private void setSmsBarTitle(String titleText) {
        getActionBar().setTitle(titleText);
    }

    private void loadMessageList(Contact contact) {
        Log.i("tag", "Loading message list");
        try {
            mCurrentMessageList = getStorage().retrieveMessageList(contact);
            mConversationView.setAdapter(new MessageListAdapter(this, mCurrentMessageList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        hideControls(false);
        if (mCurrentMessageList.size() >= 1) {
            mConversationView.setSelection(mCurrentMessageList.size() - 1);
        }
    }

    private View findViewAtPosition(ListView listView, int position) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        setContactFromIndex(position);
    }

    public void restoreActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.sms, menu);

            MenuItem editContactItem = menu.findItem(R.id.edit_contact);
            final Activity activity = this;
            editContactItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    // Edit contact name in dialog
                    return true;
                }
            });

            if (mCurrentContact == null) {
                editContactItem.setVisible(false);
            } else {
                editContactItem.setVisible(true);
            }

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sms, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

}
