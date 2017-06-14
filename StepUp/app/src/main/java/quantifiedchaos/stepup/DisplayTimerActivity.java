package quantifiedchaos.stepup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayTimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_timer);

        //Get data from shared preferences
        String startTime = getDefaults("startTime", this);
        String endTime = getDefaults("endTime", this);
        String interval = getDefaults("interval", this);

        // Find the layout's TextView and set its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(startTime + endTime + interval);
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
