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

import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import com.hao.common.R;
import com.hao.common.manager.AppManager;
import com.hao.common.rx.RxUtil;


public class ToastUtil {

    private ToastUtil() {
    }

    public static void show(CharSequence text) {
        if (StringUtil.isNotEmpty(text)) {
            Toast toast;
            if (text.length() < 10) {
                toast = Toast.makeText(AppManager.getApp(), text, Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(AppManager.getApp(), text, Toast.LENGTH_LONG);
            }
            DrawableUtil.tintBackground(toast.getView(), R.color.colorPrimary);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, UIUtil.dp2px(2));
            toast.show();
        }
    }

    public static void show(@StringRes int resId) {
        show(AppManager.getApp().getResources().getString(resId));
    }

    public static void showSafe(final CharSequence text) {
        try {
            if (AppManager.getInstance().isFrontStage()) {
                RxUtil.runInUIThread(text).subscribe(msg -> show(msg));
            }
        } catch (Exception e) {
        }
    }

    public static void showSafe(@StringRes int resId) {
        showSafe(AppManager.getApp().getResources().getString(resId));
    }

    public static void showSafeInDebug(CharSequence text) {
        if (AppManager.getInstance().isBuildDebug()) {
            showSafe("Debug: " + text);
        }
    }
}