package com.example.bjming.playerdemo;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.MediaController.MediaPlayerControl;

public class VideoView extends SurfaceView implements Callback, OnPreparedListener,MediaPlayerControl{

    //video宽度和高度
    private int mWidth;
    private int mHeight;
    private MediaPlayer mMediaPlayer;
    private Context mContext;
    //文件路径
    private String path;

    private OnPreparedListener listener;

    public void setonpreparedlistener(OnPreparedListener l){
        this.listener = l;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mWidth = mp.getVideoWidth();
        mHeight = mp.getVideoHeight();
        if(listener!=null){
            listener.onPrepared(mMediaPlayer);
        }
        if(mWidth != 0 && mHeight != 0){
            //给surface设置固定的宽和高
            getHolder().setFixedSize(mWidth, mHeight);
        }
        //开始播放
        mMediaPlayer.start();
    }

    public VideoView(Context context){
        this(context, null, 0);
    }

    public VideoView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init(){
        mWidth = 0;
        mHeight = 0;
        //添加回调  使底下三个方法能够自动调用
        getHolder().addCallback(this);
    }

    //surface创建的时候调用
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openVideo();
    }

    //surface尺寸发生变化时调用
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    //surface销毁的时候调用
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    //初始化mediaplayer
    private void openVideo(){
        //防止调用这个方法产生过多的mediaplayer
        if(mMediaPlayer!=null){
            //将mediaplayer重置成未初始化状态
            mMediaPlayer.reset();
            //将与mediaplayer相关的资源释放
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        try {
            mMediaPlayer = new MediaPlayer();
            //设置音响流
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置播放视频文件的路径
            mMediaPlayer.setDataSource(mContext, Uri.parse(path));
            //设置视频在surface上面播放
            mMediaPlayer.setDisplay(getHolder());
            //注册一个回调  当视频文件准备播放的时候
            mMediaPlayer.setOnPreparedListener(this);
            //异步准备播放录像
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setVideoPath(String path){
        this.path = path;
        openVideo();
    }



    public void stopVideo(){
        if(mMediaPlayer!=null){
            //将mediaplayer重置成未初始化状态
            mMediaPlayer.reset();
            //将与mediaplayer相关的资源释放
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    //开启视频
    @Override
    public void start() {
        if(mMediaPlayer!=null)
            mMediaPlayer.start();
    }

    //暂停视频
    @Override
    public void pause() {
        if(mMediaPlayer!=null)
            mMediaPlayer.pause();

    }

    //获取视频总时间
    @Override
    public int getDuration() {
        if(mMediaPlayer!=null)
            return mMediaPlayer.getDuration();
        else
            return 0;
    }

    //获取当前视频播放的位置
    @Override
    public int getCurrentPosition() {
        if(mMediaPlayer!=null)
            return mMediaPlayer.getCurrentPosition();
        else
            return 0;
    }

    //设置当前视频播放的位置
    @Override
    public void seekTo(int pos) {
        if(mMediaPlayer!=null)
            mMediaPlayer.seekTo(pos);

    }

    //判断当前视频是否在播放
    @Override
    public boolean isPlaying() {
        if(mMediaPlayer!=null)
            return mMediaPlayer.isPlaying();
        else
            return false;
    }

    @Override
    public int getBufferPercentage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean canPause() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canSeekForward() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


}