package com.lib.liblibgo.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHelper {
    public static final String FILE_NAME_FORMAT = "dd_MM_yyyy_HH_mm_ss";

    public static String getUniqueFileName() {
        String fileName;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FileHelper.FILE_NAME_FORMAT);
        fileName = simpleDateFormat.format(new Date());
        return "image_" + fileName + ".png";
    }
}
