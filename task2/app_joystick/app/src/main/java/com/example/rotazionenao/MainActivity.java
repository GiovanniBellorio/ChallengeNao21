package com.example.rotazionenao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static android.hardware.Sensor.TYPE_ROTATION_VECTOR;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, SensorEventListener {

    private int[] rotationAngles = new int[2];
    private int[] rotationAnglesOld = new int[2];

    public String ip = "";            // dichiaro 'indirizzo ip per mandate i dati

    private SensorManager sensorManager;
    private Sensor rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch switchGyro = (Switch) findViewById(R.id.switchGisoscopio);
        switchGyro.setOnCheckedChangeListener(this);

        // MOVIMENTO TESTA

        Button Invio = findViewById(R.id.invio);                        //BOTTONE CHE INVIA L'IP
        TextInputEditText IP = findViewById(R.id.testo_ip);            //TESTO CHE CONTIENE L'IP
        Button SINISTRA = findViewById(R.id.buttsinistra);
        Button DESTRA = findViewById(R.id.bttdestra);
        Button RESET = findViewById(R.id.bttreset);
        Button SU = findViewById(R.id.bttsu);
        Button GIU = findViewById(R.id.bttgiù);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotation = sensorManager.getDefaultSensor(TYPE_ROTATION_VECTOR);



        Invio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                           //VOID CHE CAMBIA L'IP

                ip = IP.getText().toString();
            }
        });


        SINISTRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("sx", ip);

            }
        });


        DESTRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("dx", ip);

            }
        });


        SU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("su", ip);

            }
        });


        GIU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("giu", ip);

            }
        });


        RESET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("res", ip);

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

    //GYROSCOPE
    public void onSensorChanged(SensorEvent event) {
        TextView textX = (TextView) findViewById(R.id.textX);
        TextView textY = (TextView) findViewById(R.id.textY);
        TextView textZ = (TextView) findViewById(R.id.textZ);

        rotationAngles[0] = Math.round(event.values[0] * 100);
        rotationAngles[1] = Math.round(event.values[2] * 100);

        if (rotationAngles[0] > rotationAnglesOld[0] + 2 || rotationAngles[0] < rotationAnglesOld[0] - 2 || rotationAngles[1] > rotationAnglesOld[1] + 2 || rotationAngles[1] < rotationAnglesOld[1] - 2) {
            send_data(Integer.toString(rotationAngles[0]), ip);
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

    class BackgroundTask extends AsyncTask<String,Void,Void> {

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
                writer.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }



}
