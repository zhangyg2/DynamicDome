package com.zyg.dome.dynamic;

import com.zyg.dome.AnyCommon;
import com.zyg.dome.CustomMapOper;
import com.zyg.dome.GlobalAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//依据反射原理封装的单例类，可存储每个目标对象的methods和fields到缓存map中可以使用封装的具体使用类调用这些map从而提升性能
public class MyClassUtils {
    private static Logger logger = LoggerFactory.getLogger(MyClassUtils.class);
    private List<Object> objects;
    private Map <Object,List<Field>> fieldsMap;
    private Map <Object,List<Method>> methodsMap;
    private Map <Object,List<Field>> allFieldsMap;
    private Map <Object,List<Method>> allMethodsMap;
    private Map <Object,Map<Object,Method>> readMethodsMap;
    private Map <Object,Map<Object,Method>> writeMethodsMap;
    private Map <Object,List<Object>> sameObjectKeysMap;
    private static final String FIFTER = "class";
    private static MyClassUtils classUtils;


    private MyClassUtils(){
        objects = new ArrayList<>();
        methodsMap = new HashMap<>();
        fieldsMap = new HashMap<>();
        allFieldsMap = new HashMap<>();
        allMethodsMap = new HashMap<>();
        readMethodsMap = new HashMap<>();
        writeMethodsMap = new HashMap<>();
        sameObjectKeysMap = new HashMap<>();
    }


    public static MyClassUtils getClassUtils(){
        if(classUtils == null){
            synchronized (MyClassUtils.class){
                classUtils = new MyClassUtils();
            }
        }
        return classUtils;
    }


    public List<Object> getObjects() {
        return objects;
    }


    private void addallSuperMethods(Class cclass,List<Method> methods){
        if(cclass == null || methods == null || cclass.getName().equals(GlobalAttr.getObjectClassName())){
            return;
        }
        methods.addAll(Arrays.asList(cclass.getDeclaredMethods()));
        addallSuperMethods(cclass.getSuperclass(),methods);
    }


    private void addallSuperFields(Class cclass,List<Field> fields){
        if(cclass == null || fields == null || cclass.getName().equals(GlobalAttr.getObjectClassName())){
            return;
        }
        fields.addAll(Arrays.asList(cclass.getDeclaredFields()));
        addallSuperFields(cclass.getSuperclass(),fields);
    }


    private Method getWriteFromMethods(List<Method> methods,String key){
        if(methods == null || key == null){
            return null;
        }
        String writeMethodName = String.format("set%s", AnyCommon.toUpper(key));
        for(Method m: methods){
            if(m.getName().equals(writeMethodName)){
                return m;
            }
        }
        return null;
    }

    private Method getReadFromMethods(List<Method> methods,String key){
        if(methods == null || key == null){
            return null;
        }
        String readMethodName = String.format("get%s",AnyCommon.toUpper(key));
        for(Method m: methods){
            if(m.getName().equals(readMethodName)){
                return m;
            }
        }
        return null;
    }

    public String getTypeFromFields(List<Field> fields,String key){
        if(fields == null || key == null){
            return null;
        }
        for(Field f: fields){
            if(f.getName().equals(key)){
                return f.getType().toString();
            }
        }
        return null;
    }


    //存储Bean对象的methods和fields到本地
    public void addCaches(Object object){
        if(object == null){
            return;
        }
        if(objects.contains(object)){
            //logger.warn("classUtils缓存中已经存在object，不再做存储操作");
            return;
        }
        List<Field> fields = new ArrayList<>();
        List<Object> sameObjectKeys = new ArrayList<>();
        List<Field> allFields = new ArrayList<>();
        List<Method> allMethods = new ArrayList<>();
        Map <Object,Method> tmp_read_map = new HashMap<>();
        Map <Object,Method> tmp_write_map = new HashMap<>();
        synchronized (MyClassUtils.class){
            Field[] declFields = object.getClass().getDeclaredFields();
            List<Method> methods = Arrays.asList(object.getClass().getDeclaredMethods());
            addallSuperFields(object.getClass(),allFields);
            addallSuperMethods(object.getClass(),allMethods);
            for(Field f: declFields){
                String fieldName = f.getName();
                String fieldType = f.getType().toString();
                if(!fieldName.equals(FIFTER)){
                    fields.add(f);
                    if(fieldType.equals(object.getClass().getName())){
                        sameObjectKeys.add(fieldName);
                    }
                    tmp_read_map.put(fieldName,getReadFromMethods(methods,fieldName));
                    tmp_write_map.put(fieldName,getWriteFromMethods(methods,fieldName));
                }
            }
            objects.add(object);
            fieldsMap.put(object,fields);
            methodsMap.put(object,methods);
            allFieldsMap.put(object,allFields);
            allMethodsMap.put(object,allMethods);
            readMethodsMap.put(object,tmp_read_map);
            writeMethodsMap.put(object,tmp_write_map);
            sameObjectKeysMap.put(object,sameObjectKeys);
        }
    }

    public void delCache(Object object){
        if(!objects.contains(object)){
            return;
        }
        objects.remove(object);
        CustomMapOper customMapOper = new CustomMapOper();
        customMapOper.removeSameObject(object,fieldsMap,methodsMap,allFieldsMap,allMethodsMap,readMethodsMap,writeMethodsMap,sameObjectKeysMap);
    }

    public Map<Object,Method> getReadMethodsMapByObj(Object object){
        if(object == null){
            return null;
        }
        return readMethodsMap.get(object);
    }


    public Map<Object,Method> getWriteMethodsMapByObj(Object object){
        if(object == null){
            return null;
        }
        return writeMethodsMap.get(object);
    }

    public List<Field> getFieldsByObj(Object object){
        if(object == null){
            return null;
        }
        return fieldsMap.get(object);
    }


    public List<Method> getMethodsByObj(Object object){
        if(object == null){
            return null;
        }
        return methodsMap.get(object);
    }

    public List<Field> getAllFieldsByObj(Object object){
        if(object == null){
            return null;
        }
        return allFieldsMap.get(object);
    }


    public List<Method> getAllMethodsByObj(Object object){
        if(object == null){
            return null;
        }
        return allMethodsMap.get(object);
    }


    public List<Object> getSameObjectKeysByObj(Object object){
        if(object == null){
            return null;
        }
        return sameObjectKeysMap.get(object);
    }


    public Object invoke(Object object,Method method){
        if(object == null || method == null){
            return null;
        }
        Object invokeResult = null;
        try{
            invokeResult = method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("invoke error: ",e);
        }
        return invokeResult;
    }


    public void invoke(Object object,Method method,Object value){
        if(object == null || method == null){
            return;
        }
        try{
            method.invoke(object,value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("invoke error: ", e);
        }
    }
}
