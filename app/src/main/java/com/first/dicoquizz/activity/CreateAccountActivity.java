package com.first.dicoquizz.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.first.dicoquizz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestorm;
    private EditText simpleEditTextFirstnameDefinition;
    private EditText simpleEditTextPwdDefinition;
    private EditText simpleEditTextPwdDefConfirmation;
    private EditText simpleEditTextEmailAddressDefinition;
    private Button buttonCreateMyAccount;
    private Button buttonBackToConnectionActivity;
    private Boolean emailEnteredAlreadyExist = false;
    private static final String CHANNEL_ID = "createAccountChannel";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        this.simpleEditTextEmailAddressDefinition = findViewById(R.id.simpleEditTextEmailAddressDefinition);
        this.simpleEditTextPwdDefinition = findViewById(R.id.simpleEditTextPwdDefinition);
        this.simpleEditTextPwdDefConfirmation =  findViewById(R.id.simpleEditTextPwdDefConfirmation);
        this.simpleEditTextFirstnameDefinition = findViewById(R.id.simpleEditTextFirstnameDefinition);
        // chargement des buttons
        this.buttonCreateMyAccount =  findViewById(R.id.buttonCreateMyAccount);
        this.buttonBackToConnectionActivity = findViewById(R.id.buttonBackToConnectionActivity);

        // singleton de connexion à firebase
        firestorm = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        String message = getString(R.string.passwordWith6Charaters);
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // abonnement aux clicks event des buttons
        onClickBackToConnectionActivity();
        this.buttonCreateMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Account creation channel",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getEmailAddressDefinition() {
        return this.simpleEditTextEmailAddressDefinition.getText().toString();
    }

    public String getPwdDefinition() {
        return this.simpleEditTextPwdDefinition.getText().toString();
    }

    public String getPwdDefConfirmation() {
        return this.simpleEditTextPwdDefConfirmation.getText().toString();
    }

    private String getFirstnameDefinition() {
        return this.simpleEditTextFirstnameDefinition.getText().toString();
    }

    private void createUser() {
        String emailDefined = this.getEmailAddressDefinition();
        String passwordDefined = this.getPwdDefinition();

        mAuth.createUserWithEmailAndPassword(emailDefined, passwordDefined)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User account created successfully
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("TAG", "User account created with email: " + user.getEmail());
                        // appel à l'ajout de l'utilisateur dans la bdd
                        createUserIntoDb();
                    } else {
                        // Failed to create user account
                        Log.e("TAG", "Failed to create user account", task.getException());
                        // notification
                        String message = getString(R.string.errorAccountCreatedPleaseTryAgain);
                        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT).show();
                        Log.d("createUserWithEmail:failure", "view: createAccountActivity");
                    }
                });

    }

    private void createUserIntoDb() {
        // stockage des paramètres entrés
        String emailDefined = this.getEmailAddressDefinition();
        String passwordDefined = this.getPwdDefinition();
        String firstnameDefined = this.getFirstnameDefinition();


            String message = getString(R.string.userAccountCreation);
            Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT).show();

                // creating user object
                Map<String, Object> users = new HashMap<>();
                users.put("admin", false);
                users.put("mail", emailDefined);
                users.put("firstname", firstnameDefined);
                users.put("password", passwordDefined);

                firestorm.collection("users").add(users)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Sign in success
                                Log.d("createUserWithEmail:success", "view: createAccountActivity");
                                // notification sur IHM succès creation de compte
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                        .setContentTitle("Compte crée avec succès")
                                        .setContentText("Vous allez maintenant découvrir DicoQuizz")
                                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                                // Display the notification
                                // affichage notification
                                int notificationId = 1;
                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                notificationManager.notify(notificationId, builder.build());
                                String message = getString(R.string.accountSuccessfullyCreated);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                // timeout pour passer à la vue HomeActivity
                                new android.os.Handler(Looper.getMainLooper()).postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                Log.i("tag: AccountCreated", "This'll run 3000 milliseconds later");
                                                // intent pour aller sur HomeActivity
                                                Intent intentAccountCreated = new Intent(CreateAccountActivity.this, HomeActivity.class);
                                                startActivity(intentAccountCreated);
                                                finish();
                                            }
                                        },
                                        2000);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        String message = getString(R.string.errorAccountCreatedPleaseTryAgain);
                        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT).show();
                        Log.d("createUserWithEmail:failure", "view: createAccountActivity");
                    }
                });
            }

    public void onClickBackToConnectionActivity() {
        this.buttonBackToConnectionActivity.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccountActivity.this, ConnectionActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
