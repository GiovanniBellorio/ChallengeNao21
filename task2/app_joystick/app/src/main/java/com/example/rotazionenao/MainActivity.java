package com.example.rotazionenao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.os.AsyncTask;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static android.hardware.Sensor.TYPE_ROTATION_VECTOR;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, SensorEventListener {

    private int[] rotationAngles = new int[2];
    private int[] rotationAnglesOld = new int[2];

    String ip  = ""; // dichiaro l'indirizzo ip per mandate i dati

    private SensorManager sensorManager;
    private Sensor rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch switchGyro = (Switch) findViewById(R.id.switchGisoscopio);
        switchGyro.setOnCheckedChangeListener(this);


        // MOVIMENTO TESTA


        Button DESTRA = findViewById(R.id.buttDestra);

        Button SINISTRA = findViewById(R.id.bttSinistra);

        Button RESET = findViewById(R.id.bttReset);

        Button INVIO = findViewById(R.id.bttConferma);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotation = sensorManager.getDefaultSensor(TYPE_ROTATION_VECTOR);

        // CONTROLLO CLICK DEI BOTTONI


        SINISTRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("sx", ip);
                Toast.makeText(getApplicationContext(), "sent sx on ip:" + ip , Toast.LENGTH_LONG).show();
            }
        });


        DESTRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("dx", ip);
                Toast.makeText(getApplicationContext(), "sent dx on ip:" + ip , Toast.LENGTH_LONG).show();
            }
        });

        RESET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("res", ip);
                Toast.makeText(getApplicationContext(), "sent res on ip:" + ip , Toast.LENGTH_LONG).show();
            }
        });

        INVIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textIp = findViewById(R.id.editTextIp);
                ip = textIp.getText().toString();
                Toast.makeText(getApplicationContext(), "ip set:" + ip , Toast.LENGTH_LONG).show();
            }
        });

    }

    // GYROSCOPE SWITCH
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            sensorManager.unregisterListener(this);
        }
    }

    // GYROSCOPE
    public void onSensorChanged(SensorEvent event) {
        TextView textX = (TextView) findViewById(R.id.textX);
        TextView textY = (TextView) findViewById(R.id.textY);

        rotationAngles[0] = Math.round(event.values[0] * 100);
        rotationAngles[1] = Math.round(event.values[2] * 100);

        if (rotationAngles[0] > rotationAnglesOld[0] + 2 || rotationAngles[0] < rotationAnglesOld[0] - 2 || rotationAngles[1] > rotationAnglesOld[1] + 2 || rotationAngles[1] < rotationAnglesOld[1] - 2) {
            send_data(Integer.toString(rotationAngles[1]), ip);
            rotationAnglesOld[0] = rotationAngles[0];
            rotationAnglesOld[1] = rotationAngles[1];
        }
        textX.setText("X: "+rotationAngles[0]);
        textY.setText("Z: "+rotationAngles[1]);
        }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // SEND DATA TO SERVER

    public void send_data(String message, String serverIp){
        BackgroundTask b1 = new BackgroundTask();
        b1.execute(message,serverIp);
    }

    class BackgroundTask extends AsyncTask<String,Void,Void>
    {
        PrintWriter writer;
        Socket s;
        @Override
        protected Void doInBackground(String... voids){
            try{
                String message = voids[0];
                String serverIp = voids[1];
                s = new Socket(serverIp,5050);
                writer = new PrintWriter(s.getOutputStream());
                writer.write(message+"_pho");
                System.out.println(message);
                writer.flush();
                writer.close();
                s.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
