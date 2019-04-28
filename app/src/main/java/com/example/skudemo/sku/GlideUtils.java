package com.example.skudemo.sku;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.skudemo.R;

import static com.bumptech.glide.util.Util.isOnMainThread;

/**
 * Anthor: Zhuangmingzhu
 * Date: 2019/4/26 下午8:13
 * Describe:
 */
public class GlideUtils {
    public static void showasPhoto(Context context, String url, ImageView imgv){
        if(isOnMainThread()&&!getIsDestory(context)){
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.xiezi)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.xiezi)
                    .into(imgv);
        }
    }

    //判断Activity是否finish
    private static boolean getIsDestory(Context context) {
        while (context instanceof ContextWrapper){
            if(context instanceof Activity){
                return ((Activity) context).isFinishing();
            }
            context=((ContextWrapper) context).getBaseContext();
        }
        return true;
    }

}
