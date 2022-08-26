package com.gma.saveandreaddatafromfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button submit, fetchButton;
    TextView fetchedText;
    DatabaseReference rootRef, demoRef;

    FirebaseAuth firebaseAuth;

    String dateInString = "";

    String device_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.etValue);
        submit = findViewById(R.id.btnSubmit);

        fetchedText = findViewById(R.id.tvValue);

        fetchButton = findViewById(R.id.btnFetch);

        // Database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();

        Date currentTime = Calendar.getInstance().getTime();

        device_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        demoRef = rootRef.child(device_id);

        // Get Current Date

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String startedDate = df.format(c);

        // Get Current Date

        // Get 30 Days added from Current Date

         dateInString = startedDate;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c1 = Calendar.getInstance();
        try {
            c1.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c1.add(Calendar.DATE, 30);
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date resultdate = new Date(c1.getTimeInMillis());
        dateInString = sdf.format(resultdate);

//        Today Date
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = df1.format(c);

        //Current time
        Date c2 = Calendar.getInstance().getTime();

        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                fetchedText.setText(value);


                try {

                    Log.d("value:","value:"+value);


                    if (value == null && value.equals("")) {

                        demoRef.setValue(device_id + "/"+ startedDate +"/"+ dateInString+"/"+currentDate);

                    }
                }catch (NullPointerException e){
                    demoRef.setValue(device_id + "/"+ startedDate+"/" + dateInString+"/"+currentDate);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });




        // Get 30 Days added from Current Date

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString();

                // Push creates a unique id in database
//                demoRef.setValue(device_id+ "/"+formattedDate +"/"+dateInString);
                demoRef.setValue(value+"/"+device_id+"/"+dateInString);
            }
        });


        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        fetchedText.setText(value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
