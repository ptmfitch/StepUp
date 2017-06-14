package quantifiedchaos.stepup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends Activity{
    private FloatingActionButton mSettingsButton;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    static TextView MovementText;
    static TextView TimeText;
    static TextView userTime;
    static TextView IntervalTime;
    static CountDownTimer mTimer;
    static int mIntervalTime = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        MovementText = findViewById(R.id.MovementBool);
        userTime = findViewById(R.id.TimeInterval);
        IntervalTime = findViewById(R.id.SetInterval);


        mSettingsButton = findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(!userTime.getText().toString().isEmpty()){


                    mIntervalTime = 60*(1000 * Integer.parseInt(userTime.getText().toString()));
                    IntervalTime.setText("Minutes Interval: " + (mIntervalTime / 1000)/60);
                    mTimer.cancel();
                    startTimer(mIntervalTime);
                    mTimer.start();
                }
                else{
                    Toast.makeText(MainActivity.this, "Fill in all boxes!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        TimeText = findViewById(R.id.TimeText);
        startTimer(mIntervalTime);



    }

    private void startTimer(int secondsLength){
            mTimer = new CountDownTimer(secondsLength, 1000) {

            public void onTick(long millisUntilFinished) {
                TimeText.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                TimeText.setText("done!");
                triggerAlarm();
            }
        };
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
//        mSensorManager.unregisterListener(mSensorEventListener);
    }


    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        private int mStep;
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.values[0] == 1.0f) {
                mStep++;
                mTimer.start();
            }
            MovementText.setText(Integer.toString(mStep));

        }
    };


    public void triggerAlarm() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }



}

