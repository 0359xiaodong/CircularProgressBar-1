package hrules.com.circularprogressbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.hrules.circularprogressbar.CircularProgressBar;
import com.hrules.circularprogressbar.CircularProgressBarListener;
import com.hrules.circularprogressbar.CircularProgressIndeterminateBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CircularProgressBarListener {
    private static final String TAG = "MainActivity";

    private CircularProgressBar circularProgressBar;
    private CircularProgressIndeterminateBar circularProgressIndeterminateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        circularProgressBar.setListener(this);

        circularProgressIndeterminateBar = (CircularProgressIndeterminateBar) findViewById(R.id.circularProgressIndeterminateBar);
        circularProgressIndeterminateBar.setListener(this);

        findViewById(R.id.buttonDeterminateGo).setOnClickListener(this);
        findViewById(R.id.buttonDeterminateGoNoAnim).setOnClickListener(this);
        findViewById(R.id.buttonDeterminateReset).setOnClickListener(this);
        findViewById(R.id.buttonDeterminateResetNoAnim).setOnClickListener(this);
        findViewById(R.id.buttonDeterminateCountdown).setOnClickListener(this);

        findViewById(R.id.buttonIndeterminateStart).setOnClickListener(this);
        findViewById(R.id.buttonIndeterminateStop).setOnClickListener(this);

        Toast.makeText(this, getString(R.string.click), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDeterminateGo:
                circularProgressBar.setPercentValue(.75f, true);
                break;

            case R.id.buttonDeterminateGoNoAnim:
                circularProgressBar.setPercentValue(.75f, false);
                break;

            case R.id.buttonDeterminateReset:
                circularProgressBar.reset(true);
                break;

            case R.id.buttonDeterminateResetNoAnim:
                circularProgressBar.reset(false);
                break;

            case R.id.buttonDeterminateCountdown:
                circularProgressBar.setMaxValue(1000);
                circularProgressBar.setValue(circularProgressBar.getMaxValue(), false);
                circularProgressBar.setValue(0, true);
                break;

            case R.id.buttonIndeterminateStart:
                circularProgressIndeterminateBar.start();
                break;

            case R.id.buttonIndeterminateStop:
                circularProgressIndeterminateBar.stop();
                break;
        }
    }

    @Override
    public void onClick() {
        circularProgressBar.setFilled(!circularProgressBar.isFilled());
        circularProgressIndeterminateBar.setFilled(!circularProgressIndeterminateBar.isFilled());

        Log.d(TAG, "onClick");
    }

    @Override
    public void onStartSpinning() {
        Log.d(TAG, "onStartSpinning");
    }

    @Override
    public void onFinish() {
        Log.d(TAG, "onFinish");
    }

    @Override
    public void onStopSpinning() {
        Log.d(TAG, "onStopSpinning");
    }

    @Override
    public void onValueChanged(float value) {
        Log.d(TAG, "onValueChanged " + value);
    }

    @Override
    public void onPercentValueChanged(float percent) {
        Log.d(TAG, "onPercentValueChanged " + percent);
    }
}
