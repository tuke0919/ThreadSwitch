package com.baidu.tuke_demo.worker;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * <p/>
 * 功能 :处理主线程任务
 * <p/>
 * <p>
 * <p>Copyright baidu.com 2018 All right reserved</p>
 *
 * @author tuke 时间 2018/7/20
 * @email tuke@baidu.com
 * <p>
 * 最后修改人 无
 */
public class MainTaskHandler extends Handler {

    /** 默认消息 */
    public static final int MSG_DEFAULT = 1;
    /** 其他类型消息 */
    public static final int MSG_OTHER = 2;

    public MainTaskHandler(){
        super(Looper.getMainLooper());
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what){
            case MSG_DEFAULT :
                if(msg.obj != null && msg.obj instanceof ThreadTask){
                    ((ThreadTask) msg.obj).onWork();
                }
                break;
            case MSG_OTHER:
                Log.e("mainThreadWork"," mainThreadWork is null or not instanceof  ThreadTask.");
                break;
        }
    }
    /**
     * 发送主线线程任务
     * @param callBack
     */
    public void executeMainWork(ThreadTask callBack){
        sendMessage(obtainMessage((callBack != null && callBack instanceof ThreadTask) ? MSG_DEFAULT : MSG_OTHER,callBack));
    }

    /**
     * 发送主线线程延时 任务
     * @param callBack
     * @param delayMillis  延时时间
     */
    public void executeMainWorkDelay (ThreadTask callBack , long delayMillis ){
        sendMessageDelayed(obtainMessage((callBack != null && callBack instanceof ThreadTask) ? MSG_DEFAULT : MSG_OTHER,callBack),delayMillis);
    }

}
