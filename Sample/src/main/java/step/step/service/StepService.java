package step.step.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luseen.spacenavigationview.R;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import step.activity.MainActivitytest;
import step.step.accelerometer.StepValuePassListener;
import step.step.utils.DbUtils;
import step.step.UpdateUiCallBack;
import step.step.accelerometer.StepCount;
import step.step.bean.StepData;

public class StepService extends Service implements SensorEventListener {
    private String TAG = "StepService";
    /**
     * 默認為30秒進行一次存儲
     */
    private static int duration = 30 * 1000;
    /**
     * 當前的日期
     */
    private static String CURRENT_DATE = "";
    /**
     * 傳感器管理對象
     */
    private SensorManager sensorManager;
    /**
     * 廣播接受者
     */
    private BroadcastReceiver mBatInfoReceiver;
    /**
     * 保存計步計時器
     */
    private TimeCount time;
    /**
     * 當前所走的步數
     */
    private int CURRENT_STEP;
    /**
     * 計步傳感器類型  Sensor.TYPE_STEP_COUNTER或者Sensor.TYPE_STEP_DETECTOR
     */
    private static int stepSensorType = -1;
    /**
     * 每次第一次啟動計步服務時是否從系统中獲取了已有的步數紀錄
     */
    private boolean hasRecord = false;
    /**
     * 系统中獲取到的已有的步數
     */
    private int hasStepCount = 0;
    /**
     * 上一次的步數
     */
    private int previousStepCount = 0;
    /**
     * 通知管理對象
     */
    private NotificationManager mNotificationManager;
    /**
     * 加速度传感器中獲取的步數
     */
    private StepCount mStepCount;
    /**
     * IBinder對象，向Activity傳遞數據的橋梁
     */
    private StepBinder stepBinder = new StepBinder();
    /**
     * 通知構建者
     */
    private NotificationCompat.Builder mBuilder;
    private String uname;

    DatabaseReference reference;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
//        initNotification();
        initTodayData();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                startStepDetector();
            }
        }).start();
        startTimeCount();
    }



    /**
     * 获取当天日期
     *
     * @return
     */
    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 初始化通知栏
     */
