package com.fan.componentization.module_comm;

import android.content.Context;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/4/25 13:47
 * @Modify:
 */
public abstract class BaseAppLike {

    public static final int MAX_PRIORITY = 10;
    public static final int MIN_PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;

    /**
     * 返回组件的优先级，优先级范围为[1-10]，10为最高，1为最低，默认优先级是5
     *
     * @return
     */
    public int getPriority() {
        return NORM_PRIORITY;
    }

    /**
     * 应用初始化
     *
     * @param context
     */
    public abstract void onCreate(Context context);

    public abstract void onTerminate();
}
