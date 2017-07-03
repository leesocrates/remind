package com.lee.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.lee.library.view.MyToast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lee on 2015/11/12.
 */
public class EMUtil {

    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConnected = (NetworkInfo.State.CONNECTED == connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState());
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
            boolean is3GConnected = (NetworkInfo.State.CONNECTED == connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState());
            return isWifiConnected || is3GConnected;
        }
        return isWifiConnected;
    }

    public static String getTime(String isoTime) {
        if (TextUtils.isEmpty(isoTime)) {
            return "";
        }
        String result = null;
        try {
            Date date = format.parse(isoTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            result = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getIsoTime(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        Date date = new Date(calendar.getTimeInMillis());
        return format.format(date);
    }

    public static void showMessage(Context context, String message) {
        MyToast myToast = new MyToast(context);
        myToast.setMessage(message);
        myToast.show();
    }

    public static boolean isUserNameValid(String username) {
        return username != null && username.length() >= 3 && username.length() <= 20;
    }

    /**
     * 验证密码是否有效，必须是数字和字母的组合，6-12位。 改方法只在重置密码和修改密码的地方调用，登录的地方只验证6-12位就行
     *
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        return password != null && password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$");
    }

    public static Bitmap getCompressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = 2;//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    /**
     * 获取今天24:00时的时间毫秒数
     *
     * @return
     */
    public static long getMillisTimeAtCurDay24() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static void showMessageByToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static byte[] bitmapToByteArray(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    public static byte[] fileToByteArray(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos == null ? null : baos.toByteArray();
    }

    public static boolean deleteFile(Context context, Uri fileUri, String filepath) {
        boolean wasDeleted = false;
        try {
            if (fileUri != null) {
                //删除真正的文件（图片，vedio)
                File file = new File(filepath);
                wasDeleted = file.delete();
                //删除相册中的缩略图
                context.getContentResolver().delete(fileUri, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wasDeleted;
    }

    public static boolean deleteFile(Context context, String filename) {
        boolean wasDeleted = false;
        try {
            File file = new File(context.getFilesDir(), filename);
            wasDeleted = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wasDeleted;
    }

    public static boolean deleteFile(String filepath) {
        boolean wasDeleted = false;
        try {
            File file = new File(filepath);
            wasDeleted = file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wasDeleted;
    }


    public static boolean saveFile(Context context, String sourcefilePath, String targetFileName) {
        File sourceFile = new File(sourcefilePath);
        File targetFile = new File(context.getFilesDir(), targetFileName);
        try {
            copyFile(sourceFile, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // 复制文件
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        //关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }
}
