package com.zyg.dome;

import java.util.*;

public final class CustomMapOper {
    private Map<Object, String> typeMap;
    private List<Object> sameTypeKeys;

    public Map<Object, String> getTypeMap() {
        return typeMap;
    }

    public List<Object> getSameTypeKeys() {
        return sameTypeKeys;
    }

    private boolean isHaveMap(Map<Object, Object> map){
        int Case = 0;
        for(Object key : map.keySet()){
            if(map.get(key) instanceof Map){
                Case ++;
            }
        }
        return Case != 0;
    }

    public void setkeysTypeMap(Map map){
        if(typeMap == null){
            typeMap = new HashMap<>();
        }
        if(sameTypeKeys == null){
            sameTypeKeys = new ArrayList<>();
        }
        ArrayList<Map> mapsList = new ArrayList<>();
        for(Object key:map.keySet()){
            if(map.get(key) instanceof Map){
                mapsList.add((Map) map.get(key));
                sameTypeKeys.add(key.toString());

            }
            if(!typeMap.containsKey(key)){
                typeMap.put(key,map.get(key).getClass().getName());
            }
        }
        for(Map m: mapsList){
            setkeysTypeMap(m);
        }
    }

    public void setkeysTypeMap(Map map,String old_type,String new_type){
        if(typeMap == null){
            typeMap = new HashMap<>();
        }
        if(sameTypeKeys == null){
            sameTypeKeys = new ArrayList<>();
        }
        ArrayList<Map<Object, Object>> mapsList = new ArrayList<>();
        for(Object key:map.keySet()){
            if(map.get(key) instanceof Map){
                mapsList.add((Map) map.get(key));
                sameTypeKeys.add(key);
            }
            if(!typeMap.containsKey(key)){
                String typeName = map.get(key).getClass().getName();
                if(typeName.contains(old_type)){
                    typeMap.put(key,new_type);
                }
                else{
                    typeMap.put(key,typeName);
                }
            }
        }
        for(Map m: mapsList){
            setkeysTypeMap(m,old_type,new_type);
        }
    }


    public void removeSameObject(Object...objects){
        Object same = objects[0];
        if(same == null){
            return;
        }
        for(int i=1;i<objects.length;i++){
            if(objects[i] == null || !(objects[i] instanceof Map)){
                continue;
            }
            Map map = (Map) objects[i];
            map.remove(same);
        }
    }

    public void replaceValuesForEach(Map map, String old_value, String new_value){
        if(map == null){
            return;
        }
        Iterator iterator = map.keySet().iterator();
        Object key = iterator.next();
        while (iterator.hasNext()){
            if(map.get(key).toString().contains(old_value)){
                map.put(key,new_value);
            }
        }
    }


    public Object getMapforEachBySearch(Map<Object, Object> map, String search){
        Object result = null;
        for(Object key : map.keySet()){
            if(key.equals(search)){
                result = map.get(search);
            }
            else if(map.get(key) instanceof Map){
                result = getMapforEachBySearch((Map<Object, Object>) map.get(key),search);
            }
            if(result != null){
                break;
            }
        }
        return result;
    }


    public ArrayList<Object> getMapsforEachBySearch(HashMap map, String search,String node,ArrayList<String> nodes){
        ArrayList<Object> resultMaps = new ArrayList<>();
        if((node == null) || (node.isEmpty())){
            node = "root";
        }
        nodes.add(node);
        for(Object key : map.keySet()){
            HashMap<Object, Object> m = new HashMap<>();
            if(key.equals(search)){
                m.put("value",map.get(key));
                m.put("nodes",nodes);
                resultMaps.add(m);
            }
            else if(map.get(key) instanceof HashMap){
                resultMaps.addAll(getMapsforEachBySearch((HashMap) map.get(key),search,key.toString(),nodes));
            }
        }
        return resultMaps;
    }
}
