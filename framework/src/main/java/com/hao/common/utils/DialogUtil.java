package com.hao.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;

import com.hao.common.R;
import com.hao.common.manager.AppManager;

/**
 * @Package com.hao.common.utils
 * @作 用:对话框工具类
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月31日  17:01
 */


public class DialogUtil {
    private static AlertDialog mDialog;

    public static AlertDialog.Builder dialogBuilder(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (msg != null) {
            builder.setMessage(msg);
        }
        if (title != null) {
            builder.setTitle(title);
        }
        return builder;
    }

    public static AlertDialog.Builder dialogBuilder(Context context, String title, String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (msg != null) {
            builder.setMessage(Html.fromHtml(msg));
        }
        if (title != null) {
            builder.setTitle(title);
        }
        return builder;
    }


    public static AlertDialog.Builder dialogBuilder(Context context, int title, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (view != null) {
            builder.setView(view);
        }
        if (title > 0) {
            builder.setTitle(title);
        }
        return builder;
    }

    public static AlertDialog.Builder dialogBuilder(Context context, int titleResId, int msgResId) {
        String title = titleResId > 0 ? context.getResources().getString(titleResId) : null;
        String msg = msgResId > 0 ? context.getResources().getString(msgResId) : null;
        return dialogBuilder(context, title, msg);
    }

    public static Dialog showTips(Context context, String title, String des) {
        return showTips(context, title, des, null, null);
    }

    public static Dialog showTips(Context context, int title, int des) {
        return showTips(context, context.getString(title), context.getString(des));
    }

    public static Dialog showTips(Context context, int title, int des, int btn, DialogInterface.OnDismissListener dismissListener) {
        return showTips(context, context.getString(title), context.getString(des), context.getString(btn), dismissListener);
    }

    public static Dialog showTips(Context context, String title, String des, String btn, DialogInterface.OnDismissListener dismissListener) {
        AlertDialog.Builder builder = dialogBuilder(context, title, des);
        builder.setCancelable(true);
        builder.setPositiveButton(btn, null);
        Dialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(dismissListener);
        return dialog;
    }


    protected static AlertDialog showStandardDialog(String title, String msg,
                                                    DialogInterface.OnClickListener onClickListener,
                                                    boolean isPromptDialog) {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (isPromptDialog) {
            mDialog = new AlertDialog.Builder(AppManager.getApp()).setPositiveButton(
                    R.string.sure, onClickListener).create();
        } else {
            mDialog = new AlertDialog.Builder(AppManager.getApp())
                    .setPositiveButton(R.string.sure, onClickListener)
                    .setNegativeButton(R.string.cancel, null).create();
        }

        if (title != null) {
            mDialog.setTitle(title);
        } else {
            mDialog.setTitle(AppManager.getApp().getString(R.string.prompt));
        }
        if (msg != null) {
            mDialog.setMessage(msg);
        }
        mDialog.show();

        return mDialog;
    }

    public AlertDialog showStandardDialog(String msg,
                                          DialogInterface.OnClickListener onClickListener) {
        String title = AppManager.getApp().getString(R.string.prompt);
        return showStandardDialog(title, msg, onClickListener, false);
    }

    protected AlertDialog showPromptDialog(Context context, String msg) {
        String title = context.getString(R.string.prompt);
        return showStandardDialog(title, msg, null, true);
    }

    protected void dismissAlertDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }


}


