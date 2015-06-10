package hrules.com.circularprogressbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.hrules.circularprogressbar.CircularProgressBar;
import com.hrules.circularprogressbar.CircularProgressBarListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        circularProgressBar.setListener(new CircularProgressBarListener() {
            @Override
            public void onClick() {
                Log.d(TAG, "onClick");
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish");
            }

            @Override
            public void onValueChanged(float value) {
                Log.d(TAG, "onValueChanged " + value);
            }

            @Override
            public void onPercentValueChanged(float percent) {
                Log.d(TAG, "onPercentValueChanged " + percent);
            }
        });

        findViewById(R.id.buttonGo).setOnClickListener(this);
        findViewById(R.id.buttonGoNoAnim).setOnClickListener(this);
        findViewById(R.id.buttonReset).setOnClickListener(this);
        findViewById(R.id.buttonResetNoAnim).setOnClickListener(this);
        findViewById(R.id.buttonCountdown).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonGo:
                circularProgressBar.setPercentValue(.75f, true);
                break;

            case R.id.buttonGoNoAnim:
                circularProgressBar.setPercentValue(.75f, false);
                break;

            case R.id.buttonReset:
                circularProgressBar.reset(true);
                break;

            case R.id.buttonResetNoAnim:
                circularProgressBar.reset(false);
                break;

            case R.id.buttonCountdown:
                circularProgressBar.setMaxValue(1000);
                circularProgressBar.setValue(circularProgressBar.getMaxValue(), false);
                circularProgressBar.setValue(0, true);
                break;

        }
    }
}
