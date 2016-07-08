package data.core;

import data.utils.DataUtils;
import data.utils.Params;
import javafx.scene.control.Tab;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 魏源 on 2016/6/25.
 */
public class XmlDatasFactory {

    /**
     * 解析XML模板获取所有表结构
     * @param filePath
     * @return
     * @throws Exception
     */
    public static List<Table> getAllTableList(String filePath) throws Exception {
        List<Table> tableList=new ArrayList<Table>();
        if (DataUtils.isEmptyOrNull(filePath))
            throw new IllegalArgumentException("locationPattern参数不能为null或empty");
        File file=new File(filePath);
        //获取DocumentBuilderFactory对象
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        //通过DocumentBuilderFactory对象获取DocumentBuilder对象
        DocumentBuilder builder=factory.newDocumentBuilder();
        //通过DocumentBuilder对象获取Document对象
        Document document=builder.parse(file);

        //获取根节点
        Element tablesEL= document.getDocumentElement();
        //获取当前节点的所有子节点
        NodeList tableNodeList=tablesEL.getChildNodes();
        for (int i=0;i<tableNodeList.getLength();i++){
            Node tableNode=tableNodeList.item(i);
            Table table=XmlDatasFactory.getTable(tableNode,null);
            if (table==null)
                continue;
            tableList.add(table);
        }
        return tableList;
    }

    /**
     * 获取表结构
     * @param tableNode
     * @param parentTalbe
     * @return
     */
    private static Table getTable(Node tableNode,Table parentTalbe){
        Table table=null;
        if (tableNode instanceof Element){
            table=new Table();
            Element tableEL= (Element) tableNode;
            if (!tableEL.getTagName().equals("table"))
                throw new IllegalArgumentException("当前节点不是table节点");
            table.setName(tableEL.getAttribute("name"));
            table.setNum(DataUtils.isEmptyOrNull(tableEL.getAttribute("num")) ? null : Integer.parseInt(tableEL.getAttribute("num")));
            table.setSchema(tableEL.getAttribute("schema"));
            table.setCatalog(tableEL.getAttribute("catalog"));
            table.setTextFile(tableEL.getAttribute("textfile"));
            table.setProportion(tableEL.getAttribute("proportion"));
            table.setParent(parentTalbe);
            table.setChildTalbes(new ArrayList<Table>());
            table.setColumns(XmlDatasFactory.getColumnList(tableEL.getChildNodes(), table));
        }
//        cacheTableStruct(table);
        return table;
    }

    private static void cacheTableStruct(Table table){
        StringBuffer sb = new StringBuffer();
        sb.append(table.getName()).append("(");
        for(Column column : table.getColumns()){
            sb.append(column.getName()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");
        Map<String,Object> map = new HashMap<>();
        map.put(table.getName(),sb.toString());
        Params.tableList.add(map);
    }

    /**
     * 获取表结构所有字段
     * @param columnNodeList
     * @param parentTalbe
     * @return
     */
    private static List<Column> getColumnList(NodeList columnNodeList,Table parentTalbe){
        List<Column> columnList=new ArrayList<Column>();
        for (int j=0;j<columnNodeList.getLength();j++){
            Node columnNode=columnNodeList.item(j);
            if(columnNode instanceof  Element){
                Column column=new Column();
                Element columnEL= (Element) columnNode;
                if (columnEL.getTagName().equals("column")){
                    column.setName(columnEL.getAttribute("name"));
                    column.setColumnType(DataUtils.isEmptyOrNull(columnEL.getAttribute("type")) ? null : DataUtils.getColumnTypeByKey(columnEL.getAttribute("type")));
                    column.setGenerator(columnEL.getAttribute("generator"));
                    column.setContent(columnEL.getAttribute("content"));
                    column.setMin(columnEL.getAttribute("min"));
                    column.setMax(columnEL.getAttribute("max"));
                    column.setTable(parentTalbe);
                    columnList.add(column);
                }else if (columnEL.getTagName().equals("table")){
                    parentTalbe.getChildTalbes().add(XmlDatasFactory.getTable(columnNode,parentTalbe));
                }else
                    throw new IllegalArgumentException("当前节点不是table或者colunm节点");
            }
        }
        return columnList;
    }

    /**
     * 根据表名获取表
     * @param tableName
     * @param rootTalbe
     * @return
     */
    public static Table getTableByTableName(String tableName,Table rootTalbe){
        Table result=null;
        if (tableName.equals(rootTalbe.getName()))
            result=rootTalbe;
        else {
            List<Table> childTables=rootTalbe.getChildTalbes();
            if (childTables==null||childTables.size()<=0)
                return null;
            for (Table table:childTables){
                result=XmlDatasFactory.getTableByTableName(tableName,table);
                if (result!=null)
                    break;
            }
        }
        return result;
    }

    /**
     *获取顶层表
     * @param column
     */
    public static Table getRootTableByColumn(Column column){
        if (column==null)
            throw new IllegalArgumentException("column不能为空或null");
        Table table=column.getTable();
        if (table==null)
            throw new IllegalArgumentException("此字段没有找到对应的表信息");
        return XmlDatasFactory.getRootTableByTable(table);

    }

    /**
     * 获取顶层表
     * @param table
     * @return
     */
    public static Table getRootTableByTable(Table table){
        if (table==null)
            throw new IllegalArgumentException("table不能为空或null");
        if (table.getParent()==null)
            return table;
        else
            return XmlDatasFactory.getRootTableByTable(table.getParent());
    }

    /**
     * 根据名字获取指定表下的字段
     * @param name
     * @param table
     * @return
     */
    public static Column getColumnByName(String name,Table table){
        Column column=null;
        if(DataUtils.isEmptyOrNull(name))
            throw new IllegalArgumentException("name不能为空或null");
        if (table==null)
            throw new IllegalArgumentException("table不能为空或null");
        if(table.getColumns()==null)
            throw new IllegalArgumentException("此表的字段集合不能为空或null");
        for (Column col:table.getColumns()){
            if (col.getName().equals(name)){
                column=col;
                break;
            }
        }
        return column;
    }
}
