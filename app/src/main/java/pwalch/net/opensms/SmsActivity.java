package pwalch.net.opensms;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pwalch.net.opensms.adapters.ContactListAdapter;
import pwalch.net.opensms.adapters.MessageListAdapter;
import pwalch.net.opensms.storage.Storage;
import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

public class SmsActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;

    private ListView mContactsView;
    private ListView mConversationView;

    private List<Contact> mContactList;
    private List<Message> mCurrentMessageList;
    private Contact mCurrentContact;
    private Storage mStorage;

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

        try {
            mStorage = new Storage(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button button = (Button) findViewById(R.id.send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView messageToSend = (TextView) findViewById(R.id.message_to_send);
                Message message = new Message(((int) new Date().getTime()),
                        Direction.ME_TO_YOU,
                        messageToSend.getText().toString());
                messageToSend.setText("");

                sendMessage(message);
            }
        });

        hideControls(true);
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
        Log.i("tag", "Writing message to storage");
        try {
            mStorage.addMessage(mCurrentContact, messageToSend);
            loadMessageList(mCurrentContact);
            mConversationView.setSelection(mCurrentMessageList.size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String contactName = extras.getString(ContactActivity.CONTACT_NAME_EXTRA);
            String contactNumber = extras.getString(ContactActivity.CONTACT_NAME_EXTRA);

            extras.clear();
            try {
                mStorage.addContact(contactName, contactNumber);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }

        loadContactList();
    }

    private void loadContactList() {
        Log.i("tag", "Loading contact list");
        try {
            mContactList = mStorage.retrieveContactList();
            mContactsView.setAdapter(new ContactListAdapter(this, mContactList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setContact(int contactIndex) {
        Log.i("tag", "setContact called with index : " + contactIndex);
        if (mContactList != null) {
            mCurrentContact = mContactList.get(contactIndex);
            loadMessageList(mCurrentContact);
        }
    }

    private void loadMessageList(Contact contact) {
        Log.i("tag", "Loading message list");
        try {
            mCurrentMessageList = mStorage.retrieveMessageList(contact);
            mConversationView.setAdapter(new MessageListAdapter(this, mCurrentMessageList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        hideControls(false);
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
        setContact(position);
    }

    public void restoreActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.sms, menu);
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
