package com.example.tekkenforwear.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


public class MainView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    int action_my = 0;
    int action_opponent = 0;
    int damage_r, damage_l = 0;
    boolean canRun = true;
    private final String TAG = "handheld";
    private SurfaceHolder mHolder;
    private Thread mThread;
    private int PUNCH_DELAY = 0;
    private int UPPER_DELAY = 0;
    private int HOOK_DELAY = 0;
    private int count_l = 0;
    private int count_r = 0;
    private int status_l = 1;
    private int status_r = 1;

    private Rect energy_rect_l_src = new Rect();
    private Rect energy_rect_l_dst = new Rect();
    private Rect energy_rect_r_src = new Rect();
    private Rect energy_rect_r_dst = new Rect();

    Resources res = this.getContext().getResources();

    Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.tekkenbackground);
    Bitmap paul_normal = BitmapFactory.decodeResource(res, R.drawable.normal_paul);
    Bitmap paul_punch = BitmapFactory.decodeResource(res,R.drawable.punch);
    Bitmap paul_upper = BitmapFactory.decodeResource(res,R.drawable.upper);
    Bitmap paul_hook = BitmapFactory.decodeResource(res, R.drawable.hook);
    Bitmap jayg = BitmapFactory.decodeResource(res, R.drawable.jayg);
    Bitmap energy_r = BitmapFactory.decodeResource(res, R.drawable.energy_right);
    Bitmap energy_l = BitmapFactory.decodeResource(res, R.drawable.energy_left);
    Bitmap you_win = BitmapFactory.decodeResource(res, R.drawable.you_win);
    Bitmap gameover = BitmapFactory.decodeResource(res, R.drawable.gameover);

    /////constructor
    public MainView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHandler.sendEmptyMessageDelayed(0, 10);
    }
//    @Override
//    public onMeasure(){
//
//    }
    public void doDraw() {
        int height = getHeight();
        int width = getWidth();
        int HP_length = width/2 - 30;
        Canvas canvas = mHolder.lockCanvas(null);

        paul_normal = Bitmap.createScaledBitmap(paul_normal, width / 5, height / 4, true);
        jayg = Bitmap.createScaledBitmap(jayg, width / 5, height / 4, true);
        energy_l = Bitmap.createScaledBitmap(energy_l,HP_length,height/5,true);
        energy_r = Bitmap.createScaledBitmap(energy_r,HP_length,height/5,true);
        gameover = Bitmap.createScaledBitmap(gameover, width , height / 2, true);
        you_win = Bitmap.createScaledBitmap(you_win,width/2,height/3,true);
        paul_punch = Bitmap.createScaledBitmap(paul_punch, width/5, height/4, true);
        paul_hook = Bitmap.createScaledBitmap(paul_hook, width/5, height/4, true);
        paul_upper = Bitmap.createScaledBitmap(paul_upper, width/5, height/4, true);

////////////////////////////////////////////draw pictures /////////////////////////////////////////////////
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bm, 0, 0, null);
        canvas.drawBitmap(jayg,width - width * 9 / 20,height - height / 3, null);


        /////////energybar method////////

        energy_rect_l_src.set(HP_length * damage_l / 10, 0, HP_length, height/5);
        energy_rect_l_dst.set(HP_length * damage_l / 10, 0, HP_length, height/5);

        energy_rect_r_src.set(0, 0, HP_length - HP_length * damage_r / 10, height/5);
        energy_rect_r_dst.set(width - HP_length,0,width - HP_length * damage_r / 10,height/5);
        if (status_l == 1) canvas.drawBitmap(energy_l, energy_rect_l_src, energy_rect_l_dst, null);
        if (status_r == 1) canvas.drawBitmap(energy_r, energy_rect_r_src, energy_rect_r_dst, null);

        if (damage_r >= 9){
            if(damage_r == 9){
                count_r += 1;
                if (count_r > 3) {
                    count_r = 0;
                    status_r *= -1;
                }
            }
            else canvas.drawBitmap(you_win,width/4, height / 3, null);

        }
        if (damage_l >= 9){
            if(damage_l == 9){
                count_l += 1;
                if (count_l > 3) {
                    count_l = 0;
                    status_l *= -1;
                }
            }
            else canvas.drawBitmap(gameover, 0, 200, null);

        }


        /////////////////////combat method////////////////////
        /////combat me//////
        switch(action_my) {
            case 0:
                canvas.drawBitmap(paul_normal, width / 4, height - height / 3, null);
                break;
            case 1:
                canvas.drawBitmap(paul_punch, width / 4, height - height / 3, null);

                if (PUNCH_DELAY == 0) {
                    PUNCH_DELAY = 10;
                }
                break;
            case 2:
                canvas.drawBitmap(paul_upper, width / 4, height - height / 3, null);

                if (UPPER_DELAY == 0) {
                    UPPER_DELAY = 10;
                }
                break;
            case 3:
                canvas.drawBitmap(paul_hook, width / 4, height - height / 3, null);

                if (HOOK_DELAY == 0) {
                    HOOK_DELAY = 10;
                }
                break;
        }


        /////combat opponent
/*
        if (action_opponent == 1) {
            //canvas.drawBitmap(upper_toright,300,500,null);
            damage_l++;
        }else if(action_opponent == 2) {
            //canvas.drawBitmap(hook_toright,300,500,null);
            damage_l++;
        }else if (action_opponent == 3) {
            //canvas.drawBitmap(punch_toright,300,500,null);
            damage_l++;
        }
*/
        mHolder.unlockCanvasAndPost(canvas);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread=new Thread(this);
        mThread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mThread = null;
    }

    /////////function for opponent's random attack//////////
    public void main(String[] args){
        Random generator = new Random();
        int num ;
        num= generator.nextInt(3) + 1;////random number from 1-3
        action_opponent = num;

        if(action_opponent == 1){
            damage_l++;
        }else if(action_opponent == 2){
            damage_l++;
        }else if(action_opponent == 3){
            damage_l++;
        }
    }

    public void run(){
        while(mThread != null){
            doDraw();
            if (PUNCH_DELAY > 0) PUNCH_DELAY -= 1;
            if (UPPER_DELAY > 0)  UPPER_DELAY -= 1;
            if (HOOK_DELAY > 0)  HOOK_DELAY -= 1;
            if(PUNCH_DELAY==0 && UPPER_DELAY==0 && HOOK_DELAY==0) action_my=0;
        }
    }

    public void punch(){
        action_my = 1;
        damage_r++;
        Log.d(TAG, "punch");
    }
    public void upper(){
        action_my = 2;
        damage_r++;
        Log.d(TAG, "upper");
    }
    public void hook(){
        action_my = 3;
        damage_r++;
        Log.d(TAG, "hook");

    }
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
    };


}

