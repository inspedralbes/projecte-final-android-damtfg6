package com.example.recuerdate.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {
    private DocumentReference documentReferenceUsers;
    private DocumentReference documentReferenceRelatives;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        documentReferenceUsers = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReferenceRelatives = database.collection(Constants.KEY_COLLECTION_RELATIVES)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
    }

    @Override
    protected void onPause() {
        super.onPause();
        documentReferenceUsers.update(Constants.KEY_AVAILABILITY, 0);
        documentReferenceRelatives.update(Constants.KEY_AVAILABILITY, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReferenceUsers.update(Constants.KEY_AVAILABILITY, 1);
        documentReferenceRelatives.update(Constants.KEY_AVAILABILITY, 1);
    }
}
