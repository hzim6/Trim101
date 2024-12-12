package com.example.trim101;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    private TextView textConfirmation;
    private Button buttonReturnHome;
    private ProgressBar progressBar;
    private ImageView checkmarkIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_confirmation);

        // Initialize views
        textConfirmation = findViewById(R.id.textConfirmation);
        buttonReturnHome = findViewById(R.id.buttonReturnHome);
        progressBar = findViewById(R.id.progressBar);
        checkmarkIcon = findViewById(R.id.checkmarkIcon);

        // Hide button and checkmark initially
        buttonReturnHome.setVisibility(View.GONE);
        checkmarkIcon.setVisibility(View.GONE);

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up button click listener
        buttonReturnHome.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentConfirmationActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the booking activity
        });

        // Delay to simulate loading and update UI elements
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE); // Hide progress bar
            checkmarkIcon.setVisibility(View.VISIBLE); // Show checkmark icon
            textConfirmation.setText("Appointment Booked!"); // Update text
            buttonReturnHome.setVisibility(View.VISIBLE); // Show the button
        }, 3000); // 3-second delay
    }
}
