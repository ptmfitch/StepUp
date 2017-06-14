package quantifiedchaos.stepup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends Activity implements View.OnClickListener {
    private FloatingActionButton mSettingsButton;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    static TextView MovementText;
    static TextView TimeText;
    static TextView userTime;
    static TextView IntervalTime;
    CountDownTimer mTimer;
    static int mIntervalTime = 30000;

    static Button btnTimePickerStart, btnTimePickerEnd;
    static TextView txtTimeStart, txtTimeEnd;
    static private int mHourStart, mMinuteStart, mHourEnd, mMinuteEnd;

    static boolean start = false, end = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        MovementText = findViewById(R.id.MovementBool);
        userTime = findViewById(R.id.TimeInterval);
        IntervalTime = findViewById(R.id.SetInterval);

        btnTimePickerStart = findViewById(R.id.StartTime);
        btnTimePickerEnd = findViewById(R.id.EndTime);
        txtTimeStart = findViewById(R.id.StartTimeText);
        txtTimeEnd = findViewById(R.id.EndTimeText);

        btnTimePickerStart.setOnClickListener(this);
        btnTimePickerEnd.setOnClickListener(this);

        btnTimePickerStart.setOnClickListener(this);
        btnTimePickerEnd.setOnClickListener(this);

        mSettingsButton = findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(this);

        TimeText = findViewById(R.id.TimeText);
        startTimer(mIntervalTime);
    }

    private void startTimer(int secondsLength){
            if(mTimer !=null){
                mTimer.cancel();
                mTimer = null;
            }
            mTimer = new CountDownTimer(secondsLength, 1000) {

            public void onTick(long millisUntilFinished) {
                TimeText.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
                int currentMinute = rightNow.get(Calendar.MINUTE); // return the minute
                // TODO: 14/06/17 CURRENTLY only support positive time eg. 1pm to 3 pm not over 24 hours eg. 3pm to  1pm next day
                // TODO: 14/06/17 Using  0:00 for both times just freezes program.
                if((currentHour > mHourStart && currentHour < mHourEnd) ){
                    TimeText.setText("done!");
                    triggerAlarm();
                }
                else if(currentHour == mHourStart || currentHour == mHourEnd){
                    if((currentMinute > mMinuteStart) && (currentMinute < mMinuteEnd)){
                        TimeText.setText("done!");
                        triggerAlarm();
                    }
                else{
                        TimeText.setText("Tracker Paused!");
                    }
                }
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


    @Override
    public void onClick(View v){

        if (v == mSettingsButton){
            if(!userTime.getText().toString().isEmpty() && (start == true && end == true)){
                mIntervalTime = 60*(1000 * Integer.parseInt(userTime.getText().toString()));
                IntervalTime.setText("Minutes Interval: " + (mIntervalTime / 1000)/60);
                startTimer(mIntervalTime);
                mTimer.start();
            }
            else{
                Toast.makeText(MainActivity.this, "Fill in all boxes!", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == btnTimePickerStart) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHourStart = c.get(Calendar.HOUR_OF_DAY);
            mMinuteStart = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTimeStart.setText(hourOfDay + ":" + minute);
                            mHourStart = hourOfDay;
                            mMinuteStart = minute;
                        }
                    }, mHourStart, mMinuteStart, false);
            timePickerDialog.show();
            start = true;

        }

        if (v == btnTimePickerEnd) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHourEnd = c.get(Calendar.HOUR_OF_DAY);
            mMinuteEnd = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTimeEnd.setText(hourOfDay + ":" + minute);
                            mHourEnd = hourOfDay;
                            mMinuteEnd = minute;
                        }
                    }, mHourEnd, mMinuteEnd, false);
            timePickerDialog.show();
            end = true;
        }
    }
}


