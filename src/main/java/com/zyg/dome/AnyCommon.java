package com.zyg.dome;

public final class AnyCommon {
    public static String toUpper(String s,Integer index){
        if(index == null || index >= s.length()){
            return s;
        }
        char[] char_s = s.toCharArray();
        if(index < 0){
            index = char_s.length - Math.abs(index);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<char_s.length;i++){
            String tmp = String.valueOf(char_s[i]);
            if(i == index){
                stringBuilder.append(tmp.toUpperCase());
            }
            else{
                stringBuilder.append(tmp);
            }
        }
        return stringBuilder.toString();
    }

    public static String toUpper(String s){
        return toUpper(s,0);
    }

    public static String transformPropertiesKey(String properties_key){
        if(properties_key == null || properties_key.isEmpty() || !properties_key.contains("-")){
            return properties_key;
        }
        String []keys = properties_key.split("-");
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<keys.length;i++){
            String s = null;
            if(i == 0){
                s = keys[i];
            }
            else {
                s = toUpper(keys[i]);
            }
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public static String subStringBySplitIndex(String s,String separator,int index){
        if(s == null || s.isEmpty()){
            return s;
        }
        String[] slist = s.split(separator);
        if(index >= slist.length){
            return s;
        }
        if(index < 0){
            index = slist.length - Math.abs(index);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<slist.length;i++){
            if(i == index){
                continue;
            }
            stringBuilder.append(slist[i]);
            stringBuilder.append(separator);
        }
        String new_s = stringBuilder.toString().replace("\\","");
        return new_s.substring(0,new_s.length() - 1);
    }

    public static String getStringBySplitIndex(String s,String separator,int index){
        String[] slist = s.split(separator);
        if(index < slist.length){
            if(index < 0){
                index = slist.length - Math.abs(index);
            }
            for(int i=0;i<slist.length;i++){
                if(i == index){
                    return slist[i];
                }
            }
        }
        return s;
    }

    public static String joinByNewSeparator(String s,String old_separator,String new_separator){
        if(s == null || s.isEmpty()){
            return s;
        }
        String[] slist = s.split(old_separator);
        StringBuilder stringBuilder = new StringBuilder();
        for(String tmp: slist){
            stringBuilder.append(tmp);
            stringBuilder.append(new_separator);
        }
        String new_s = stringBuilder.toString();
        return new_s.substring(0,new_s.length() - 1);
    }

    public static String replaceUntilNoTarget(String s,String target,String new_target){
        if(!s.contains(target)){
            return s;
        }
        return replaceUntilNoTarget(s.replace(target,new_target),target,new_target);
    }

    public static Object parseByType(String type,String raw){
        if(type == null || raw == null){
            return null;
        }
        Object value = null;
         if(type.contains("int")){
            value = Integer.parseInt(raw);
        }
        else if(type.contains("boolean")){
            value = Boolean.parseBoolean(raw);
        }
        else if(type.contains("String")){
            value = raw;
        }
        return value;
    }
}


