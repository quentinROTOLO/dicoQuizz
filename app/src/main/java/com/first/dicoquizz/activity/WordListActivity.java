package com.first.dicoquizz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.first.dicoquizz.R;
import com.first.dicoquizz.adapter.ListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WordListActivity extends Activity {

  private ListView list_item;
  private static FirebaseFirestore firestorm;
  public ArrayList<Map<String, Object>> objectList = new ArrayList<>();
  private DatabaseReference mRef;
  private static final String TAG = "wordListView";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        // instance de firebase firestore
        this.firestorm = FirebaseFirestore.getInstance();

        ListView listViewVocabulary = (ListView) findViewById(R.id.list_item);

        int test = android.R.layout.simple_list_item_1;

       // final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, objectList);
        // adapter
        ListAdapter listViewAdapter = new ListAdapter(WordListActivity.this, objectList);
        // load adapter in listView

        listViewVocabulary.setAdapter(listViewAdapter);


/**
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String value = dataSnapshot.getValue(String.class);
                objectList.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
 */
    }
/**
    public String getReferenceLanguage(int i) {
        return (String) this.objectList.get(i).get("referenceLanguage");
    }
*/

    @Override
    protected void onStart() {
        super.onStart();
        // récupération des valeurs
        this.firestorm.collection("words")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "task: successful ");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // TODO: valeur autoIncrementable
                                Map<String, Object> dataList = new HashMap<>();
                                // Retrieve data from document and Add it to the list
                                dataList.put("referenceLanguage", document.getString("referenceLanguage"));
                                dataList.put("referenceWord", document.getString("referenceWord"));
                                dataList.put("referenceWordSynonyms", document.getString("referenceWordSynonyms"));
                                dataList.put("translationLanguage", document.getString("translationLanguage"));
                                dataList.put("translationWord", document.getString("translationWord"));
                                dataList.put("translationWordSynonyms", document.getString("translationWordSynonyms"));
                                Log.d(TAG, "wordListView: successful ");

                                objectList.add(dataList);
                                Log.d(TAG, "objectList: " + objectList);
                            }
                            // Update the adapter with the new data
                        } else {
                            // Handle task exception
                            Log.d(TAG, "wordListView: failure ");
                        }
                    }
                });
    }

}