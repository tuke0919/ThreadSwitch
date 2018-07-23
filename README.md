# ThreadSwitch
一个方便的主线程和子线程之间的线程切换框架

# 使用说明
- **提交主线程任务**
当你在子线程完成了任务，比如查询数据库等，需要切换到主线程，在主线程中操作
```
 WorkerCenter.getInstance().submitMainThreadTask(new ThreadTask() {
            @Override
            public void onWork() {
                //主线程任务
            }
        });
//主线程延时任务
WorkerCenter.getInstance().submitMainThreadTaskDelay(new ThreadTask() {
            @Override
            public void onWork() {
                //主线程延迟任务
            }
        },10000);
```
- **提交子线程队列任务**
子线程执行队列任务，根据提交的先后顺序执行，无任何方式的结果通知
```
WorkerCenter.getInstance().submitQueneTask(new ThreadTask() {
            @Override
            public void onWork() {
                //子线程队列任务
            }
        });
//子线程队列延时任务
WorkerCenter.getInstance().submitQueneTaskDelay(new ThreadTask() {
            @Override
            public void onWork() {
                //子线程队列延时任务
            }
        },10000);
```
- **提交子线程任务，同时执行多个子任务**
线程池处理提交任务，同时执行。可配置 执行的结果 是在子线程中还是在主线程 执行
``` 
 WorkerCenter.getInstance().submitNormalTask(new WorkerTask<String>("workTask",true) {

            @Override
            protected String execute() {
                 //子线程中执行任务
                return "this is a test task for woker";
            }

            @Override
            protected void notifyResult(String result) {
                 //处理结果，可配置在子线程还是主线程
                if(!TextUtils.isEmpty(result)){
                    Log.e("woker","execute's result is  " + result + " 当前执行线程是否为主线程 : " + (Looper.myLooper() == Looper.getMainLooper()) );
                }
            }
        });
```
- **提交子线程阻塞任务**
提交任务 ，阻塞执行，直到子线程任务执行完成
```
WorkerCenter.getInstance().submitBlockTask(new ThreadTask() {
            @Override
            public void onWork() {
                //提交线程池 阻塞任务
            }
        });
```











