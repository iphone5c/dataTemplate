package data.core;

import java.io.Serializable;
import java.util.List;

/**
 * 表结构信息
 * Created by 魏源 on 2016/6/25.
 */
public class Table implements Serializable {

    private static final long serialVersionUID = -4452835156495863075L;

    /**
     * 表名
     */
    private String name;

    /**
     * 记录数
     */
    private Integer num;

    /**
     * 模式（暂时没用）
     */
    private String schema;

    /**
     * 目录（暂时没用）
     */
    private String catalog;

    /**
     * 文本文件路径
     */
    private String textFile;

    /**
     * 产生数据比例
     */
    private String proportion;

    /**
     * 表字段集合
     */
    private List<Column> columns;

    /**
     * 子表集合
     */
    private List<Table> childTalbes;

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", num=" + num +
                ", schema='" + schema + '\'' +
                ", catalog='" + catalog + '\'' +
                ", textFile='" + textFile + '\'' +
                ", proportion='" + proportion + '\'' +
                ", columns=" + columns +
                ", childTalbes=" + childTalbes +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getTextFile() {
        return textFile;
    }

    public void setTextFile(String textFile) {
        this.textFile = textFile;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Table> getChildTalbes() {
        return childTalbes;
    }

    public void setChildTalbes(List<Table> childTalbes) {
        this.childTalbes = childTalbes;
    }
}
