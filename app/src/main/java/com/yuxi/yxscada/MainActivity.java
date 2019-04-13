package com.yuxi.yxscada;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuxi.yxscada.object.MobileParams;
import com.yuxi.yxscada.util.Utils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private final String TAG="yuxi";
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        getMobile();

    }
    private void getMobile() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.WAKE_LOCK,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {

                    MobileParams params = new MobileParams();
                    //获取imei号
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    WifiManager wifi_service = (WifiManager) ctx
                            .getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifi_service.getConnectionInfo();


                    @SuppressLint("MissingPermission")
                    String DeviceId= tm.getDeviceId();
                    params.setDeviceId(DeviceId);

                    String AndroidId = getAndroidId1(MainActivity.this);
                    params.setAndroidId(AndroidId);

                    @SuppressLint("MissingPermission")
                    String Line1Number = tm.getLine1Number();
                    params.setLine1Number(Line1Number);

                    @SuppressLint("MissingPermission")
                    String SimSerialNumber = tm.getSimSerialNumber();
                    params.setSimSerialNumber(SimSerialNumber);

                    @SuppressLint("MissingPermission")
                    String SubscriberId = tm.getSubscriberId();
                    params.setSubscriberId(SubscriberId);

                    String SimOperator = tm.getSimOperator();
                    params.setSimOperator(SimOperator);

                    String NetworkOperatorName = tm.getNetworkOperatorName();
                    params.setNetworkOperatorName(NetworkOperatorName);

                    String NetworkType = tm.getNetworkType()+"";
                    params.setNetworkType(NetworkType);

                    String MacAddress = wifiInfo.getMacAddress();
                    params.setMacAddress(MacAddress);

                    String SSID = wifiInfo.getSSID();
                    params.setSSID(SSID);

                    String BSSID = wifiInfo.getBSSID();
                    params.setBSSID(BSSID);

                    String SERIAL = android.os.Build.SERIAL;
                    params.setSERIAL(SERIAL);

                    String RELEASE = android.os.Build.VERSION.RELEASE;
                    params.setRELEASE(RELEASE);

                    String SDK = android.os.Build.VERSION.SDK;
                    params.setSDK(SDK);

                    String BRAND = android.os.Build.BRAND;
                    params.setBRAND(BRAND);

                    String DEVICE = Build.DEVICE;
                    params.setDEVICE(DEVICE);

                    String BOARD = Build.BOARD;
                    params.setBOARD(BOARD);

                    String FINGERPRINT = Build.FINGERPRINT;
                    params.setFINGERPRINT(FINGERPRINT);

                    String HARDWARE = Build.HARDWARE;
                    params.setHARDWARE(HARDWARE);

                    String MODEL = Build.MODEL;
                    params.setMODEL(MODEL);

                    String PRODUCT = Build.PRODUCT;
                    params.setPRODUCT(PRODUCT);

                    String MANUFACTURER = Build.MANUFACTURER;
                    params.setMANUFACTURER(MANUFACTURER);

                    String RadioVersion = Build.getRadioVersion();
                    params.setRadioVersion(RadioVersion);

                    String cpu = Utils.getCpuName(ctx);
                    params.setCpu(cpu);

                    String IpAddress = Utils.intToIp(wifiInfo.getIpAddress());
                    params.setIpAddress(IpAddress);

                    String SimState = tm.getSimState()+"";
                    params.setSimState(SimState);

                    WebView webview = new WebView(ctx);
                    webview.layout(0, 0, 0, 0);
                    WebSettings settings = webview.getSettings();
                    String UserAgent = settings.getUserAgentString();

                    params.setUserAgent(UserAgent);

                    String lat = Utils.getLat(ctx)+"";
                    String lng = Utils.getLng(ctx)+"";
                    params.setLat(lat);
                    params.setLng(lng);


                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                   String widthPixels = displaymetrics.widthPixels+"";
                   params.setWidthPixels(widthPixels);
                   String heightPixels = displaymetrics.heightPixels+"";
                   params.setHeightPixels(heightPixels);

                   params.save(new SaveListener<String>() {
                       @Override
                       public void done(String s, BmobException e) {

                           Log.e(TAG,"保存数据出错了："+e);

                       }
                   });


                } else {

                }
            }
        });
    }

    public static String getAndroidId1 (Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        return ANDROID_ID;
    }
    public static String getAndroidId2 (Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return ANDROID_ID;
    }
    
}
