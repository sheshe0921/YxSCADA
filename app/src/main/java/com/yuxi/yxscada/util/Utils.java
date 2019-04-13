package com.yuxi.yxscada.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    /** 是否有sd卡 */
    public static boolean haveSDcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**获取手机cpu名*/
    public static String getCpuName(Context context) {
        String s = null;
        FileReader filereader = null;
        BufferedReader bufferedreader = null;
        try {
            copyCpuinfo(context);
            filereader = new FileReader("/proc/cpuinfo");
            bufferedreader = new BufferedReader(filereader);
            String s1 = null;
            String s2 = "";
            while ((s1 = bufferedreader.readLine()) != null)
                s2 = s2 + s1 + "\n";

            if (s2.indexOf("Hardware\t:") >= 0) {
                int i = s2.indexOf("Hardware\t:") + "Hardware\t:".length();
                int j = s2.indexOf("\n", i);// 从i索引开始搜索到\n
                if (j >= 0) {
                    s = s2.substring(i, j).trim();
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (filereader != null)
                    filereader.close();
                if (bufferedreader != null)
                    bufferedreader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return s;
    }

    /**将assets中的cpuinfo文件考到sd卡中*/
    private static void copyCpuinfo(Context context) {
        // TODO Auto-generated method stub
        if(!haveSDcard())
            return;
        String str = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/yuxi";
        try {
            File file = new File(str);
            if (!file.exists())
                file.mkdir();
            File file1 = new File(file, "cpuinfo");
            if (!file1.exists()) {
                InputStream source = context.getAssets().open("cpuinfo");
                OutputStream destination = new FileOutputStream(file1);
                byte[] buffer = new byte[1024];
                int n;

                while ((n = source.read(buffer)) != -1) {
                    if (n == 0) {
                        n = source.read();
                        if (n < 0)
                            break;
                        destination.write(n);
                        continue;
                    }
                    destination.write(buffer, 0, n);
                }
                source.close();
                destination.close();

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String intToIp(int i) {
        return (i & 0xFF) + "." +

                ((i >> 8) & 0xFF) + "." +

                ((i >> 16) & 0xFF) + "." +

                (i >> 24 & 0xFF);

    }

    public static int ipToInt(final String s) {
        final String[] split = s.split("\\.");
        if (split.length != 4) {
            return 0;
        }
        return 0 + Integer.parseInt(split[0])
                + (Integer.parseInt(split[1]) << 8)
                + (Integer.parseInt(split[2]) << 16)
                + (Integer.parseInt(split[3]) << 24);
    }

    /** 获取纬度信息lat */
    @SuppressLint("MissingPermission")
    public static double getLat(Context context) {
        double lat = 0.0;

        try {
            String serviceName = "location";
            Location location = getLocation(context);
            @SuppressLint("WrongConstant")
            LocationManager locationManager = (LocationManager) context
                    .getSystemService(serviceName);

            // 取得手机用户所在经纬度
            if (location != null) {
                // 纬度
                lat = location.getLatitude();
            } else {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    // 纬度
                    lat = location.getLatitude();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return lat;
    }

    /** 获取经度信息lng */
    @SuppressLint("MissingPermission")
    public static double getLng(Context context) {
        double lng = 0.0;

        try {
            String serviceName = "location";
            Location location = getLocation(context);
            @SuppressLint("WrongConstant")
            LocationManager locationManager = (LocationManager) context
                    .getSystemService(serviceName);

            // 取得手机用户所在经纬度
            if (location != null) {
                // 经度
                lng = location.getLongitude();
            } else {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    // 经度
                    lng = location.getLongitude();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return lng;
    }

    /** 获取当前位置 */
    @SuppressLint("MissingPermission")
    private static Location getLocation(Context context) {

        Location mlocation = null;

        try {
            // GPS纬度，经度
            String serviceName = "location";
            // A LocationManager for controlling location (e.g., GPS) updates.
            @SuppressLint("WrongConstant")
            LocationManager locationManager = (LocationManager) context
                    .getSystemService(serviceName);
            // new一个标准
            Criteria criteria = new Criteria();
            // 设置为最大精度 Criteria.ACCURACY_FINE=1
            criteria.setAccuracy(1);
            // 不要求海拔信息
            criteria.setAltitudeRequired(false);
            // 不要求方位信息
            criteria.setBearingRequired(false);
            // 是否允许付费
            criteria.setCostAllowed(true);
            // 对电量的要求 Criteria.POWER_LOW=1
            criteria.setPowerRequirement(1);

            String provider = locationManager.getBestProvider(criteria, true);

            if ((provider != null) && (provider.length() > 0)) {

                mlocation = locationManager.getLastKnownLocation(provider);

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return mlocation;
    }


}
