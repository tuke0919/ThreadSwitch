package com.baidu.tuke_demo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 通用HandlerThread工具，提供简单业务队列的子线程实现，避免多线程互斥的使用
 * @author tuke
 */
public class CommonHandlerThread {

	private static final String TAG = CommonHandlerThread.class.getSimpleName();

	private static CommonHandlerThread sInstance = null;

	//默认消息 可以定义更多
	public static final int MSG_ID_DEFAULT = 1;
	//消息2
	public static final int MSG_ID_DEFAULT_2 = 2;

	/**
	 * 线程Handler
	 */
	private HandlerThread mHandlerThread = null;
	/**
	 * 处理消息的Handler
	 */
	private Handler mHandler = null;

	/**
	 * 回调集合
	 */
	private List<Callback> mCallbacks = new ArrayList<Callback>();


	protected CommonHandlerThread() {
		//初始化
		init();
	}
	//单例
	public static CommonHandlerThread getInstance() {
		if ( sInstance == null ) {
			synchronized (CommonHandlerThread.class) {
				if ( sInstance == null ) {
					sInstance = new CommonHandlerThread();
				}
			}
		}
		return sInstance;
	}
	
	public Handler getHandler() {
		return mHandler;
	}
	
	public Looper getLooper() {
		if ( mHandlerThread == null ) {
			return null;
		}
		return mHandlerThread.getLooper();
	}

	/**
	 * 注册回调
	 * @param cb
	 */
	public void registerCallback(Callback cb) {
		if ( cb != null && !mCallbacks.contains(cb )) {
			cb.careAbouts();
			mCallbacks.add(cb);
		}
	}

	/**
	 * 移除回调
	 * @param cb
	 */
	public void unregisterCallback(Callback cb) {
		if ( cb != null && mCallbacks.contains(cb)) {
			mCallbacks.remove(cb);
		}
	}

	/**
	 * 移除消息
	 * @param what
	 */
	public void removeMessage(int what) {
		if ( mHandler != null && mHandler.hasMessages(what)) {
			mHandler.removeMessages(what);
		}
	}

	/**
	 * 发送消息
	 * @param what
	 * @return
	 */
	public boolean sendMessage(int what) {
		return sendMessage(what, 0, 0, null, 0);
	}

	/**
	 * 发送消息
	 * @param what
	 * @param arg1
	 * @param arg2
	 * @param obj
	 * @param delayTime
	 * @return
	 */
	public boolean sendMessage(int what, int arg1, int arg2, Object obj, long delayTime) {
		if ( mHandler == null ) {
			return false;
		}
		Message message = mHandler.obtainMessage(what);
		message.arg1 = arg1;
		message.arg2 = arg2;
		if ( obj != null ) {
			message.obj = obj;
		}
		if ( delayTime <= 0 ) {
			mHandler.sendMessage(message);
		} else {
			mHandler.sendMessageDelayed(message, delayTime);
		}
		return true;
	}
	
	/**
	 * 回调逻辑执行类
	 *
	 * */
	public static abstract class Callback {

		//关注的消息ID
		private Set<Integer> mCareMsgs = new HashSet<Integer>();
		
		/**
		 *
		 * 注册关注的消息，请调用 careAbout(msgID)方法关注
		 * */
		public abstract void careAbouts();
		/**
		 * 执行
		 * */
		public abstract void execute(Message message);
		/**
		 * 注册单个关注的消息
		 * */
		public final void careAbout(int msgID) {
			mCareMsgs.add(msgID);
		}
		/**
		 * 检测是否关系指定消息
		 * */
		public final boolean isCareAbout(int msgID) {
			return mCareMsgs.contains(msgID);
		}
		/**
		 * 一个标识名
		 * */
		public String getName() {
			return "default";
		}
	}

	/**
	 * 初始化
	 */
	private void init() {

		if (mHandlerThread != null ) {
			return;
		}
		mHandlerThread = new HandlerThread("CommonHandlerThread");
		mHandlerThread.start();

		mHandler = new Handler(mHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				//线程中处理消息

				//遍历回调消息集合
				for (int i = mCallbacks.size() - 1 ; i >= 0 && i < mCallbacks.size() && mCallbacks.get(i) != null ; i-- ) {
					//如果关注多个消息 消息是串行处理的

					CommonHandlerThread.Callback callback = mCallbacks.get(i);
					//如果是关注的消息
					if (callback != null && callback.isCareAbout(msg.what)) {
						callback.execute(msg);
					}
				}
			}
		};
	}
}
