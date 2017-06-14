package quantifiedchaos.stepup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "quantifiedchaos.stepup.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Called when the user taps the Confirm button
    public void confirmDetails(View view) {
        Intent intent = new Intent(this, DisplayTimerActivity.class);

        //Get the user inputs
        EditText startText = (EditText) findViewById(R.id.startTime);
        String startTime   = startText.getText().toString();
        EditText endText   = (EditText) findViewById(R.id.endTime);
        String endTime     = endText.getText().toString();
        EditText interText = (EditText) findViewById(R.id.interval);
        String interTime   = interText.getText().toString();

        //Check user inputs are valid
        if(!validTime(startTime)) {
            startText.setError("Please input a valid start time!");
            return;
        }
        if(!validTime(endTime)) {
            endText.setError("Please input a valid end time!");
            return;
        }
        if(!validInterval(interTime)) {
            interText.setError("Please input a valid interval!");
            return;
        }


        //Save user inputs to shared preferences
        setDefaults("startTime", startTime, this);
        setDefaults("endTime", endTime, this);
        setDefaults("interval", interTime, this);

        startActivity(intent);
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private boolean validTime(String time) {
        //Check null
        if(time.length() == 0) {
            return false;
        }
        return true;
    }

    private boolean validInterval(String interval) {
        //Check null
        if(interval.length() == 0) {
            return false;
        }
        int value = Integer.valueOf(interval);
        if(value <= 0) {
            return false;
        }
        return true;
    }
}
