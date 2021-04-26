package com.fan.componentization.module_comm;

import com.fan.componentization.apt_annotation.ARouter;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/4/22 17:41
 * @Modify:
 */
@ARouter(path = "/111/222")
public class MyCommon {

    public String getMyCommon() {
        return "myCommon";
    }

    public Class findClass(String path) {
        return null;
    }

}
