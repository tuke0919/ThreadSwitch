package com.baidu.tuke_demo;

import android.nfc.Tag;
import android.os.Build;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.tuke_demo.worker.WorkerCenter;
import com.baidu.tuke_demo.worker.WorkerTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Main
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testWokerTask();
            }
        });
    }

    public void testWokerTask(){

        WorkerCenter.getInstance().submitNormalTask(new WorkerTask<String>("workTask",true) {

            @Override
            protected String execute() {
                return "this is a test task for woker";
            }

            @Override
            protected void notifyResult(String result) {

                if(!TextUtils.isEmpty(result)){
                    Log.e("woker","execute's result is  " + result + " 当前执行线程是否为主线程 : " + (Looper.myLooper() == Looper.getMainLooper()) );
                }
            }
        });
    }


    public void testThreadHandler(){
        final CommonHandlerThread.Callback callback1 = new CommonHandlerThread.Callback() {
            @Override
            public void careAbouts() {
                //只关注消息1
                careAbout(CommonHandlerThread.MSG_ID_DEFAULT);
                careAbout(CommonHandlerThread.MSG_ID_DEFAULT_2);
            }

            @Override
            public void execute(Message message) {
                //关注多个消息 消息是穿行处理的

                switch (message.what){
                    case CommonHandlerThread.MSG_ID_DEFAULT :
                        //处理消息1
                        int i = 5;
                        while(i != 0){
                            try {
                                Thread.sleep(2000);
                                Log.e("callback","消息ID = " + message.what + "当前时间：" + System.currentTimeMillis() );
                                i--;

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case CommonHandlerThread.MSG_ID_DEFAULT_2:
                        //处理消息2

                        int j = 5;
                        while(j != 0){
                            try {

                                Thread.sleep(2000);
                                Log.e("callback","消息ID = " + message.what + "当前时间：" + System.currentTimeMillis() );
                                j --;

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }
        };
        //注册处理回调
        CommonHandlerThread.getInstance().registerCallback(callback1);


        //发送消息 1
        CommonHandlerThread.getInstance().sendMessage(CommonHandlerThread.MSG_ID_DEFAULT);
        CommonHandlerThread.getInstance().sendMessage(CommonHandlerThread.MSG_ID_DEFAULT_2);

    }


}
