package com.hrules.circularprogressbar;

public interface CircularProgressBarListener {

    void onClick();

    void onFinish();

    void onValueChanged(float value);

    void onPercentValueChanged(float percent);
}
