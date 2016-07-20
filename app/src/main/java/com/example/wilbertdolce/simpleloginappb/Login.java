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

public class Login extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static TextView attempts;
    private static Button login_btn;
    int attempt_counter = 5;
    Button btnPaired;
    ListView deviceList;
    private BluetoothAdapter myBluetooth = null;
    //private Set pairedDevices;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPaired = (Button)findViewById(R.id.button);
        deviceList = (ListView)findViewById(R.id.listView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth==null){

            Toast.makeText(getApplicationContext(),"Bluetooth Device Not Available",Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!myBluetooth.isEnabled()){
            Intent turnBTon = new Intent((BluetoothAdapter.ACTION_REQUEST_ENABLE));
            startActivityForResult(turnBTon,1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                pairedDevicesList();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        LoginButton();
    }

    private void pairedDevicesList(){
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList<>();

        if (pairedDevices.size()>0){
            for(BluetoothDevice bt : pairedDevices){
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }

        

        else
        {
            Toast.makeText(getApplicationContext(),"No Paired Bluetooth Devices Found",Toast.LENGTH_LONG).show();

        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener(){
        public void onItemClick (AdapterView av, View v, int arg2, long arg3){
            String info =((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent i = new Intent(Login.this,Usera.class);
            i.putExtra(EXTRA_ADDRESS, address);

            startActivity(i);
        }
    };

    public void  LoginButton(){
        username=(EditText)findViewById(R.id.editText_user);
        password=(EditText)findViewById(R.id.editText_password);
        attempts=(TextView)findViewById(R.id.textView_attempts_Count);
        login_btn=(Button)findViewById(R.id.button_login);

        attempts.setText(Integer.toString(attempt_counter));

        login_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                public void onClick(View v){
                        if(username.getText().toString().equals("user")
                                && password.getText().toString().equals("pass")){
                            Toast.makeText(Login.this,"Username and Password is correct",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("com.example.wilbertdolce.simpleloginappb.Usera");
                            startActivity(intent);
                        } else{
                            Toast.makeText(Login.this,"Username and Password is NOT correct",
                                    Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            attempts.setText(Integer.toString(attempt_counter));
                            if(attempt_counter==0){
                                login_btn.setEnabled(false);
                            }

                        }

                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
