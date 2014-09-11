package pwalch.net.opensms.sms;

import android.app.Notification;
import android.app.NotificationManager;
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

import pwalch.net.opensms.R;
import pwalch.net.opensms.storage.Storage;
import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 11.09.14.
 */
public class MessageManager extends BroadcastReceiver {
    private static SmsManager mSmsManager = SmsManager.getDefault();

    public static void sendMessage(Context context, String phoneNumber, String text) {
        // PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
        // PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);
        Log.i("tag", "Sending SMS to : " + phoneNumber);
        mSmsManager.sendTextMessage(phoneNumber, null, text, null, null);
    }

    private List<SmsMessage> generateSmsList(Bundle bundle) {
        Object[] messageBytesArray = (Object[]) bundle.get("pdus");
        List<SmsMessage> smsList = new ArrayList<SmsMessage>();
        for (int i = 0; i < messageBytesArray.length; ++i) {
            smsList.add(SmsMessage.createFromPdu((byte[])messageBytesArray[i]));
        }
        return smsList;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            List<SmsMessage> smsList = generateSmsList(bundle);
            try {
                Storage storage = new Storage(context);

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

                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle("OpenSMS")
                                    .setContentText("Received an SMS.");
                    notificationManager.notify(0, builder.build());
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
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
}
