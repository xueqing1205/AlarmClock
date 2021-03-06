package com.strangeman.alarmclock.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.strangeman.alarmclock.R;
import com.strangeman.alarmclock.common.AlarmClockCommon;
import com.strangeman.alarmclock.util.ActivityCollector;
import com.strangeman.alarmclock.util.AudioPlayer;

/**
 * Created by Administrator on 2018/2/10.
 */

public class TimerOnTimeActivity extends BaseActivitySimple implements View.OnClickListener {

    private AudioManager mAudioManager;
    private int mCurrentVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_on_time);
        ActivityCollector.addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playRing();
        initViews();

    }

    private void playRing() {
        mAudioManager = (AudioManager) getSystemService(
                Context.AUDIO_SERVICE);

        mCurrentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        // 设置铃声音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                6, AudioManager.ADJUST_SAME);

        SharedPreferences shares = getSharedPreferences(
                AlarmClockCommon.EXTRA_AC_SHARE, Activity.MODE_PRIVATE);
        String ringUrl = shares.getString(AlarmClockCommon.RING_URL_TIMER, AlarmClockCommon.DEFAULT_RING_URL);

        // 默认铃声
        switch (ringUrl) {
            case AlarmClockCommon.DEFAULT_RING_URL:
                AudioPlayer.getInstance(this).playRaw(R.raw.ring_weac_alarm_clock_default, true, false);
                break;
            // 无铃声
            case AlarmClockCommon.NO_RING_URL:
                AudioPlayer.getInstance(this).stop();
                break;
            default:
                AudioPlayer.getInstance(this).play(ringUrl, true, false);
                break;
        }
    }

    private void initViews() {
        Button rogerBtn = (Button) findViewById(R.id.roger_btn);
        rogerBtn.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        // 停止播放
        AudioPlayer.getInstance(this).stop();
        // 设置铃声音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mCurrentVolume, AudioManager.ADJUST_SAME);
        overridePendingTransition(0, R.anim.zoomout);
        ActivityCollector.finishAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

