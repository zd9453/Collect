package com.zd.collectlibrary.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;

import java.util.Arrays;

/**
 * Package: com.zd.collectlibrary.utils
 * <p>
 * describe:
 *
 * @author zhangdong on 2020/10/9
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class StatesBarUtil {

    private static final String TAG = ">>>>>";

    public static void changeStateBar(Activity activity, @ColorInt int color) {
        if (null == activity || activity.isFinishing())
            return;
        Window window = activity.getWindow();
        //5.0以上才有效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //清除半透明效果
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //全屏
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏背景色
            window.setStatusBarColor(color);
        } else {
            //顶部半透明状态 5.0以下
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void changeNavigationBar(Activity activity) {
        if (null == activity || activity.isFinishing())
            return;
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                    | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//            window.setStatusBarColor(Color.BLUE);
        }

    }

    public static void getPhoneInformation() {
        String device = Build.DEVICE;
        Log.e(TAG, "getPhoneInformation: ----device: " + device);
        String display = Build.DISPLAY;//型号+版本号
        Log.e(TAG, "getPhoneInformation: ----display: " + display);
        String product = Build.PRODUCT;//型号
        Log.e(TAG, "getPhoneInformation: ----product: " + product);
        String model = Build.MODEL;
        Log.e(TAG, "getPhoneInformation: ----model: " + model);
        String brand = Build.BRAND;
        Log.e(TAG, "getPhoneInformation: ----brand: " + brand);
        String board = Build.BOARD;
        Log.e(TAG, "getPhoneInformation: ----board: " + board);
        String id = Build.ID;
        Log.e(TAG, "getPhoneInformation: ----id: " + id);
        String radioVersion = Build.getRadioVersion();
        Log.e(TAG, "getPhoneInformation: ----radioVersion: " + radioVersion);
        String[] supported32BitAbis = Build.SUPPORTED_32_BIT_ABIS;
        Log.e(TAG, "getPhoneInformation: ----supported32BitAbis" + Arrays.toString(supported32BitAbis));
        String[] supported64BitAbis = Build.SUPPORTED_64_BIT_ABIS;
        Log.e(TAG, "getPhoneInformation: ----supported64BitAbis" + Arrays.toString(supported64BitAbis));
        String[] supportedAbis = Build.SUPPORTED_ABIS;
        Log.e(TAG, "getPhoneInformation: ----supportedAbis: " + Arrays.toString(supportedAbis));
        String bootloader = Build.BOOTLOADER;
        Log.e(TAG, "getPhoneInformation: ----bootloader: " + bootloader);
        String fingerprint = Build.FINGERPRINT;
        Log.e(TAG, "getPhoneInformation: ----fingerprint: " + fingerprint);
        String hardware = Build.HARDWARE;
        Log.e(TAG, "getPhoneInformation: ----hardware: " + hardware);
        String host = Build.HOST;
        Log.e(TAG, "getPhoneInformation: ----host: " + host);
        String manufacturer = Build.MANUFACTURER;
        Log.e(TAG, "getPhoneInformation: ----manufacturer: " + manufacturer);
        String tags = Build.TAGS;
        Log.e(TAG, "getPhoneInformation: ----tags: " + tags);
        long time = Build.TIME;
        Log.e(TAG, "getPhoneInformation: ----time: " + time);
        String type = Build.TYPE;
        Log.e(TAG, "getPhoneInformation: ----type: " + type);
        String user = Build.USER;
        Log.e(TAG, "getPhoneInformation: ----user: " + user);
        String partitionNameSystem = Build.Partition.PARTITION_NAME_SYSTEM;
        Log.e(TAG, "getPhoneInformation: ----partitionNameSystem: " + partitionNameSystem);

        int sdkInt = Build.VERSION.SDK_INT;
        Log.e(TAG, "getPhoneInformation: ----sdkInt: " + sdkInt);
        String baseOs = Build.VERSION.BASE_OS;
        Log.e(TAG, "getPhoneInformation: ----baseOs: " + baseOs);
        String codename = Build.VERSION.CODENAME;
        Log.e(TAG, "getPhoneInformation: ----codename: " + codename);
        String incremental = Build.VERSION.INCREMENTAL;
        Log.e(TAG, "getPhoneInformation: ----incremental: " + incremental);
        int previewSdkInt = Build.VERSION.PREVIEW_SDK_INT;
        Log.e(TAG, "getPhoneInformation: ----previewSdkInt: " + previewSdkInt);
        String release = Build.VERSION.RELEASE;
        Log.e(TAG, "getPhoneInformation: ----release: " + release);
        String securityPatch = Build.VERSION.SECURITY_PATCH;
        Log.e(TAG, "getPhoneInformation: ----securityPatch: " + securityPatch);

        /**
         * E/>>>>>: getPhoneInformation: ----device: HWCOR
         * E/>>>>>: getPhoneInformation: ----display: COR-AL00 9.1.0.346(C00E336R1P1)
         * E/>>>>>: getPhoneInformation: ----product: COR-AL00
         * E/>>>>>: getPhoneInformation: ----model: COR-AL00
         * E/>>>>>: getPhoneInformation: ----brand: HONOR
         * E/>>>>>: getPhoneInformation: ----board: COR
         * E/>>>>>: getPhoneInformation: ----id: HUAWEICOR-AL00
         * E/>>>>>: getPhoneInformation: ----radioVersion: 21C20B369S009C000,21C20B369S009C000
         * E/>>>>>: getPhoneInformation: ----supported32BitAbis[armeabi-v7a, armeabi]
         * E/>>>>>: getPhoneInformation: ----supported64BitAbis[arm64-v8a]
         * E/>>>>>: getPhoneInformation: ----supportedAbis: [arm64-v8a, armeabi-v7a, armeabi]
         * E/>>>>>: getPhoneInformation: ----bootloader: unknown
         * E/>>>>>: getPhoneInformation: ----fingerprint: HONOR/COR-AL00/HWCOR:9/HUAWEICOR-AL00/9.1.0.346C00:user/release-keys
         * E/>>>>>: getPhoneInformation: ----hardware: kirin970
         * E/>>>>>: getPhoneInformation: ----host: cn-west-3b-2b7a7cf141589623073275-647b948566-jh946
         * E/>>>>>: getPhoneInformation: ----manufacturer: HUAWEI
         * E/>>>>>: getPhoneInformation: ----tags: release-keys
         * E/>>>>>: getPhoneInformation: ----time: 1589627039000
         * E/>>>>>: getPhoneInformation: ----type: user
         * E/>>>>>: getPhoneInformation: ----user: test
         * E/>>>>>: getPhoneInformation: ----partitionNameSystem: system
         * E/>>>>>: getPhoneInformation: ----sdkInt: 28
         * E/>>>>>: getPhoneInformation: ----baseOs:
         * E/>>>>>: getPhoneInformation: ----codename: REL
         * E/>>>>>: getPhoneInformation: ----incremental: 9.1.0.346C00
         * E/>>>>>: getPhoneInformation: ----previewSdkInt: 0
         * E/>>>>>: getPhoneInformation: ----release: 9
         * E/>>>>>: getPhoneInformation: ----securityPatch: 2020-05-01
         */
    }
}
