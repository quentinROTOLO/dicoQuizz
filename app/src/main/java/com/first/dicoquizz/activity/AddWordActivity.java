package com.first.dicoquizz.activity;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.first.dicoquizz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;
import java.util.Map;

public class AddWordActivity extends Activity {

    private FirebaseFirestore firestorm;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText simpleEditTextReferenceLang;
    private EditText simpleEditTextReferenceWord;
    private EditText simpleEditTextReferenceWordSynonyms;
    private EditText simpleEditTextTranslationLang;
    private EditText simpleEditTextTranslatedWord;
    private EditText simpleEditTextTranslatedWordSynonyms;
    private Button buttonAddWordToMyList;
    private Button buttonBackToHomeActivity;
    private TextView textViewEnterWords;


    private static final String CHANNEL_ID = "addWordChannel";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        this.simpleEditTextReferenceLang = findViewById(R.id.simpleEditTextReferenceLang);
        this.simpleEditTextReferenceWord = findViewById(R.id.simpleEditTextReferenceWord);
        this.simpleEditTextReferenceWordSynonyms = findViewById(R.id.simpleEditTextReferenceWordSynonyms);
        this.simpleEditTextTranslationLang = findViewById(R.id.simpleEditTextTranslationLang);
        this.simpleEditTextTranslatedWord = findViewById(R.id.simpleEditTextTranslatedWord);
        this.simpleEditTextTranslatedWordSynonyms = findViewById(R.id.simpleEditTextTranslatedWordSynonyms);
        this.buttonAddWordToMyList = findViewById(R.id.buttonAddWordToMyList);
        this.buttonBackToHomeActivity = findViewById(R.id.buttonBackToHomeActivity);
        this.textViewEnterWords = findViewById(R.id.textViewEnterWords);
        // récupération de l'utilisateur courant
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();

        // singleton de connexion à firebase
        firestorm = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        // abonnement aux clicks event des buttons
        onClickBackToConnectionActivity();
        this.buttonAddWordToMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWordIntoDb();
            }
        });

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Add word channel",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createWordIntoDb() {
        // stockage des paramètres entrés
        String referenceLang = this.simpleEditTextReferenceLang.getText().toString().trim();
        String translationLang = this.simpleEditTextTranslationLang.getText().toString().trim();
        String referenceWord = this.simpleEditTextReferenceWord.getText().toString().trim();
        String referenceWordSynonyms = this.simpleEditTextReferenceWordSynonyms.getText().toString().trim();
        String translatedWord = this.simpleEditTextTranslatedWord.getText().toString().trim();
        String translatedWordSynonyms = this.simpleEditTextTranslatedWordSynonyms.getText().toString().trim();

        if (TextUtils.isEmpty(referenceLang) || TextUtils.isEmpty(translationLang) || TextUtils.isEmpty(referenceWord) || TextUtils.isEmpty(translatedWord)) {
            String messageError = getString(R.string.editTextMustNotBeEmpty);
            Toast.makeText(AddWordActivity.this, messageError, Toast.LENGTH_SHORT).show();
            return;
        }

        // creating user object
        Map<String, Object> word = new HashMap<>();
        word.put("validated", false);
        word.put("referenceLanguage:", referenceLang);
        word.put("referenceWord:", referenceWord);
        word.put("referenceWordSynonyms", referenceWordSynonyms);
        word.put("translationLanguage", translationLang);
        word.put("translatedWord", translatedWord);
        word.put("translatedWordSynonyms", translatedWordSynonyms);

        firestorm.collection("words").add(word)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Sign in success
                        Log.d("saveWordIntoDb:success", "view: AddWordActivity");
                        String message = getString(R.string.wordSuccessfullyRegistered);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    String message = getString(R.string.errorWordRegistration);
                    Toast.makeText(AddWordActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.d("saveWordIntoDb:failure", "view: AddWordActivity");
                }
        });
    }

    public void onClickBackToConnectionActivity() {
        this.buttonBackToHomeActivity.setOnClickListener(v -> {
            Intent intent = new Intent(AddWordActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

    }


}