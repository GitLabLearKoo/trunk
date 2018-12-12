package com.yuan.base.tools.router.jump;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Activity跳转辅助类
 *
 * @author Created by YuanYe on 2018/4/13.
 */
public class JumpActivity {

    /**
     * 提供的默认请求码
     */
    public final static int PERMISSIONREQUESTCODE = 10021;


    public static void open(Context mContext, Class clazz) {
        open(mContext, clazz, null, false);
    }

    public static void open(Context mContext, Class clazz, JumpParam param) {
        open(mContext, clazz, param, false);
    }

    public static void open(Context mContext, Class clazz, boolean finishSelf) {
        open(mContext, clazz, null, finishSelf);
    }

    public static void open(Context mContext, Class clazz, JumpParam param, boolean finishSelf) {
        mContext.startActivity(getIntent(mContext, clazz, param));
        if (mContext instanceof Activity && finishSelf) {
            Activity activity = (Activity) mContext;
            activity.finish();
        }
    }

    public static void openResult(Context mContext, Class clazz, int requestCode, OnResultListener listener) {
        openResult(mContext, clazz, null, requestCode, listener);
    }

    public static void openResult(Context mContext, Class clazz, OnResultListener listener) {
        openResult(mContext, clazz, PERMISSIONREQUESTCODE, listener);
    }

    public static void openResult(Context mContext, Class clazz, JumpParam param, int requestCode, OnResultListener listener) {
        ResultFragmentManager jumpResult = new ResultFragmentManager(mContext);
        jumpResult.getFragment().startForResult(getIntent(mContext, clazz, param), requestCode, listener);
    }

    public static void openResult(Context mContext, Intent intent, OnResultListener listener) {
        openResult(mContext, intent, PERMISSIONREQUESTCODE, listener);
    }

    public static void openResult(Context context, Intent intent, int requestCode, OnResultListener listener) {
        ResultFragmentManager jumpResult = new ResultFragmentManager(context);
        jumpResult.getFragment().startForResult(intent, requestCode, listener);
    }


    /**
     * 构造Intent
     */
    private static Intent getIntent(Context mContext, Class clazz, JumpParam param) {
        Intent intent = new Intent(mContext, clazz);
        if (param != null) {
            //向Intent中添加参数
            HashMap<String, Object> map = param.getAttr();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                Object val = entry.getValue();
                if (val instanceof String) {
                    intent.putExtra(key, (String) val);
                } else if (val instanceof Boolean) {
                    intent.putExtra(key, (boolean) val);
                } else if (val instanceof Integer) {
                    intent.putExtra(key, (int) val);
                } else if (val instanceof Long) {
                    intent.putExtra(key, (long) val);
                } else if (val instanceof Float) {
                    intent.putExtra(key, (float) val);
                } else if (val instanceof Double) {
                    intent.putExtra(key, (double) val);
                } else if (val instanceof Serializable) {
                    intent.putExtra(key, (Serializable) val);
                } else if (val instanceof Bundle) {
                    intent.putExtra(key, (Bundle) val);
                } else if (val instanceof Parcelable) {
                    intent.putExtra(key, (Parcelable) val);
                }
            }
            if (param.getStringList() != null) {
                intent.putStringArrayListExtra(param.getStringListKey(), param.getStringList());
            }
            if (param.getParcelableList() != null) {
                intent.putParcelableArrayListExtra(param.getParcelableKey(), param.getParcelableList());
            }
        }
        return intent;
    }
}