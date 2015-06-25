package com.hrules.circularprogressbar;

public interface CircularProgressBarListener {

    void onClick();

    void onStartSpinning();

    void onStopSpinning();

    void onFinish();

    void onValueChanged(float value);

    void onPercentValueChanged(float percent);
}
