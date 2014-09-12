package pwalch.net.opensms.sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pwalch.net.opensms.OpenSmsApplication;
import pwalch.net.opensms.R;
import pwalch.net.opensms.SmsActivity;
import pwalch.net.opensms.storage.Storage;
import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 11.09.14.
 */
public class MessageManager extends BroadcastReceiver {
    private static SmsManager mSmsManager = SmsManager.getDefault();

    public static final String CONTACT_PHONE_NUMBER_INTENT_KEY = "contactPhoneNumber";

    public static void sendMessage(String phoneNumber, String text, PendingIntent sentIntent) {
        Log.i("tag", "Sending SMS to : " + phoneNumber);
        mSmsManager.sendTextMessage(phoneNumber, null, text, sentIntent, null);
    }

    private Storage getStorage(Context context) {
        OpenSmsApplication application = (OpenSmsApplication) context.getApplicationContext();
        return application.getStorage();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            List<SmsMessage> smsList = generateSmsList(bundle);
            try {
                Storage storage = getStorage(context);
                List<Contact> contactList = storage.retrieveContactList();
                for (SmsMessage smsMessage : smsList) {
                    String originPhoneNumber = smsMessage.getOriginatingAddress();

                    Contact contact = findContactWithPhoneNumber(contactList, originPhoneNumber);
                    if (contact == null) {
                        storage.addContact("Unknown", originPhoneNumber);
                        contactList = storage.retrieveContactList();
                        contact = findContactWithPhoneNumber(contactList, originPhoneNumber);
                    }

                    storage.addMessage(contact, new Message((int)(new Date()).getTime(),
                                                            Direction.YOU_TO_ME,
                                                            smsMessage.getMessageBody()));

                    emitNotification(context, contact);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void emitNotification(Context context, Contact contact) {
        Intent resultIntent = new Intent(context, SmsActivity.class);
        resultIntent.putExtra(CONTACT_PHONE_NUMBER_INTENT_KEY, contact.getPhoneNumber());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(SmsActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("OpenSMS")
                        .setContentText("Received an SMS.")
                        .setContentIntent(resultPendingIntent);
        Notification notification = builder.build();

        notification.flags = Notification.FLAG_AUTO_CANCEL | notification.flags;

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public static Contact findContactWithPhoneNumber(List<Contact> contactList,
                                                     String phoneNumber) {
        for (int i = 0; i < contactList.size(); ++i) {
            Contact currentContact = contactList.get(i);
            if (currentContact.getPhoneNumber().equals(phoneNumber)) {
                return currentContact;
            }
        }
        return null;
    }

    private static List<SmsMessage> generateSmsList(Bundle bundle) {
        Object[] messageBytesArray = (Object[]) bundle.get("pdus");
        List<SmsMessage> smsList = new ArrayList<SmsMessage>();
        for (int i = 0; i < messageBytesArray.length; ++i) {
            smsList.add(SmsMessage.createFromPdu((byte[])messageBytesArray[i]));
        }
        return smsList;
    }
}
