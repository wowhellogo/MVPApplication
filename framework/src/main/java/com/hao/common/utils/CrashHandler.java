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

import android.os.Build;

import com.hao.common.manager.AppManager;
import com.orhanobut.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

public class CrashHandler implements UncaughtExceptionHandler {
    private static CrashHandler sInstance;
    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private CrashHandler() {
    }

    public static final CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    sInstance = new CrashHandler();
                }
            }
        }
        return sInstance;
    }

    public void init() {
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex != null) {
            if (mDefaultUncaughtExceptionHandler != null) {
                mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
            }

            handleException(thread, ex);
        }
    }

    private void handleException(Thread thread, Throwable ex) {
        ToastUtil.showSafe("系统出现未知异常，即将退出...");

        collectionException(ex);

        try {
            thread.sleep(2000);
            AppManager.getInstance().exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void collectionException(Throwable ex) {
        // TODO 在 APPManager 里增加回调接口来处理上传
        Logger.e("【deviceInfo】\n" + getDeviceInfo() + "【errorInfo】\n" + getErrorInfo(ex));
    }

    private String getErrorInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return writer.toString();
    }

    private String getDeviceInfo() {
        StringBuffer sb = new StringBuffer();
        // 通过反射获取系统的硬件信息
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                // 暴力反射 ,获取私有的信息
                field.setAccessible(true);
                sb.append(field.getName() + "=" + field.get(null).toString());
                sb.append("\n");
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return sb.toString();
    }
}
