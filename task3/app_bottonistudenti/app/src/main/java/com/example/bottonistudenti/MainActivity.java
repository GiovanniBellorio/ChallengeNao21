package com.example.bottonistudenti;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.os.Bundle;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    String ip = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button st1 = findViewById(R.id.button);
        Button st2 = findViewById(R.id.button2);
        Button st3 = findViewById(R.id.button3);
        Button st4 = findViewById(R.id.button4);
        Button st5 = findViewById(R.id.button5);
        Button inv = findViewById(R.id.invio);


        st1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("1", ip);
                Toast.makeText(getApplicationContext(), "sent 1 on ip:" + ip, Toast.LENGTH_LONG).show();
            }
        });

        st2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("2", ip);
                Toast.makeText(getApplicationContext(), "sent 2 on ip:" + ip, Toast.LENGTH_LONG).show();
            }
        });

        st3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("3", ip);
                Toast.makeText(getApplicationContext(), "sent 3 on ip:" + ip, Toast.LENGTH_LONG).show();
            }
        });

        st4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("4", ip);
                Toast.makeText(getApplicationContext(), "sent 4 on ip:" + ip, Toast.LENGTH_LONG).show();
            }
        });

        st5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data("5", ip);
                Toast.makeText(getApplicationContext(), "sent 5 on ip:" + ip, Toast.LENGTH_LONG).show();
            }
        });


        inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textIp = findViewById(R.id.editTextIp);
                ip = textIp.getText().toString();
                Toast.makeText(getApplicationContext(), "ip set:" + ip, Toast.LENGTH_LONG).show();
            }
        });

    }

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