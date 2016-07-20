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


public class Usera extends AppCompatActivity implements SensorEventListener {


    //private static Button fan_button;

    private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager SM;
    String A="A";
    String B="B";

    Button fanOn, fanOff, doorOpen, doorClose, btnDis;
    SeekBar brightness;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usera);
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        Intent newint = getIntent();
        address= newint.getStringExtra(address);

        //setContentView(R.layout.activity_usera);
        doorOpen=(Button)findViewById(R.id.button2);
        doorClose=(Button)findViewById(R.id.button3);
        btnDis=(Button)findViewById(R.id.button6);

        xText=(TextView)findViewById(R.id.xText);
        yText=(TextView)findViewById(R.id.yText);
        zText=(TextView)findViewById(R.id.zText);

        doorOpen.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                TurnOnLed();
            }
        });

        doorClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TurnOffLed();
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                Disconnect();
            }

        });

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

    private void TurnOffLed(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("B".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void TurnOnLed(){
        if(btSocket != null){
            try
            {
                btSocket.getOutputStream().write("A".toString().getBytes());
            }
            catch (IOException e){
                msg("Error");
            }
        }
    }

    private void msg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int acuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        xText.setText("X: " + event.values[0]);
        yText.setText("Y: " + event.values[1]);
        zText.setText("Z: " + event.values[2]);
    }

    private class connectBT extends AsyncTask<Void,Void,Void>{
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
