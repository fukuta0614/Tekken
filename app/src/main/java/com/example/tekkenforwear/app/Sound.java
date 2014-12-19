package com.example.tekkenforwear.app;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by fukuta0614 on 12/19/14.
 */
public class Sound {
    private SoundPool mSoundPool;
    private int mSE_PUNCH, mSE_UPPER, mSE_HOOK;

    public Sound(Context context) {
        this.mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
//        mSoundPool = SoundPool.Builder
        this.mSE_PUNCH = mSoundPool.load(context, R.raw.punch, 1);
        this.mSE_UPPER = mSoundPool.load(context, R.raw.upper, 1);
        this.mSE_HOOK = mSoundPool.load(context, R.raw.hook, 1);

    }

    public void play(int number) {
        switch (number) {
            case 1:
                mSoundPool.play(mSE_PUNCH, 1.0f, 1.0f, 0, 0, 1.0f);
                break;
            case 2:
                mSoundPool.play(mSE_UPPER, 1.0f, 1.0f, 0, 0, 1.0f);
                break;
            case 3:
                mSoundPool.play(mSE_HOOK, 1.0f, 1.0f, 0, 0, 1.0f);
                break;
        }
    }
}
