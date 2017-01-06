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

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hao.common.manager.AppManager;

public class SPUtil {
    private static SharedPreferences mSharedPreferences;

    private SPUtil() {
    }

    private static SharedPreferences getPreferneces() {
        if (mSharedPreferences == null) {
            synchronized (SPUtil.class) {
                if (mSharedPreferences == null) {
                    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(AppManager.getApp());
                }
            }
        }
        return mSharedPreferences;
    }

    public static void clear() {
        getPreferneces().edit().clear().apply();
    }

    public static void putString(String key, String value) {
        getPreferneces().edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getPreferneces().getString(key, null);
    }

    public static void putInt(String key, int value) {
        getPreferneces().edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return getPreferneces().getInt(key, 0);
    }

    public static void putBoolean(String key, Boolean value) {
        getPreferneces().edit().putBoolean(key, value).apply();
    }

    public static void putLong(String key, long value) {
        getPreferneces().edit().putLong(key, value).apply();
    }

    public static long getLong(String key) {
        return getPreferneces().getLong(key, 0);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPreferneces().getBoolean(key, defValue);
    }

    public static void remove(String key) {
        getPreferneces().edit().remove(key).apply();
    }

    public static boolean hasKey(String key) {
        return getPreferneces().contains(key);
    }
}