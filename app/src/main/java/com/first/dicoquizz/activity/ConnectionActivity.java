package com.first.dicoquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.first.dicoquizz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.AuthResult;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ConnectionActivity extends Activity {

    private static final String CHANNEL_ID = "connectToAccountChannel";
    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonCreateAccount;
    private Button buttonConnect;
    private FirebaseFirestore firestorm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        // chargement des éléments du layout avec les attributs
        // chargement des boutons de connection/création de compte
        this.buttonConnect = findViewById(R.id.buttonConnect);
        this.buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        // chargement des layout editText
        this.editTextEmail = findViewById(R.id.simpleEditTextEmail);
        this.editTextPassword = findViewById(R.id.simpleEditTextPwd);

        // pattern singleton pour connexion à la bdd
        firestorm = FirebaseFirestore.getInstance();
        // pattern singleton pour authentification utilisateur
        mAuth = FirebaseAuth.getInstance();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // abonnement aux event click sur button createAccount et connection
        // tests
        this.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnectionActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        this.buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyConnection();
            }
        });
    }
    // [END on_start_check_user]

    // a mettre dans un service
    public boolean isUserConnected() {
        return mAuth.getCurrentUser() != null;
    }

    public void createAccountButtonClick(View view) {
            System.out.println(ConnectionActivity.this);
            System.out.println(CreateAccountActivity.class);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in, update UI accordingly
            Intent intent = new Intent(ConnectionActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            // User is not signed in, update UI accordingly
            String message = getString(R.string.noUserConnected);
            Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();
        }
    }

    public void verifyConnection() {
        // récupération des paramètres entrés
        String enteredEmail = this.editTextEmail.getText().toString().trim();
        String enteredPassword = this.editTextPassword.getText().toString().trim();

        Log.d(TAG, "email: " + enteredEmail);
        Log.d(TAG, "pwd: " + enteredPassword);
        Log.d(TAG, "firestorm: " + firestorm);
        Log.d(TAG, "mAuth: " + mAuth);

        if (mAuth == null) {
           // this.mAuth = FirebaseAuth.getInstance();
        }
        // Validate email and password
        if (TextUtils.isEmpty(enteredEmail)) {
            editTextEmail.setError("Email is required."); // faire appel à classe R
            return;
        }
        if (TextUtils.isEmpty(editTextPassword.getText())) {
            editTextPassword.setError("Password is required."); // faire appel à classe R
            return;
        }

        // connexion avec email et mot de passe, méthode de l'API Firebase
        mAuth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(ConnectionActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User is signed in
                            Log.d(TAG, "signInWithEmail:success");
                            // notification de succès de connexion
                            String message = getString(R.string.connectionSuccess);
                            Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Update UI with the signed-in user's information
                            updateUI(user);
                        } else {
                            // Sign in failed
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            // notification d'échec de connexion
                            String message = getString(R.string.errorConnectionEmailAndPasswordEnteredAreFalse);
                            Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();
                            // Update UI with the error message
                            updateUI(null);
                        }
                    }
                });
    }

}
