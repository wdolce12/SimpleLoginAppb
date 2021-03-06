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


public class motion_option extends AppCompatActivity implements SensorEventListener {


    //private static Button fan_button;

    private TextView doorstat,fanstat;
    private Sensor mySensor;
    private SensorManager SM;
    String A="A";
    String B="B";

    Button regular;

    //Button fanOn, fanOff, doorOpen, doorClose, btnDis, motion;
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
        setContentView(R.layout.activity_motion_option);
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Intent newint = getIntent();
        Intent newint = getIntent();
        address= newint.getStringExtra(pin_input.EXTRA_ADDRESS);

        regular = (Button)findViewById(R.id.reg);


        new ConnectBT().execute();

        doorstat=(TextView)findViewById(R.id.doorstat);
        fanstat=(TextView)findViewById(R.id.fanstat);

        regular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnecta();
                Intent newint4 = new Intent(motion_option.this,Usera.class);
                newint4.putExtra(EXTRA_ADDRESS,address);
                startActivity(newint4);
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
    @Override
    public void onAccuracyChanged(Sensor sensor, int acuracy){

    }
    @Override
    public void onSensorChanged(SensorEvent event){

        if(event.values[0]>7.0){
            TurnOnLed();
            doorstat.setText("THE DOOR IS UNLOCKED!!");
        }
        if(event.values[0]<(-7.0)){
            TurnOffLed();
            doorstat.setText("THE DOOR IS LOCKED!!");
        }
        if(event.values[1]>10.0){
            TurnOnFan();
            fanstat.setText("THE FAN IS ON!!");
        }
        if(event.values[1]<(-1.5)){
            TurnOffFan();
            fanstat.setText("THE FAN IS OFF!!");
        }
    }
    private class ConnectBT extends AsyncTask<Void,Void,Void>{
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(motion_option.this,"Connecting . . .","Please Wait ! ! !");
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
