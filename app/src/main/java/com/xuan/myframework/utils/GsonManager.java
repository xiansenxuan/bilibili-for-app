package com.xuan.myframework.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * com.toonyoo.smartterminal_superplayer..model.utils
 * json解析到javabean
 *
 * @author by xuan on 2017/4/24
 * @version [1.0, 2017/4/24]
 * @update by xuan on 2017/9/14
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class GsonManager {

    /**
     * 解析字符串到对象数组
     * @param result
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> beanToJsonArray(String result,Class<T> cls){
        ArrayList<T> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(result).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 解析字符串到对象
     * @param result
     * @return t 解析json到bean
     */
    public static <T> T jsonToBean(String result, Class<T> clazz) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(result, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
