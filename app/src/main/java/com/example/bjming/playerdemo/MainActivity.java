package com.example.bjming.playerdemo;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mHasplay;
    private TextView mDuration;
    private SeekBar mSeekBar;
    private ImageButton mBackward;
    private ImageButton mPlay;
    private ImageButton mForward;
    private View mControler;
    private VideoView mVideoView;
    private PopupWindow mControlerPopupWindow;
    private int mWidth;
    private int mHeight;
    private LinkedList<Movie> list;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    //获取视频当前播放的位置(时间  毫秒)
                    int position = mVideoView.getCurrentPosition();
                    //将视频当前播放的位置给seekbar设置进度
                    mSeekBar.setProgress(position);
                    //将毫秒转成秒
                    position /= 1000;
                    //总的分数
                    int minute = position/60;
                    //小时
                    int hour = minute/60;
                    //总秒数余60取秒
                    int seconds = position%60;
                    //总得分数余60取分
                    minute %= 60;
                    mHasplay.setText(String.format("%02d:%02d:%02d", hour,minute,seconds));
                    //发送消息给handler  延迟1秒发送
                    sendEmptyMessageDelayed(1, 1000);
                    break;

                case 2:

                    break;
                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new LinkedList<Movie>();
        getSDcardMovie(list, new File("/sdcard/"));
        //获取controler视图
        LayoutInflater inflater = LayoutInflater.from(this);
        mControler = inflater.inflate(R.layout.mycontroler, null);
        //从控制台视图里面查找其相应的控件
        mHasplay = (TextView) mControler.findViewById(R.id.hasplay);
        mDuration = (TextView) mControler.findViewById(R.id.duration);
        mSeekBar = (SeekBar) mControler.findViewById(R.id.seekbar);
        mBackward = (ImageButton) mControler.findViewById(R.id.backward);
        mForward = (ImageButton) mControler.findViewById(R.id.forward);
        mPlay = (ImageButton) mControler.findViewById(R.id.play);

        mVideoView = (VideoView) findViewById(R.id.videoview);
        //将控制台视图加进popupwindow
        mControlerPopupWindow = new PopupWindow(mControler);
        //获取屏幕的宽和高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        //当主线程空闲的时候执行
        Looper.myQueue().addIdleHandler(new IdleHandler() {

            @Override
            public boolean queueIdle() {
                if(mControlerPopupWindow != null && mVideoView.isShown()){
                    mControlerPopupWindow.showAtLocation(mVideoView, Gravity.BOTTOM, 0, 0);
                    //将popupwindow呈现在父容器当中
                    mControlerPopupWindow.update(0, 0, mWidth, mHeight/4);
                }
                return false;
            }
        });
        mVideoView.setonpreparedlistener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                //返回的是毫秒
                int i = mVideoView.getDuration();
                //将视频总的毫秒数设置成seekbar的最大值
                mSeekBar.setMax(i);
                //转变成秒
                i /= 1000;
                //总的分数
                int minute = i/60;
                //小时
                int hour = minute/60;
                //总秒数余60取秒
                int seconds = i%60;
                //总得分数余60取分
                minute %= 60;
                mDuration.setText(String.format("%02d:%02d:%02d", hour,minute,seconds));
                mPlay.setImageResource(R.drawable.pause);
                //给handler发送消息
                handler.sendEmptyMessage(1);
            }
        });
    }

    @Override
    protected void onResume() {
        //与用户交互时开启视频
        mVideoView.setVideoPath(list.get(0).path);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //释放mediaplayer资源
        mVideoView.stopVideo();
        super.onDestroy();
    }


    private void getSDcardMovie(final LinkedList<Movie> list, File file){
        //查找sdcard底下的子文件
        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                //获取文件名称
                String name = pathname.getName();
                //获取文件名称中.的位置
                int i = name.indexOf('.');
                //pathname是文件夹的时候   i=-1
                if(i!=-1){
                    //.txt   .mp4
                    name = name.substring(i);
                    if(name.equalsIgnoreCase(".mp4")){
                        Movie movie = new Movie();
                        movie.path = pathname.getAbsolutePath();
                        movie.name = pathname.getName();
                        list.add(movie);
                        return true;
                    }
                }else if(pathname.isDirectory()){
                    //递归遍历文件夹
                    getSDcardMovie(list,pathname);
                }
                return false;
            }
        });
    }

}