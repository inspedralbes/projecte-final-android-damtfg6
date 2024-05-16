package com.example.recuerdate.utilities;

import java.util.HashMap;

public class Constants {
    public static final String  KEY_COLLECTION_USERS ="users";
    public static final String KEY_COLLECTION_STREETS = "streets";
    public static final String  KEY_NAME ="name";
    public static final String KEY_USER_IDENTIFIER = "usuari_identificador";
    public static final String KEY_SUPERVISED_USER_DNI = "supervised_user_dni";
    public static final String KEY_SUPERVISED_USER_NAME = "supervised_user_name";
    public static final String  KEY_EMAIL ="dni";
    public static final String  KEY_PHONE ="phone";
    public static final String  KEY_ROLE ="role";
    public static final String  KEY_PASSWORD ="password";
    public static final String  KEY_PREFERENCE_NAME ="recuerdatePreference";
    public static final String  KEY_IS_SIGNED_IN ="isSignedIn";

    public static final String  KEY_USER_ID ="userId";

    public static final String  KEY_IMAGE ="image";
    public static final String KEY_COLLECTION_RELATIVES = "relatives";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER="user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE="message";
    public static final String KEY_TIMESTAMP="timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS="conversations";
    public static final String KEY_SENDER_NAME="senderName";
    public static final String KEY_RECEIVER_NAME="receiverName";
    public static final String KEY_SENDER_IMAGE="senderImage";
    public static final String KEY_RECEIVER_IMAGE="receiverImage";
    public static final String KEY_LAST_MESSAGE="lastMessage";
    public static final String KEY_AVAILABILITY="availability";
    public static final String REMOTE_MSG_AUTHORIZATION="Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE="Content-Type";
    public static final String REMOTE_MSG_DATA="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";

    public static HashMap<String , String> remoteMsgHeaders = null;
    public static HashMap<String, String>  getRemoteMsgHeaders(){
        if(remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAO157qr8:APA91bHKFGS87XEyhGfSjGVeWIjH9NmqI8or8cKfsydmkXSB3pR_1w_3zHEGzOabJ3fR8GogWKswidy7OTy5sZBowrTc2-lfMWq0bn4HsAHCi_mb3Nag-OCsZ_sAzGaZ2HsCSuC3pHBe"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }
}
