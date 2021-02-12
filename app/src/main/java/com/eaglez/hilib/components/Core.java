package com.eaglez.hilib.components;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Core {
    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private static FirebaseFirestore store;
    private static Users user = null;

    public Core() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        store = FirebaseFirestore.getInstance();

        auth.addAuthStateListener(firebaseAuth -> user = new Users(firebaseAuth.getCurrentUser()));
    }

    public static boolean LoggedIn() {
        return (user != null);
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static FirebaseFirestore getStore() {
        return store;
    }
}
