// BarberDetailActivity.java
package com.example.trim101;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BarberDetailActivity extends AppCompatActivity {

    private ImageView imageProfile;
    private TextView textName, textDetails, textPrice;
    private Button buttonBookAppointment;
    private ImageButton btnBack;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_detail);

        // Initialize views
        imageProfile = findViewById(R.id.imageProfile);
        textName = findViewById(R.id.textName);
        textDetails = findViewById(R.id.textDetails);
        textPrice = findViewById(R.id.textPrice);
        btnBack = findViewById(R.id.imageButton);
        buttonBookAppointment = findViewById(R.id.buttonBookAppointment);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get barber ID from intent extras
        String barberId = getIntent().getStringExtra("barberId");

        // Display initial data (name and profile picture)
        String username = getIntent().getStringExtra("username");
        textName.setText(username);

        btnBack.setOnClickListener(v -> onBackPressed());


        // Fetch additional details from Firestore
        if (barberId != null) {
            db.collection("Users").document(barberId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve details from document
                            String details = documentSnapshot.getString("details");
                            String price = documentSnapshot.getString("price");

                            // Update UI elements with the details
                            textDetails.setText(details != null ? details : "No details listed");
                            textPrice.setText(price != null ? price : "No price listed");
                        } else {
                            Toast.makeText(this, "Barber details not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error loading barber details", e);
                        Toast.makeText(this, "Failed to load details", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Barber ID is missing", Toast.LENGTH_SHORT).show();
        }

        // Set up "Book Appointment" button click listener
        buttonBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookAppointmentActivity.class);
            intent.putExtra("barberId",barberId);
            startActivity(intent);
        });
    }
}
