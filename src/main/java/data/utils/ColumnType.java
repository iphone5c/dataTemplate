package data.utils;

/**
 * 字段类型
 * Created by 魏源 on 2016/6/25.
 */
public enum ColumnType {
    /**
     * 主键
     */
    PK,

    /**
     *外键
     */
    FK,

    /**
     * 单值代码
     */
    CODE,

    /**
     * 时间
     */
    DATE,

    /**
     * 数字
     */
    NUM,

    /**
     * 字符串
     */
    STRING,

    /**
     * 组织机构(有级联性质)
     */
    ORGAN,

    /**
     * 从文本中随机读取长度的字符串
     */
    TEXT,

}
