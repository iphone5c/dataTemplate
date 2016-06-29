package data.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/3/1.
 */
public class DataUtils {
//    public static String SETTING_PATH="yc-coresystem-services/conf/setting/settings-config.xml";
//
//    public static String MODEL_TARGET_PATH="_yc_resources";

    /**
     * 日期格式：yyyy-MM-dd
     */
    public static String DATEFORMAT_DATA_EN_LONG = "yyyy-MM-dd";

    public static boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }

    public static ColumnType getColumnTypeByKey(String key){
        if (DataUtils.isEmptyOrNull(key))
            throw new IllegalArgumentException("key参数不能为null或empty");
        if (key.equals(ColumnType.PK.name())){
            return ColumnType.PK;
        }else if (key.equals(ColumnType.FK.name())){
            return ColumnType.FK;
        }else if (key.equals(ColumnType.CODE.name())){
            return ColumnType.CODE;
        }else if (key.equals(ColumnType.DATE.name())){
            return ColumnType.DATE;
        }else if (key.equals(ColumnType.NUM.name())){
            return ColumnType.NUM;
        }else if (key.equals(ColumnType.STRING.name())){
            return ColumnType.STRING;
        }else if (key.equals(ColumnType.ORGAN.name())){
            return ColumnType.ORGAN;
        }else if (key.equals(ColumnType.TEXT.name())){
            return ColumnType.TEXT;
        }else
            throw new IllegalArgumentException("没有此"+key+"字段类型");
    }

    /**
     * 生成一个指定范围的随机数
     * @param min
     * @param max
     * @return
     */
    public static int getRanDom(int min,int max){
        Random random=new Random();
        return random.nextInt(max-min+1)+min;
    }

    /**
     * 生成一个指定范围的随机时间
     * @param min
     * @param max
     * @return
     */
    public static Date getRanDomDate(Date min,Date max){
        int day=DataUtils.daysBetween(min,max);
        int random=DataUtils.getRanDom(0,day);
        return DataUtils.dateAddDay(min,random);
    }

    /**
     * 获取匹配的字符串
     * @param info
     * @param regex
     * @return
     */
    public static String[] getString(String info,String regex){
        StringBuffer sb=new StringBuffer();
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(info);
        while (m.find()){
            sb.append(m.group()).append(",");
        }
        if (DataUtils.isEmptyOrNull(sb.toString()))
            return null;
        else {
            sb.deleteCharAt(sb.length()-1);
            return sb.toString().split(",");
        }
    }

    public static Date dateAddMinute(Date date, int minute) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(12, minute);
        return ca.getTime();
    }

    public static Date dateAddDay(Date date, int day) {
        int minute = day * 24 * 60;
        return dateAddMinute(date, minute);
    }

    public static int daysOfTwo(Date begin, Date end) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(begin);
        int day1 = aCalendar.get(6);
        aCalendar.setTime(end);
        int day2 = aCalendar.get(6);
        return day2 - day1;
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate)
    {
        SimpleDateFormat sdf=new SimpleDateFormat(DataUtils.DATEFORMAT_DATA_EN_LONG);
        try {
            smdate=sdf.parse(sdf.format(smdate));
            bdate=sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static Date toDate(String text, String format) {
        if (DataUtils.isEmptyOrNull(text))
            return null;
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static Date dateAddMinute(Date date, int minute) {
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(date);
//        ca.add(12, minute);
//        return ca.getTime();
//    }
//
//    public static Date dateAddDay(Date date, int day) {
//        int minute = day * 24 * 60;
//        return dateAddMinute(date, minute);
//    }
//
//    public static void copyFile(File sourcefile,File targetFile) throws IOException {
//
//        //新建文件输入流并对它进行缓冲
//        FileInputStream input=new FileInputStream(sourcefile);
//        BufferedInputStream inbuff=new BufferedInputStream(input);
//
//        //新建文件输出流并对它进行缓冲
//        FileOutputStream out=new FileOutputStream(targetFile);
//        BufferedOutputStream outbuff=new BufferedOutputStream(out);
//
//        //缓冲数组
//        byte[] b=new byte[1024*5];
//        int len=0;
//        while((len=inbuff.read(b))!=-1){
//            outbuff.write(b, 0, len);
//        }
//
//        //刷新此缓冲的输出流
//        outbuff.flush();
//
//        //关闭流
//        inbuff.close();
//        outbuff.close();
//        out.close();
//        input.close();
//
//
//    }
//
//    public static void copyDirectiory(String sourceDir,String targetDir) throws IOException{
//
//        //新建目标目录
//
//        (new File(targetDir)).mkdirs();
//
//        //获取源文件夹当下的文件或目录
//        File[] file=(new File(sourceDir)).listFiles();
//
//        for (int i = 0; i < file.length; i++) {
//            if(file[i].isFile()){
//                //源文件
//                File sourceFile=file[i];
//                //目标文件
//                File targetFile=new File(new File(targetDir).getAbsolutePath()+File.separator+file[i].getName());
//
//                copyFile(sourceFile, targetFile);
//
//            }
//
//            if(file[i].isDirectory()){
//                //准备复制的源文件夹
//                String dir1=sourceDir+"/"+file[i].getName();
//                //准备复制的目标文件夹
//                String dir2=targetDir+"/"+file[i].getName();
//
//                copyDirectiory(dir1, dir2);
//            }
//        }
//
//    }
//
//    public static boolean isBasisClass(Class<?> cls)
//    {
//        return (cls.isPrimitive()) || (cls.isAssignableFrom(String.class)) || (cls.isAssignableFrom(Date.class)) || (isWrapClass(cls)) || (cls.isEnum());
//    }
//
//    public static boolean isWrapClass(Class<?> cls)
//    {
//        try
//        {
//            return ((Class)cls.getField("TYPE").get(null)).isPrimitive(); } catch (Exception e) {
//        }
//        return false;
//    }
//
//    public static String getJsonSerialize(Object obj)
//    {
//        ISerialize serialize = new JsonSerialize();
//        return serialize.serialize(obj);
//    }
//
//    public static Object getJsonDeserialize(String json, Class<?> cls)
//    {
//        ISerialize serialize = new JsonSerialize();
//        return serialize.deserialize(json, cls);
//    }
//
//    public static <T> T getJsonDeserializeT(String json, Class<T> cls)
//    {
//        ISerialize serialize = new JsonSerialize();
//        return serialize.deserializeT(json, cls);
//    }
//
//    public static <T> List<T> getJsonDeserializeListT(String json,Class<T> cls)
//    {
//        ISerialize serialize = new JsonSerialize();
//        return serialize.deserializeListT(json, cls);
//    }

}
