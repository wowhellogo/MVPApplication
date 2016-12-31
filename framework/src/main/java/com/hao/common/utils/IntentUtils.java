package com.hao.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

/**
 * 跳转工具类
 *
 * @author yebin
 * @date 2014-10-22
 */
public class IntentUtils {

    public static void showNetworkSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        context.startActivity(intent);
    }

    public static void showSmsActivity(Activity activity, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                + phoneNumber));
        activity.startActivity(intent);
    }

    public static void showPhoneActivity(Activity activity, String phoneNumber) {
        // ACTION_DIAL:调用拨号程序,手工拨出
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + phoneNumber));
        activity.startActivity(intent);
    }

    public static void showUrlActivity(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

    public static void showPhotoActivity(Activity activity,int requestCode,String title){
        // 打开手机中的相册
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, title);
        activity.startActivityForResult(wrapperIntent, requestCode);
    }

}
