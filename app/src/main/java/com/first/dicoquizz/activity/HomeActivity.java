
package com.first.dicoquizz.activity;

import static com.google.firebase.auth.FirebaseAuth.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.first.dicoquizz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HomeActivity extends Activity {

    private Button buttonDisconnect;
    private Button buttonAddTranslations;
    private Button buttonAddFriends;
    private Button buttonShowTranslationList;
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore firestorm;
    private static FirebaseUser currentUser;
    private static final String TAG = "DisconnectUser";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.buttonDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        this.buttonAddTranslations = (Button) findViewById(R.id.addTranslations);
        this.buttonAddFriends = (Button) findViewById(R.id.addFriends);
        this.buttonShowTranslationList = (Button) findViewById(R.id.showTranslationList);

        // instanciation de FirebaseFirestore
        this.firestorm = FirebaseFirestore.getInstance();
        // instanciation de Firebase Auth
        this.mAuth = getInstance();
        // intialsation de FirebaseUser
        this.currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onStart() {
        super.onStart();

        this.buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnection();
            }
        });

        this.buttonAddTranslations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddWordActivity.class);
                startActivity(intent);
            }
        });

        this.buttonAddFriends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FriendListActivity.class);
                startActivity(intent);
            }
        });

        this.buttonShowTranslationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, WordListActivity.class);
                startActivity(intent);
            }
        });

    }

    // à mettre dans un service
    public FirebaseUser getUserStatus() {
        return mAuth.getCurrentUser();
    }

    public void disconnection() {
        mAuth.signOut();
        if (getUserStatus() == null) {
            Log.d(TAG, "userDisconnection: successful");
            String message = getString(R.string.disconnexionSuccesfull);
            Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();

            // User is signed out, update UI accordingly
            Intent intent = new Intent(HomeActivity.this, ConnectionActivity.class);
            startActivity(intent);
        } else {
            Log.d(TAG, "userDisconnection: failure");
            String message = getString(R.string.errorDisconnection);
            Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