//    private void initNotification() {
//        mBuilder = new NotificationCompat.Builder(this);
//        mBuilder.setContentTitle(getResources().getString(R.string.app_name))
//                .setContentText("今日步数" + CURRENT_STEP + " 步")
//                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
//                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
//                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//                .setAutoCancel(false)//设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setSmallIcon(R.mipmap.logo);
//        Notification notification = mBuilder.build();
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        startForeground(notifyId_Step, notification);
//        Log.d(TAG, "initNotification()");
//    }

    /**
     * 初始化當天步數
     */
    private void initTodayData() {
        CURRENT_DATE = getTodayDate();
        DbUtils.createDb(this, "DylanStepCount");
        DbUtils.getLiteOrm().setDebugged(false);
        //獲取當天數據，用於展示
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENT_DATE});
        if (list.size() == 0 || list.isEmpty()) {
            CURRENT_STEP = 0;
        } else if (list.size() == 1) {
            Log.v(TAG, "StepData=" + list.get(0).toString());
            CURRENT_STEP = Integer.parseInt(list.get(0).getStep());
        } else {
            Log.v(TAG, "出错了！");
        }
        if (mStepCount != null) {
            mStepCount.setSteps(CURRENT_STEP);
        }
        // updateNotification();
    }

    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d(TAG, "screen off");
                    //改为60秒一存储
                    duration = 60000;
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.d(TAG, "screen unlock");
                    duration = 30000;
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //保存一次
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    Log.i(TAG, " receive ACTION_SHUTDOWN");
                    save();
                } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                    isCall();
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_TICK.equals(action)) {
                    isCall();
                    save();
                    isNewDay();
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }

    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date())) || !CURRENT_DATE.equals(getTodayDate())) {
            initTodayData();
        }
    }

    private void isCall() {
        String time = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("achieveTime", "21:00");
        String plan = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("planWalk_QTY", "7000");
        String remind = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("remind", "1");
        Logger.d("time=" + time + "\n" +
                "new SimpleDateFormat(\"HH: mm\").format(new Date()))=" + new SimpleDateFormat("HH:mm").format(new Date()));
        if (("1".equals(remind)) &&
                (CURRENT_STEP < Integer.parseInt(plan)) &&
                (time.equals(new SimpleDateFormat("HH:mm").format(new Date())))
                ) {
            remindNotify();
        }

    }

    //開始保存計步
    private void startTimeCount() {
        if (time == null) {
            time = new TimeCount(duration, 1000);
        }
        time.start();
    }

    private void updateNotification() {
        Intent hangIntent = new Intent(this, MainActivitytest.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("今日步数" + CURRENT_STEP + " 步")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(hangPendingIntent)
                .build();
        mNotificationManager.notify(notifyId_Step, notification);
        if (mCallback != null) {
            mCallback.updateUi(CURRENT_STEP);
        }
        Log.d(TAG, "updateNotification()");
    }

    /**
     * UI監聽器對象
     */
    private UpdateUiCallBack mCallback;

    /**
     * 註冊UI更新監聽
     *
     * @param paramICallback
     */
    public void registerCallback(UpdateUiCallBack paramICallback) {
        this.mCallback = paramICallback;
    }

    /**
     * 計步Notification的ID
     */
    int notifyId_Step = 100;
    /**
     * 提醒锻炼的Notification的ID
     */
    int notify_remind_id = 200;

    private void remindNotify() {

        Intent hangIntent = new Intent(this, MainActivitytest.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        String plan = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("planWalk_QTY", "7000");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("今日步數" + CURRENT_STEP + " 步")
                .setContentText("距离目标还差" + (Integer.valueOf(plan) - CURRENT_STEP) + "步，加油！")
                .setContentIntent(hangPendingIntent)
                .setTicker(getResources().getString(R.string.app_name) + "提醒您開始鍛鍊了")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.logo);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notify_remind_id, mBuilder.build());
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stepBinder;
    }

    public class StepBinder extends Binder {

        public StepService getService() {
            return StepService.this;
        }
    }

    public int getStepCount() {
        return CURRENT_STEP;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        uname = intent.getExtras().getString("uname");
        reference = FirebaseDatabase.getInstance().getReference("Steps").child(uname);
        return START_STICKY;
    }

    /**
     * 获取传感器实例
     */
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);
        //android4.4以后可以使用计步传感器
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        } else {
            addBasePedometerListener();
        }
    }

    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_COUNTER;
            Log.v(TAG, "Sensor.TYPE_STEP_COUNTER");
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_DETECTOR;
            Log.v(TAG, "Sensor.TYPE_STEP_DETECTOR");
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.v(TAG, "Count sensor not available!");
            addBasePedometerListener();
        }
    }

    /**
     * 傳感器監聽回調
     * 計步的關鍵代碼
     * 1. TYPE_STEP_COUNTER API的解釋說返回從開機被激活後统計的步數，當重啟手機後該數據歸零，
     * 該傳感器是一個硬件傳感器所以它是低功耗的。
     * 為了能持續的計步，不能重新註冊登入，就算手機處於休眠狀態它依然會計步。
     * API文檔也確實是這樣說的，該sensor只用来監測走步，每次返回數字1.0。
     * 如果需要長事件的計步要使用TYPE_STEP_COUNTER。
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            int tempStep = (int) event.values[0];
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                //獲取APP打開到现在的總步數=本次系统回調的總步數-APP打開之前已有的步數
                int thisStepCount = tempStep - hasStepCount;
                //本次有效步數=（APP打開後所紀錄的總步數-上一次APP打開後所紀錄的總步數）
                int thisStep = thisStepCount - previousStepCount;
                //總步數=現有的步數+本次有效步數
                CURRENT_STEP += (thisStep);
                //紀錄最後一次APP打開到現在的總步數
                previousStepCount = thisStepCount;
            }
            Logger.d("tempStep" + tempStep);
        } else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                CURRENT_STEP++;
            }
        }
        // updateNotification();
    }

    /**
     * 通過加速度傳感器來計步
     */
    private void addBasePedometerListener() {
        mStepCount = new StepCount();
        mStepCount.setSteps(CURRENT_STEP);
        // 獲得傳感器的類型，這裡獲得的類型是加速度傳感器
        // 此方法用來註冊，只有註冊過才會生效，參數：SensorEventListener的實例，Sensor的實例，更新速率
        Sensor sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean isAvailable = sensorManager.registerListener(mStepCount.getStepDetector(), sensor,
                SensorManager.SENSOR_DELAY_UI);
        mStepCount.initListener(new StepValuePassListener() {
            @Override
            public void stepChanged(int steps) {
                CURRENT_STEP = steps;
//                updateNotification();
            }
        });
        if (isAvailable) {
            Log.v(TAG, "加速度傳感器可以使用");
        } else {
            Log.v(TAG, "加速度傳感器無法使用");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 保存計步數據
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果計時器正常结束，則開始計步
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    /**
     * 保存計步數據!!!!!!!!!!
     */
    private void save() {
        int tempStep = CURRENT_STEP;
        //if (tempStep % 20 == 0)
           updateFirebase(tempStep);

        Log.d("save1", tempStep+"");
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENT_DATE});
        if (list.size() == 0 || list.isEmpty()) {
            StepData data = new StepData();
            data.setToday(CURRENT_DATE);
            Log.d("save2", tempStep+"");
            data.setStep(tempStep + "");
            DbUtils.insert(data);
        } else if (list.size() == 1) {
            Log.d("save3", tempStep+"");
            StepData data = list.get(0);
            data.setStep(tempStep + "");
            DbUtils.update(data);
        }
    }

    //時間是否需要
    public void updateFirebase(int step) {
        HashMap<String, Object> hashMap = new HashMap<>();
        // hashMap.put("time", Calendar.getInstance().getTime());
        hashMap.put("step", step);

        reference.setValue(hashMap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消前台進程
        stopForeground(true);
        DbUtils.closeDb();
        unregisterReceiver(mBatInfoReceiver);
        Logger.d("stepService關閉");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
