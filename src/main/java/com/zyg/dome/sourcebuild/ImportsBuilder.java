package com.zyg.dome.sourcebuild;

import java.util.*;

public class ImportsBuilder {
    private Map<Object, String> typeMap;
    private String fullClassName;
    private String imports;

    public String getImports() {
        return imports;
    }

    public ImportsBuilder(String fullClassName, Map<Object, String> typeMap){
        this.fullClassName = fullClassName;
        this.typeMap = typeMap;
    }

    public void importsBuild(){
        if(typeMap == null || fullClassName == null){
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        List<String> tmps = new ArrayList<>();
        for (Object key : typeMap.keySet()) {
            String type = typeMap.get(key);
            if (!type.equals(fullClassName) && !tmps.contains(type)) {
                stringBuilder.append(String.format("import %s;\n", type));
                tmps.add(type);
            }
        }
        imports = stringBuilder.toString();
    }
}
