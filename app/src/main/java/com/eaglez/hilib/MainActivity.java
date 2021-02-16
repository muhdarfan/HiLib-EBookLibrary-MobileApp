package com.eaglez.hilib;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.components.material.MyMaterial;
import com.eaglez.hilib.ui.library.MaterialFileListModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_library, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        if (!Core.LoggedIn()) {
            Toast.makeText(getApplicationContext(), "You have been logged out.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
         */
    }

    @Override
    protected void onStart() {
        super.onStart();

        Core.issuedMaterial = Core.getStore().collection("issued").whereEqualTo("borrower", Core.getUser().getUid()).addSnapshotListener((value, error) -> {
            if (value.getDocuments().size() == Core.getUserMaterial().size())
                return;

            Core.getUserMaterial().clear();
            for(DocumentSnapshot snapshot : value.getDocuments()) {
                MyMaterial file = snapshot.toObject(MyMaterial.class);

                if (Core.getSubscribedMaterial(file.getMaterialRef().getId()) == null) {
                    Core.getUserMaterial().add(file);
                }
            }

            MaterialFileListModel.update();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        Core.issuedMaterial.remove();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}