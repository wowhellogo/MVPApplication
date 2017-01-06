/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hao.common.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

import com.hao.common.R;
import com.hao.common.exception.ApiException;
import com.hao.common.rx.RxBus;
import com.hao.common.rx.RxEvent;
import com.hao.common.utils.CrashHandler;
import com.hao.common.utils.ToastUtil;
import com.hao.common.utils.UmengUtil;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.Iterator;
import java.util.Stack;


import static android.content.Context.ACTIVITY_SERVICE;

/**
 * App管理类
 */
public class AppManager implements Application.ActivityLifecycleCallbacks {
    private static final AppManager sInstance;
    private static final Application sApp;

    private int mActivityStartedCount = 0;
    private long mLastPressBackKeyTime;
    private Stack<Activity> mActivityStack = new Stack<>();

    private boolean mIsBuildDebug;
    private Delegate mDelegate;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(AppManager.class.getSimpleName(), "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(AppManager.class.getSimpleName(), "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                try {
                    Class.forName("android.os.AsyncTask");
                } catch (ClassNotFoundException e) {
                }
            }

            sApp = app;

            sInstance = new AppManager();

            sApp.registerActivityLifecycleCallbacks(sInstance);
        }
    }

    private AppManager() {
        // 初始化崩溃日志统计
        CrashHandler.getInstance().init();

        sApp.registerReceiver(new BroadcastReceiver() {
            private boolean mIsFirstReceiveBroadcast = true;

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (!mIsFirstReceiveBroadcast) {
                        if (NetUtil.isNetworkAvailable()) {
                            RxBus.send(new RxEvent.NetworkConnectedEvent());
                        } else {
                            RxBus.send(new RxEvent.NetworkDisconnectedEvent());
                        }
                    } else {
                        mIsFirstReceiveBroadcast = false;
                    }
                }
            }
        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * 必须在 Application 的 onCreate 方法中调用
     *
     * @param isBuildDebug 是否构建的是 debug
     */
    public void init(boolean isBuildDebug, Delegate delegate) {
        mIsBuildDebug = isBuildDebug;
        mDelegate = delegate;

        // 初始化日志打印库
        Logger.init(getInstance().getAppName()).logLevel(mIsBuildDebug ? LogLevel.FULL : LogLevel.NONE);
    }

    public static AppManager getInstance() {
        return sInstance;
    }

    public static Application getApp() {
        return sApp;
    }

    /**
     * 是否构建的是 debug
     *
     * @return
     */
    public boolean isBuildDebug() {
        return mIsBuildDebug;
    }

    /**
     * LeakCanary 监控 Fragment 的内存泄露
     *
     * @param fragment
     */
    public void refWatcherWatchFragment(Fragment fragment) {
        if (mDelegate != null) {
            mDelegate.refWatcherWatchFragment(fragment);
        }
    }

    /**
     * Activity 是否包含 Fragment。用于处理友盟页面统计，避免重复统计 Activity 和 Fragment
     *
     * @param activity
     * @return
     */
    public boolean isActivityNotContainFragment(Activity activity) {
        if (mDelegate != null) {
            return mDelegate.isActivityNotContainFragment(activity);
        }
        return true;
    }

    /**
     * 处理全局网络请求异常
     *
     * @param apiException
     */
    public void handleServerException(ApiException apiException) {
        if (mDelegate != null) {
            mDelegate.handleServerException(apiException);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityStack.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mActivityStartedCount == 0) {
            RxBus.send(new RxEvent.AppEnterForegroundEvent());
        }
        mActivityStartedCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        UmengUtil.onActivityResumed(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        UmengUtil.onActivityPaused(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mActivityStartedCount--;
        if (mActivityStartedCount == 0) {
            RxBus.send(new RxEvent.AppEnterBackgroundEvent());
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityStack.remove(activity);
    }


    /**
     * Activity栈是否是空的
     *
     * @return
     */
    public boolean isActivityStackEmpty() {
        return mActivityStack.isEmpty();
    }

    /**
     * Activity栈中Activity的个数
     *
     * @return
     */
    public int getActivityStackSize() {
        return mActivityStack.size();
    }

    /**
     * 获取当前栈顶Activity
     *
     * @return
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (!mActivityStack.empty()) {
            activity = mActivityStack.lastElement();
        }
        return activity;
    }

    /**
     * 移除指定Activity
     *
     * @param activity
     */
    public void popOneActivity(Activity activity) {
        if (activity == null || mActivityStack.isEmpty()) {
            return;
        }
        if (!activity.isFinishing()) {
            activity.finish();
        }
        mActivityStack.remove(activity);
    }

    /**
     * 应用场景：支付完后，关闭 MainActivity 之外的其他页面
     *
     * @param activityClass
     */
    public void popOthersActivity(Class<Activity> activityClass) {
        if (activityClass == null || mActivityStack.isEmpty()) {
            return;
        }

        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().equals(activityClass)) {
                activity.finish();
                iterator.remove();
            }
        }
    }

    /**
     * 双击后 全退出应用程序
     */
    public void exitWithDoubleClick() {
        if (System.currentTimeMillis() - mLastPressBackKeyTime <= 1500) {
            exit();
        } else {
            mLastPressBackKeyTime = System.currentTimeMillis();
            ToastUtil.show(R.string.toast_exit_tip);
        }
    }

    /**
     * 关闭所有Activity
     */
    public void finishAllActivity() {
        try {
            while (true) {
                Activity activity = currentActivity();
                if (activity == null) {
                    break;
                }
                popOneActivity(activity);
            }
        } catch (Exception e) {
            Logger.e("关闭所有Activity错误");
        }
    }

    /**
     * 退出应用程序
     */
    public void exit() {
        try {
            finishAllActivity();

            // 如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据
            UmengUtil.onKillProcess();

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            Logger.e("退出错误");
        }
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    public String getAppName() {
        try {
            return sApp.getPackageManager().getPackageInfo(sApp.getPackageName(), 0).applicationInfo.loadLabel(sApp.getPackageManager()).toString();
        } catch (Exception e) {
            // 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
            return "";
        }
    }

    /**
     * 获取当前版本名称
     *
     * @return
     */
    public String getCurrentVersionName() {
        try {
            return sApp.getPackageManager().getPackageInfo(sApp.getPackageName(), 0).versionName;
        } catch (Exception e) {
            // 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
            return "";
        }
    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    public int getCurrentVersionCode() {
        try {
            return sApp.getPackageManager().getPackageInfo(sApp.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            // 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
            return 0;
        }
    }

    /**
     * 获取渠道号
     *
     * @return
     */
    private String getChannel() {
        try {
            ApplicationInfo appInfo = sApp.getPackageManager().getApplicationInfo(sApp.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取倒数第二个 Activity
     *
     * @return
     */
    @Nullable
    public Activity getPenultimateActivity() {
        Activity activity = null;
        try {
            if (mActivityStack.size() > 1) {
                activity = mActivityStack.get(mActivityStack.size() - 2);
            }
        } catch (Exception e) {
        }
        return activity;
    }

    public static void onPanelSlide(float slideOffset) {
        try {
            Activity activity = getInstance().getPenultimateActivity();
            if (activity != null) {
                View decorView = activity.getWindow().getDecorView();
                ViewCompat.setTranslationX(decorView, -(decorView.getMeasuredWidth() / 3.0f) * (1 - slideOffset));
            }
        } catch (Exception e) {
        }
    }

    public static void onPanelClosed() {
        try {
            Activity activity = getInstance().getPenultimateActivity();
            if (activity != null) {
                View decorView = activity.getWindow().getDecorView();
                ViewCompat.setTranslationX(decorView, 0);
            }
        } catch (Exception e) {
        }
    }


    /**
     * 应用是否在后台
     *
     * @return
     */
    public boolean isBackStage() {
        return mActivityStartedCount == 0;
    }

    /**
     * 应用是否在前台
     *
     * @return
     */
    public boolean isFrontStage() {
        return mActivityStartedCount > 0;
    }

    public static boolean isInOtherProcess(Context context) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        Iterator iterator = am.getRunningAppProcesses().iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (iterator.next());
            try {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                }
            } catch (Exception e) {
                Log.d(AppManager.class.getSimpleName(), "Error>> :" + e.toString());
            }
        }
        return processName == null || !processName.equalsIgnoreCase(context.getPackageName());
    }

    public interface Delegate {

        /**
         * LeakCanary 监控 Fragment 的内存泄露
         *
         * @param fragment
         */
        void refWatcherWatchFragment(Fragment fragment);

        /**
         * Activity 是否包含 Fragment。用于处理友盟页面统计，避免重复统计 Activity 和 Fragment
         *
         * @param activity
         * @return
         */
        boolean isActivityNotContainFragment(Activity activity);

        /**
         * 处理全局网络请求异常
         *
         * @param apiException
         */
        void handleServerException(ApiException apiException);

    }
}
