package com.eaglez.hilib.components;

import android.content.Context;
import android.util.Log;

import com.eaglez.hilib.components.material.MyMaterial;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private static StorageReference storage;
    private static Users UserData = null;
    private static ArrayList<MyMaterial> UserMaterial;

    public Core(Context applicationContext) {
        context = applicationContext;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        store = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        user = auth.getCurrentUser();
        UserMaterial = new ArrayList<>();

        auth.addAuthStateListener(firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();

            if (user != null) {
                database.getReference("users").child(user.getUid()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserData = task.getResult().getValue(Users.class);
                    } else {
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

    public static StorageReference getStorage() {
        return storage;
    }

    public static void setStorage(StorageReference storage) {
        Core.storage = storage;
    }

    public static void setUserMaterial(ArrayList<MyMaterial> userMaterial) {
        UserMaterial = userMaterial;
    }

    public static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static MyMaterial getSubscribedMaterial(String materialID) {
        for (MyMaterial m : UserMaterial)
            if (m.getMaterialRef().getId().equals(materialID) && m.getBorrower().equals(user.getUid()))
                return m;

        return null;
    }
}
