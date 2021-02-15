package com.eaglez.hilib.components;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.eaglez.hilib.components.material.MyMaterial;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class Core {
    private static Core instance = null;
    private static Context context = null;

    public static ListenerRegistration issuedMaterial;
    public static GoogleSignInClient mGoogleSignInClient;
    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private static FirebaseFirestore store;
    private static FirebaseUser user;
    private static Users UserData = null;
    private static ArrayList<MyMaterial> UserMaterial;

    public Core(Context applicationContext) {
        context = applicationContext;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        store = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        UserMaterial = new ArrayList<>();

        auth.addAuthStateListener(firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();

            if (user != null) {
                database.getReference("users").child(user.getUid()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserData = task.getResult().getValue(Users.class);
                    } else {
                        Log.v("asd", "apa lanxiao");
                    }
                });
            }
        });
    }

    public static void initialize(Context applicationContext) {
        if (instance == null)
            instance = new Core(applicationContext);
    }

    public static boolean LoggedIn() {
        return (user != null);
    }

    public static void setUser(FirebaseUser user) {
        Core.user = user;
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static FirebaseUser getUser() {
        return user;
    }

    public static Users getUserData() {
        return UserData;
    }

    public static FirebaseFirestore getStore() {
        return store;
    }

    public static Core getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public static ArrayList<MyMaterial> getUserMaterial() {
        return UserMaterial;
    }

    public static void setUserMaterial(ArrayList<MyMaterial> userMaterial) {
        UserMaterial = userMaterial;
    }

    public static MyMaterial getSubscribedMaterial(String materialID) {
        for (MyMaterial m : UserMaterial)
            if (m.getMaterialRef().getId().equals(materialID) && m.getBorrower().equals(user.getUid()))
                return m;

        return null;
    }
}
