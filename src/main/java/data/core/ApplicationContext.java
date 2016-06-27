package data.core;

import data.utils.Params;

/**
 * 应用上下文
 * Created by wy on 2016/6/27.
 */
public class ApplicationContext {

    public ApplicationContext(){
        //初始化数据库连接参数
        Params.initParam();
    }

}
