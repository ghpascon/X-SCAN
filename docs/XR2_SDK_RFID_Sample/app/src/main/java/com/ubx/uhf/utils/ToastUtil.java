package com.ubx.uhf.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Context mContext ;
    /**
     * Toast instance，Used to process all Toast messages that appear on this page
     */
    private static Toast myToast;

    private ToastUtil() {
        throw new RuntimeException("ToastUtils cannot be initialized!");
    }

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * Encapsulated Toast method，You can cancel what was not completed last time and proceed directly to the next Toast.
     * @param text Contents that require Toast
     */
    public static void toast( String text){
        if (myToast != null) {
            myToast.cancel();
            myToast=Toast.makeText(mContext,text,Toast.LENGTH_SHORT);
        }else{
            myToast=Toast.makeText(mContext,text,Toast.LENGTH_SHORT);
        }
        myToast.show();
    }

}
