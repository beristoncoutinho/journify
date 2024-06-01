package com.example.journify;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser curreUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").document(curreUser.getUid()).collection("my_notes");

    }
    static String timeStampToString(Timestamp timestamp){
       return new SimpleDateFormat("MM/dd/yyy").format(timestamp.toDate());
    }
}
