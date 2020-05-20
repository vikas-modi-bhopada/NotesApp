package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleActivity extends AppCompatActivity {

    private static final String TAG = "ExampleActivity";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
    }

    public void createDocument(View view) {
        Map<String, Object> map = new HashMap<>();
        map.put("text","i wanna play");
        map.put("isCompleted","true");
        map.put("created",new Timestamp(new Date()));
        
        firestore.collection("notes").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"onSuccess: task was successful");
                        Log.d(TAG,"onSUcess: "+documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Onfailer: task was notsuccessful");
                    }
                });
    }

    public void readDocument(View view) {
        firestore.collection("notes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG,"onSuccess: we re getting the data");
                       List<DocumentSnapshot> snapshots =  queryDocumentSnapshots.getDocuments();
                       for (DocumentSnapshot snapshot :snapshots)
                       {
                           Log.d(TAG,snapshot.getData().toString());
                       }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure: "+e);
                    }
                });
        /*firestore.collection("notes")
                .whereEqualTo("isCompleted","true")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG,"onSuccess: we re getting the data");
                        List<DocumentSnapshot> snapshots =  queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot :snapshots)
                        {
                            Log.d(TAG,snapshot.getData().toString());
                            Log.d(TAG,snapshot.getString("text"));
                            Log.d(TAG,snapshot.getString("isCompleted"));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure: "+e);
                    }
                });*/

       /* firestore.collection("notes")
                .orderBy("isCompleted")
                //.orderBy("isCompleted", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG,"onSuccess: we re getting the data");
                        List<DocumentSnapshot> snapshots =  queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot :snapshots)
                        {
                            Log.d(TAG,snapshot.getData().toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure: "+e);
                    }
                });*/
    }

    public void getAlldocumentwithrealtimeupdate(View view) {
        firestore.collection("notes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null)
                        {
                            Log.e(TAG,"onEvent: "+e);
                        }
                        if (queryDocumentSnapshots != null)
                        {
                            Log.d(TAG,"---------------------------");
                            //List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            //for (DocumentSnapshot snapshot:snapshotList)
                            //{
                             //   Log.d(TAG,"onEvent: "+snapshot.getData());
                            //}

                            List<DocumentChange> documentChangeList = queryDocumentSnapshots.getDocumentChanges();
                            for (DocumentChange documentChange:documentChangeList)
                            {
                                Log.d(TAG,"onEvent: "+documentChange.getDocument().getData());
                            }
                        }
                        else
                        {
                            Log.e(TAG,"onEvent: query snapshot was null");
                        }
                    }
                });
    }

    public void updateDocument(View view) {
        DocumentReference documentReference = firestore.collection("notes")
                .document("IvcMft0SY9z01BqAg7Qg");
        Map<String, Object> map = new HashMap<>();
       // map.put("text","vikas");
        map.put("movie", FieldValue.delete());

        documentReference.update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"onSuccess: update");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onfail: "+e);
                    }
                });

       /*documentReference.set(map)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Log.e(TAG,"onSuccess: update");
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.e(TAG,"onfail: "+e);
                   }
               });*/
    }
}
