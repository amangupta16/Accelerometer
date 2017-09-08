package com.example.jessica.myapplication;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;

    public class MainActivity extends Activity implements SensorEventListener {
        Button Start;
        Button End;
        private SensorManager sensorManager;
        private boolean color = false;
        private View view;
        int click = 0;
        private long lastUpdate;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            view = findViewById(R.id.textView);
            //view.setBackgroundColor(Color.BLUE);

            sensorManager = (SensorManager)
                    getSystemService(SENSOR_SERVICE);
            lastUpdate = System.currentTimeMillis();
            addListenerOnButton();
        }

        public void addListenerOnButton() {

            Start = (Button) findViewById(R.id.button1);
            End = (Button) findViewById(R.id.button2);

            Start.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    click = 1;

                }

            });

            End.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    click = 0;

                }

            });

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && click>0) {
                getAccelerometer(event);
            }

        }

        private void getAccelerometer(SensorEvent event) {
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            long actualTime = event.timestamp;
            if (accelationSquareRoot >= 2)
            {
                if (actualTime - lastUpdate < 10) {
                    return;
                }
                lastUpdate = actualTime;
                Toast.makeText(this,"timestamp:"+lastUpdate+"\n"+"x's value:"+x+"\n"+"y's value:"+y+"\n"+"z's value"+z, Toast.LENGTH_SHORT).show();
//                if (color) {
//                    view.setBackgroundColor(Color.GREEN);
//                } else {
//                    view.setBackgroundColor(Color.RED);
//                }
//                color = !color;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        protected void onResume() {
            super.onResume();
            // register this class as a listener for the orientation and
            // accelerometer sensors
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        protected void onPause(){   // unregister listener
            super.onPause();
            sensorManager.unregisterListener(this);
        }
    }