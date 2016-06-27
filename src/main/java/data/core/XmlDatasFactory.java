package data.core;

import data.utils.DataUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            Table table=XmlDatasFactory.getTable(tableNode);
            if (table==null)
                continue;
            tableList.add(table);
        }
        return tableList;
    }

    /**
     * 获取表结构
     * @param tableNode
     * @return
     */
    private static Table getTable(Node tableNode){
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
            table.setChildTalbes(new ArrayList<Table>());
            table.setColumns(XmlDatasFactory.getColumnList(tableEL.getChildNodes(), table));
        }
        return table;
    }

    /**
     * 获取表结构所有字段
     * @param columnNodeList
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
                }else if (columnEL.getTagName().equals("table")){
                    parentTalbe.getChildTalbes().add(XmlDatasFactory.getTable(columnNode));
                }else
                    throw new IllegalArgumentException("当前节点不是table或者colunm节点");
                columnList.add(column);
            }
        }
        return columnList;
    }
}
