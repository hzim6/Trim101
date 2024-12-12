package com.example.trim101;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookAppointmentActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TextView textTime;
    private Button buttonSelectTime, buttonConfirmAppointment;
    private ImageButton btnBack;
    private RadioGroup paymentTypeGroup;
    private FirebaseFirestore db;
    private String barberId, userId, selectedTime, customerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        // Initialize views
        datePicker = findViewById(R.id.datePicker);
        textTime = findViewById(R.id.textTime);
        buttonSelectTime = findViewById(R.id.buttonSelectTime);
        paymentTypeGroup = findViewById(R.id.paymentTypeGroup);
        buttonConfirmAppointment = findViewById(R.id.buttonConfirmAppointment);
        btnBack = findViewById(R.id.btnBack);

        db = FirebaseFirestore.getInstance();

        // Get Barber ID and User ID
        barberId = getIntent().getStringExtra("barberId");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve customer's name from Firestore
        db.collection("Users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                customerName = documentSnapshot.getString("username");
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error retrieving customer name", Toast.LENGTH_SHORT).show());

        // Set up time picker
        buttonSelectTime.setOnClickListener(v -> openTimePicker());

        // Confirm appointment button
        buttonConfirmAppointment.setOnClickListener(v -> confirmAppointment());

        // Back button
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    textTime.setText("Selected Time: " + selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void confirmAppointment() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String appointmentDate = day + "/" + (month + 1) + "/" + year;

        int selectedPaymentTypeId = paymentTypeGroup.getCheckedRadioButtonId();
        RadioButton selectedPaymentTypeButton = findViewById(selectedPaymentTypeId);
        String paymentType = selectedPaymentTypeButton.getText().toString();

        if (selectedTime == null) {
            Toast.makeText(this, "Please select a time for the appointment", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure customerName has been retrieved
        if (customerName == null) {
            Toast.makeText(this, "Customer name not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create appointment data
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("barberId", barberId);
        appointment.put("userId", userId);
        appointment.put("customerName", customerName);
        appointment.put("appointmentDate", appointmentDate);
        appointment.put("appointmentTime", selectedTime);
        appointment.put("paymentType", paymentType);
        appointment.put("status", "Pending");

        db.collection("Appointments").add(appointment)
                .addOnSuccessListener(documentReference -> {
                    // Go to confirmation screen
                    Intent intent = new Intent(BookAppointmentActivity.this, AppointmentConfirmationActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(BookAppointmentActivity.this, "Failed to Confirm Appointment", Toast.LENGTH_SHORT).show());
    }
}
