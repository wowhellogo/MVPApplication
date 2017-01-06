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

import android.text.TextUtils;
import android.widget.EditText;

public class StringUtil {
    private StringUtil() {
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !TextUtils.isEmpty(str);
    }

    /**
     * EditText 是否为空
     *
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        return isEmpty(editText.getText().toString().trim());
    }

    /**
     * 比较两个字符串是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqual(CharSequence a, CharSequence b) {
        return TextUtils.equals(a, b);
    }

    /**
     * 比较两个字符串是否不相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isNotEqual(CharSequence a, CharSequence b) {
        return !TextUtils.equals(a, b);
    }
}