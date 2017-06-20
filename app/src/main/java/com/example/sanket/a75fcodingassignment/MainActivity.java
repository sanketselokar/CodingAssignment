package com.example.sanket.a75fcodingassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener, SensorEventListener
{
    private Button ambientBtn, lightBtn, pressureBtn, humidityBtn, send_btn;
    private TextView ambientValue, lightValue, pressureValue, humidityValue;
    private TextView[] valueFields = new TextView[4];

    //Sensor type
    private final int AMBIENT=0;
    private final int LIGHT=1;
    private final int PRESSURE=2;
    private final int HUMIDITY=3;

    // For sensing
    private SensorManager senseManage;
    private Sensor envSense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ambientBtn = (Button)findViewById(R.id.ambient_btn);
        lightBtn = (Button)findViewById(R.id.light_btn);
        pressureBtn = (Button)findViewById(R.id.pressure_btn);
        humidityBtn = (Button)findViewById(R.id.humidity_btn);
        send_btn = (Button)findViewById(R.id.send_btn);

        ambientBtn.setOnClickListener(this);
        lightBtn.setOnClickListener(this);
        pressureBtn.setOnClickListener(this);
        humidityBtn.setOnClickListener(this);
        send_btn.setOnClickListener(this);

        ambientValue = (TextView)findViewById(R.id.ambient_text);
        valueFields[AMBIENT]=ambientValue;
        lightValue = (TextView)findViewById(R.id.light_text);
        valueFields[LIGHT]=lightValue;
        pressureValue = (TextView)findViewById(R.id.pressure_text);
        valueFields[PRESSURE]=pressureValue;
        humidityValue = (TextView)findViewById(R.id.humidity_text);
        valueFields[HUMIDITY]=humidityValue;

        senseManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }
    public void onClick(View v)
    {
        if (v.getId()==R.id.ambient_btn)
        {
           //ambient temperature
            envSense = senseManage.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            if(envSense==null)
                Toast.makeText(this.getApplicationContext(),
                        "Sorry - your device doesn't have an ambient temperature sensor!",
                        Toast.LENGTH_SHORT).show();
            else
                senseManage.registerListener(this, envSense, SensorManager.SENSOR_DELAY_NORMAL);
        }

        else if(v.getId()==R.id.light_btn)
        {
            //light
            envSense = senseManage.getDefaultSensor(Sensor.TYPE_LIGHT);
            if(envSense==null)
                Toast.makeText(this.getApplicationContext(),
                        "Sorry - your device doesn't have a light sensor!",
                        Toast.LENGTH_SHORT).show();
            else
                senseManage.registerListener(this, envSense, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else if(v.getId()==R.id.pressure_btn)
        {
            //pressure
            envSense = senseManage.getDefaultSensor(Sensor.TYPE_PRESSURE);
            if(envSense==null)
                Toast.makeText(this.getApplicationContext(),
                        "Sorry - your device doesn't have a pressure sensor!",
                        Toast.LENGTH_SHORT).show();
            else
                senseManage.registerListener(this, envSense, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else if(v.getId()==R.id.humidity_btn)
        {
            //humidity
            envSense = senseManage.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            if(envSense==null)
                Toast.makeText(this.getApplicationContext(),
                        "Sorry - your device doesn't have a humidity sensor!",
                        Toast.LENGTH_SHORT).show();
            else
                senseManage.registerListener(this, envSense, SensorManager.SENSOR_DELAY_NORMAL);
        }

        //Send button
        else if(v.getId()==R.id.send_btn)
        {
            System.out.println("Inside the send button");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://mywebsite.com/phpscriptwebside.php");
            try {
                SensorEvent event = null;
                float sensorValue1 = event.values[0];
                float sensorValue2 = event.values[0];
                float sensorValue3 = event.values[0];
                float sensorValue4 = event.values[0];
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("ambient", Float.toString(sensorValue1)));
                nameValuePairs.add(new BasicNameValuePair("light", Float.toString(sensorValue2)));
                nameValuePairs.add(new BasicNameValuePair("pressure", Float.toString(sensorValue3)));
                nameValuePairs.add(new BasicNameValuePair("humidity", Float.toString(sensorValue4)));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            String sendMessage = "Data has been sent";
            Toast accuracyToast = Toast.makeText(this.getApplicationContext(), sendMessage, Toast.LENGTH_SHORT);
            accuracyToast.show();
        }

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        String accuracyMsg = "";
        switch(accuracy){
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                accuracyMsg="Sensor has high accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                accuracyMsg="Sensor has medium accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                accuracyMsg="Sensor has low accuracy";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                accuracyMsg="Sensor has unreliable accuracy";
                break;
            default:
                break;
        }
        Toast accuracyToast = Toast.makeText(this.getApplicationContext(), accuracyMsg, Toast.LENGTH_SHORT);
        accuracyToast.show();
    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        float sensorValue = event.values[0];
        TextView currValue = ambientValue;
        String envInfo="";
        int currType=event.sensor.getType();

        switch(currType){
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                envInfo=sensorValue+" degrees Celsius";
                currValue=valueFields[AMBIENT];
                break;
            case Sensor.TYPE_LIGHT:
                envInfo=sensorValue+" SI lux units";
                currValue=valueFields[LIGHT];
                break;
            case Sensor.TYPE_PRESSURE:
                envInfo=sensorValue+" hPa (millibars)";
                currValue=valueFields[PRESSURE];
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                envInfo=sensorValue+" percent humidity";
                currValue=valueFields[HUMIDITY];
                break;
            default: break;
        }
        currValue.setText(envInfo);
        envSense=null;
        senseManage.unregisterListener(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        senseManage.unregisterListener(this);
    }


}
