package com.example.wilbertdolce.simpleloginappb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Set;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;


import android.widget.Button;
import android.widget.ListView;

import junit.framework.TestCase;

public class pin_input extends AppCompatActivity {

    private static EditText pin_input;
    private static Button login_button;
    private static TextView attempts;
    int attempt_counter = 5;
    String address = null;
    public static String EXTRA_ADDRESS=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_input);
        Intent newint = getIntent();
        address = newint.getStringExtra(Login.EXTRA_ADDRESS);
        LoginButton();
    }
    public void LoginButton(){
        pin_input=(EditText)findViewById(R.id.pin_input);
        attempts=(TextView)findViewById(R.id.attempts1a);
        login_button=(Button)findViewById(R.id.logina);

        attempts.setText(Integer.toString(attempt_counter));

        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pin_input.getText().toString().equals("1995")) {
                            Toast.makeText(pin_input.this, "Pin is correct",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(pin_input.this,Usera.class);
                            intent.putExtra(EXTRA_ADDRESS,address);
                            startActivity(intent);
                        } else {
                            Toast.makeText(pin_input.this, "Pin is NOT correct",
                                    Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            attempts.setText(Integer.toString(attempt_counter));
                            if (attempt_counter == 0) {
                                login_button.setEnabled(false);
                            }

                        }

                    }
                }
        );

    }
}


