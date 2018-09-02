package com.tone.library;

import android.os.Environment;
import java.io.*;
/**
 * Created by zhaotong on 2016/9/28.
 */
public class FileUtil {
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String DIR = SD_PATH + File.separator + "QRY/Video";

    public static File getFile(String url) {
        try {
            File file = new File(DIR);
            if (!file.exists())
                file.mkdirs();
            File file1 = new File(DIR + File.separator + getFileName(url));
            file1.createNewFile();
            return file1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileName(String url) {
String[] str = url.split("/");
int isccr=str[str.length-1].indexOf("?");
String[] str2;
String str3;
if(isccr!=-1){
	str2 =str[str.length-1].split("?");
	str3=str2[0];
}else{
	str3=str[str.length-1];
}
        return str3;
    }
}
