package com.example.bjming.playerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
//private VideoView mVideoView;
/**
 * Created by bjming on 5/16/16.
 */
public class PlayToolActivity extends Activity {
    private TextView mHasplay;
    private TextView mDuration;
    private SeekBar mSeekBar;
    private ImageButton mBackward;
    private ImageButton mPlay;
    private ImageButton mForward;
    private View mController;
    private VideoView mVidwoView;
    private PopupWindow mControlerPopuWindow;
    private int mWidth;
    private int mHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        获取Controler视图
        LayoutInflater inflater=LayoutInflater.from(this);
        mController=inflater.inflate(R.layout.mycontroler, null);
        mHasplay=(TextView) mController.findViewById(R.id.hasplay);
        mDuration=(TextView) mController.findViewById(R.id.duration);
        mSeekBar=(SeekBar) mController.findViewById(R.id.seekbar);
        mBackward=(ImageButton)mController.findViewById(R.id.backward);
        mPlay=(ImageButton)mController.findViewById(R.id.forward);
        //.......
        mVidwoView=(VideoView)findViewById(R.id.videoview);
        //将控制台视图加进popuwindow当中
        mControlerPopuWindow=new PopupWindow(mController);
        //获取屏幕的宽和高
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth=dm.widthPixels;
        mHeight=dm.heightPixels;
        //当主线程空闲的时候执行
        //这里的looper主线程里的looper
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                if(mControlerPopuWindow!=null){
//                if(mControlerPopuWindow!=null&&mVideoView.isShow()){
//                    mControlerPopuWindow.showAtLocation(mVideoView, Gravity.bottom,0);
                    //将
//                    mControlerPopupWindow.update(0, 0, mWidth, mHeight/4);
                }
                return false;
            }
        });

    }


}
