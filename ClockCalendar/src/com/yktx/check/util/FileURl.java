package com.yktx.check.util;

import java.io.File;

import android.os.Environment;

/**
 * Created by Administrator on 2014/4/14.
 */
public class FileURl {
    /*商品图片*/
    public  static final String GoodsIamgeURL=Environment.getExternalStorageDirectory()+"/clockimage/";
    public  static  final  String LOAD_FILE="file://"; // from SD
    public  static  final  String LOAD_HTTP="http://";// from Web
    public  static  final  String LOAD_CONTENT="content://";// from content provider
    public  static  final  String LOAD_ASSETS= "assets://image.png"; // from assets
    public  static  final  String LOAD_DRAWABLES= "drawable://";  // from drawables (only images, non-9patch)
    public  static  final  String IMAGE_NAME = "clock.jpg";  // from drawables (only images, non-9patch)
    public static File ImageFilePath = new File((Environment.getExternalStorageDirectory()).getPath()+"/clock/");
}
