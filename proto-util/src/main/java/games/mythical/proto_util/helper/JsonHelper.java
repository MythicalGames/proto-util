package games.mythical.proto_util.helper;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class JsonHelper {
  public final static String EMPTY_JSON_OBJECT_STRING = "{}";

  public static Map<String, Object> toStringObjectMap(String stringObjectJson) {
    Gson gson = new Gson();
    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
    return gson.fromJson(stringObjectJson, mapType);
  }

  public static String toJsonString(Object object) {
    Gson gson = new Gson();
    return gson.toJson(object);
  }

  public static String toJsonStringSnakeCase(Object object) {
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    return gson.toJson(object);
  }

  public static <T> T fromJson(String jsonString, Class<T> retClass) {
    Gson gson = new Gson();
    return gson.fromJson(jsonString, retClass);
  }

  public static <T> List<T> fromJsonArray(String jsonString, Class<T> retClass) {
    var gson = new Gson();
    var listObj = gson.fromJson(jsonString, TypeToken.getParameterized(List.class, retClass).getType());
    return (ArrayList<T>) listObj;
  }

  public static <T> T fromJsonSnakeCase(String jsonString, Class<T> retClass) {
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();

    return gson.fromJson(jsonString, retClass);
  }

  public static <T> List<T> fromJsonSnakeCaseListType(String jsonString, Class<T> retClass) {
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();

    Type complexType = TypeToken.getParameterized(List.class, retClass).getType();
    return gson.fromJson(jsonString, complexType);
  }

  public static Map<String, Integer> toStringIntegerMap(String stringIntegerJson) {
    Gson gson = new Gson();
    Type mapType = new TypeToken<Map<String, Integer>>(){}.getType();
    return gson.fromJson(stringIntegerJson, mapType);
  }

  public static String addFieldToJsonString(String stringObjectJson, String key, String value) {
    Map<String, Object> map;
    if (StringUtils.isBlank(stringObjectJson)) {
      map = new HashMap<>();
    } else {
      map = toStringObjectMap(stringObjectJson);
    }

    map.put(key, value);
    return toJsonString(map);
  }
}
