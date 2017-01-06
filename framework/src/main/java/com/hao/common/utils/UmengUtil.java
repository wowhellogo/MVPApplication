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

package com.hao.common.utils;

import android.app.Activity;

import com.hao.common.manager.AppManager;
import com.umeng.analytics.MobclickAgent;
public class UmengUtil {
    private UmengUtil() {
    }

    /**
     * 初始化友盟 SDK，在 Application 的 onCreate 方法里调用
     */
    public static void initSdk() {
        MobclickAgent.setDebugMode(AppManager.getInstance().isBuildDebug());
        // 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setSessionContinueMillis(AppManager.getInstance().isBuildDebug() ? 3000 : 30000);
    }

    /**
     * 如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据
     */
    public static void onKillProcess() {
        MobclickAgent.onKillProcess(AppManager.getApp());
    }

    // ======================== 页面路径统计 START ========================

    /**
     * Activity 中是否包含 Fragment
     *
     * @param activity
     * @return
     */
    private static boolean isActivityNotContainFragment(Activity activity) {
        return AppManager.getInstance().isActivityNotContainFragment(activity);
    }

    /**
     * 在 ActivityLifecycleCallbacks 的 onActivityResumed 回调方法里调用
     *
     * @param activity
     */
    public static void onActivityResumed(Activity activity) {
        if (UmengUtil.isActivityNotContainFragment(activity)) {
            MobclickAgent.onPageStart(activity.getClass().getSimpleName());
        }
        MobclickAgent.onResume(activity);
    }

    /**
     * 在 ActivityLifecycleCallbacks 的 onActivityPaused 回调方法里调用
     *
     * @param activity
     */
    public static void onActivityPaused(Activity activity) {
        if (UmengUtil.isActivityNotContainFragment(activity)) {
            // 保证 onPageEnd 在 onPause 之前调用,因为 onPause 中会保存信息
            MobclickAgent.onPageEnd(activity.getClass().getSimpleName());
        }
        MobclickAgent.onPause(activity);
    }
    // ======================== 页面路径统计 START ========================
}