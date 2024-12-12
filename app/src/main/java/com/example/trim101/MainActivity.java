package com.example.trim101;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        private RecyclerView recyclerView;
        private BarberAdapter barberAdapter;
        private List<Barber> barberList;
        private FirebaseFirestore db;
        private DrawerLayout drawerLayout;
        private NavigationView navigationView;
        private Toolbar toolbar;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                // Initialize views
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                barberList = new ArrayList<>();
                barberAdapter = new BarberAdapter(this, barberList);
                recyclerView.setAdapter(barberAdapter);

                toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                db = FirebaseFirestore.getInstance();

                drawerLayout = findViewById(R.id.drawer_layout);
                navigationView = findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();

                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                recyclerView.setLayoutManager(gridLayoutManager);

                // Retrieve barbers from Firestore
                db.collection("Users").whereEqualTo("roles", "Barber").get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        String username = document.getString("username");
                                        String barberId = document.getId();  // Retrieve barber's document ID

                                        Barber barber = new Barber(barberId, username);
                                        barberList.add(barber);
                                }
                                barberAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error retrieving barbers", e));

                // Handle item click to open BarberDetailActivity
                barberAdapter.setOnItemClickListener(barber -> {
                        Intent intent = new Intent(MainActivity.this, BarberDetailActivity.class);
                        intent.putExtra("username", barber.getUsername());
                        intent.putExtra("barberId", barber.getId()); // Pass barber ID if needed
                        startActivity(intent);
                });
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.Profile) {
                        Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
                } else if (menuItem.getItemId() == R.id.Home) {
                        Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                } else if (menuItem.getItemId() == R.id.Appointment) {
                        Toast.makeText(this, "Appointments selected", Toast.LENGTH_SHORT).show();
                } else if (menuItem.getItemId() == R.id.Logout) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                        finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }

        @Override
        public void onBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                        super.onBackPressed();
                }
        }
}
