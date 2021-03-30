package com.ajwlforever.forum.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForumUtils {

    //生产随机UUID
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getTags(String tags)
    {
        String[] strs = tags.split("，");
        List<String> stringList = new ArrayList<String>();
        for(String str : strs)
        {
            if(str.length()>=5){
                str = str.substring(0,5);
            }
            stringList.add(str);
        }
        return JSONObject.toJSONString(stringList);
    }
    //帖子的最后活跃时间
    public static String getLastActiveTime(Date d1)
    {
        Date d2 = new Date();
        long mills = d2.getTime()-d1.getTime();
        if(mills<60*1000*60){
            return mills/(60*1000)+"分钟前";
        }else if(mills<60*1000*60*24){
            return mills/(60*1000*60)+"小时前";
        }else if((mills/1000)<(60*60*24*30)){
            return mills/(60*1000*60*24)+"天前";
        } else if((mills/1000)<60*60*24*365){
            return (mills/1000)/(60*60*24*30)+"个月前";
        } else
            return (mills/1000)/(60*60*24*365)+"年前";

    }
    //md5加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getRandomNickName()
    {
        return "AXForum-"+getStringRandom(6);
    }
    //自动生成名字(中文)
    public static String getRandomJianHan(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); // 转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }

    //生成随机用户名，数字和字母组成,
    public static  String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    //判断Email合法性
    public static boolean isEmail(String email) {
        if (email == null)
            return false;
        String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(rule);
        matcher = pattern.matcher(email);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static String toJsonString(int code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);

        if (map != null) {
            for (String key : map.keySet()) {
                jsonObject.put(key, map.get(key));
            }
        }

        return jsonObject.toJSONString();
    }

    public static String toJsonString(int code, String msg) {
        return toJsonString(code, msg, null);
    }

    public static String toJsonString(int code) {
        return toJsonString(code, null, null);
    }

    public static void main(String...args)
    {
//        System.out.println(getRandomJianHan(3));
//        System.out.println(getStringRandom(7));
//        System.out.println(getRandomNickName());
//        Calendar calendar = new GregorianCalendar(2020, 11, 25,0,0,0);
//
//        Date date = calendar.getTime();
//        System.out.println((new Date().getTime()-date.getTime()));
//
//        System.out.println(getLastActiveTime(date));
        System.out.println(getTags("地球，科普"));
    }
}
