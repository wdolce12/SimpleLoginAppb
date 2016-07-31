package com.example.wilbertdolce.simpleloginappb;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.provider.SyncStateContract;
import android.app.Activity;


import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.UUID;
public class Usera extends AppCompatActivity {


    //private static Button fan_button;

   // private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager SM;
    String A="A";
    String B="B";
    int F=0;
    String t;
    private TextView temp;
    Button fanOn, fanOff, doorOpen, doorClose, btnDis, motion, temperature;
    SeekBar brightness;
    String address = null;
    private ProgressDialog progress;
    public static String EXTRA_ADDRESS=null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usera);


        //Intent newint = getIntent();
        Intent newint = getIntent();
        address= newint.getStringExtra(pin_input.EXTRA_ADDRESS);

        //setContentView(R.layout.activity_usera);
        doorOpen=(Button)findViewById(R.id.button2);
        doorClose=(Button)findViewById(R.id.button3);
        btnDis=(Button)findViewById(R.id.button6);
        //fanOn=(Button)findViewByID(R.id.button4);
        fanOn=(Button)findViewById(R.id.button4);
        fanOff=(Button)findViewById(R.id.button5);
        motion=(Button)findViewById(R.id.motion);
        temp=(TextView)findViewById(R.id.temper);
        temperature=(Button)findViewById(R.id.temp_btn);


        InputStream in = null;

        new ConnectBT().execute();



        //xText=(TextView)findViewById(R.id.xText);
        //yText=(TextView)findViewById(R.id.yText);
        //zText=(TextView)findViewById(R.id.zText);

        doorOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnOnLed();
            }
        });
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTemp();
            }
        });
        doorClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TurnOffLed();
            }
        });
        fanOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnOnFan();
            }
        });
        fanOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TurnOffFan();
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                Disconnect();
            }

        });
        motion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Disconnecta();
                Intent newint2 = new Intent(Usera.this,motion_option.class);
                newint2.putExtra(EXTRA_ADDRESS,address);
                startActivity(newint2);
            }
        });
    }
    private void Disconnecta() {
        if (btSocket != null) {
            try
            {
                btSocket.close();
            }
            catch (IOException e)
            {
                msg("Connect Success!!");
            }
        }
    }
    private void Disconnect() {
        if (btSocket != null) {
            try
            {
                btSocket.close();
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void GetTemp() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write('E');
                F=btSocket.getInputStream().read();
                temp.setText("Temperature = " + F);

            } catch (IOException e) {
                msg("ERROR!!");
            }
        }
    }
    private void TurnOffLed(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write('B');
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void TurnOnFan(){
        if(btSocket != null){
            try
            {
                btSocket.getOutputStream().write('C');
            }
            catch (IOException e)
            {
                msg("ERROR");
            }
        }
    }
    private void TurnOffFan(){
        if(btSocket != null){
            try
            {
                btSocket.getOutputStream().write('D');
            }
            catch (IOException e)
            {
                msg("ERROR");
            }
        }
    }
    private void TurnOnLed(){
        if(btSocket != null){
            try
            {
                btSocket.getOutputStream().write('A');
            }
            catch (IOException e){
                msg("Error");
            }
        }
    }
    private void msg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
    private class ConnectBT extends AsyncTask<Void,Void,Void>{
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(Usera.this,"Connecting . . .","Please Wait ! ! !");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try
            {
                if (btSocket== null || !isBtConnected){
                    myBluetooth= BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    //btSocket=dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }

            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(!ConnectSuccess)
            {
                msg("connection Failed");
                finish();
            }
            else
            {
                msg("Connected!");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
