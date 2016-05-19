package com.example.bjming.playerdemo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;


/**
 * Created by bjming on 5/16/16.
 */
public class VideoView2 extends SurfaceView implements SurfaceHolder.Callback , MediaPlayer.OnPreparedListener{
    //video的宽和高
    private int mWidth;
    private int mHeight;
    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private String path;
    private LinkedList<Movie> list;


    public void setonparedListener(MediaPlayer.OnPreparedListener listener){

    }

    public VideoView2(Context context) {
        this(context, null, 0);
    }

    public VideoView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        context=mContext;
        init();
        list=new LinkedList<Movie>();
        getSDcardMovie(list, new File("/sdcard/"));

    }
    //初始化
    private void init() {
        mWidth=0;
        mHeight=0;
        //添加回调，使底下三个方法能够自动调用

        getHolder().addCallback(this);//由于实现了callback,可以填this

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openVideo();
    }

    //surface尺寸发生变化时调用
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //surface创建的时，去打开video
    }
    //surface销毁时调用
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    //初始化medioplayer
    public void openVideo(){
        //防止调用这个方法产生过多的mediaPlayer
        if(mMediaPlayer!=null){
            //将mediaplayer重制为未初始化状态
            mMediaPlayer.reset();
            //将与mediaplayer相关的资源释放
            mMediaPlayer.release();
            mMediaPlayer=null;
        }

        try {
            mMediaPlayer=new MediaPlayer();
            //设置音响流
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置播放视频文件的路径
            mMediaPlayer.setDataSource(mContext, Uri.parse(path));
            //设置视频在哪上面播放
            mMediaPlayer.setDisplay(getHolder());
            //注册一个回调
            mMediaPlayer.setOnPreparedListener(this);
            //异步准备播放图像
            mMediaPlayer.prepareAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopVideo(){
        //防止调用这个方法产生过多的mediaPlayer
        if(mMediaPlayer!=null){
            //将mediaplayer重制为未初始化状态
            mMediaPlayer.reset();
            //将与mediaplayer相关的资源释放
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mWidth=mp.getVideoWidth();
        mHeight=mp.getVideoHeight();
        if(mWidth!=0&&mHeight!=0){
            //给surface设置固定的宽和高
            getHolder().setFixedSize(mWidth, mHeight);
        }
        //开始播放
        mMediaPlayer.start();

    }

    public void setPath(String path) {
        this.path = path;
        openVideo();//开始播放
    }

    public void onResume(){

    }
    //有问题？？？？/
    public void onDestoy(){
//        mVideoView.stopView();
//        super.onDestory();
    }

    private MediaPlayer.OnPreparedListener listener;
    private void setonpreparedlistener(MediaPlayer.OnPreparedListener l){
        this.listener=l;
    }
    //判断当前视频播放位置
    public void seekTo(int pos){
        if(mMediaPlayer!=null){
            mMediaPlayer.seekTo(pos);
        }
    }
    //判断当前视频是否在播放
    public boolean isPlaying(){
        if(mMediaPlayer!=null)
            return mMediaPlayer.isPlaying();
        else return false;
    }



    private void getSDcardMovie(final LinkedList<Movie> list, File file){
//        查找sdcard下的子文件
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
//                获取文件名称
                String name=pathname.getName();
                //获取文件的.的位置
                int i=name.indexOf(".");
                if(i!=-1) {
                    //.txt .mp4过滤
                    name = name.substring(i);
                    //???????
                    if (name.equalsIgnoreCase(".mp4")) {
                        Movie movie = new Movie();
                        movie.path = pathname.getAbsolutePath();
                        movie.name = pathname.getName();
                        list.add(movie);
                    }
                }else if(pathname.isDirectory()){
                    getSDcardMovie(list, pathname);
                }
                return false;
            }
        });
    }
}