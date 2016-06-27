package data.core;

import data.utils.ColumnType;

import java.io.Serializable;

/**
 * 字段结构信息
 * Created by 魏源 on 2016/6/25.
 */
public class Column implements Serializable {

    private static final long serialVersionUID = 3716303030261597733L;

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段类型
     */
    private ColumnType columnType;

    /**
     * 生成方式
     */
    private String generator;

    /**
     * 当type为SRING时，generator为LABLE时，
     * 当type为ORGAN时，corp,dept,user，分别代表单位，部门，人员
     * 当type为FK时，表示外键对应的表字段
     */
    private String content;

    /**
     * 最小值
     */
    private String min;

    /**
     * 最大值
     */
    private String max;

    /**
     * 所属表
     */
    private Table table;

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", columnType=" + columnType +
                ", generator='" + generator + '\'' +
                ", content='" + content + '\'' +
                ", min='" + min + '\'' +
                ", max='" + max + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
