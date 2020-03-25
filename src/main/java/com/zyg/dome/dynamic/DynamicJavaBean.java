package com.zyg.dome.dynamic;

import com.zyg.dome.AnyCommon;
import com.zyg.dome.GlobalAttr;
import com.zyg.dome.sourcebuild.ClassWriterEntiy;
import com.zyg.dome.sourcebuild.FieldCodeBuilder;
import com.zyg.dome.sourcebuild.ImportsBuilder;
import com.zyg.dome.sourcebuild.MethodCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class DynamicJavaBean {
    private static final Logger logger = LoggerFactory.getLogger(DynamicJavaBean.class);
    private String fullClassName;
    private String packageName;
    private String className;
    private String subMoudleName;
    private String javaFileName;
    private Map<Object,String> typeMap;

    public DynamicJavaBean(String fullClassName, String subMoudleName, Map<Object,String> typeMap){
        this.fullClassName = fullClassName;
        this.subMoudleName = subMoudleName;
        this.typeMap = typeMap;
        this.packageName = AnyCommon.subStringBySplitIndex(fullClassName,"\\.",-1);
        this.className = AnyCommon.getStringBySplitIndex(fullClassName,"\\.",-1);
        this.javaFileName = GlobalAttr.getJavaFileName(fullClassName,subMoudleName);
    }

    private FieldCodeBuilder buildFieldSpec(String key) {
        String type = AnyCommon.getStringBySplitIndex(typeMap.get(key),"\\.",-1);
        FieldCodeBuilder fieldCodeBuilder = FieldCodeBuilder.getCodeBuilderObject();
        fieldCodeBuilder.setName(key).setType(type).ContentBuild();
        return fieldCodeBuilder;
    }

    private MethodCodeBuilder buildReadMethodSpec(String key) {
        String methodName = String.format("get%s", AnyCommon.toUpper(key));
        String type = AnyCommon.getStringBySplitIndex(typeMap.get(key),"\\.",-1);
        MethodCodeBuilder readmethodCodeBuilder = MethodCodeBuilder.getCodeBuilderObject();
        readmethodCodeBuilder
                .setName(methodName)
                .setType(type)
                .setInitValue(String.format("return this.%s",key))
                .ContentBuild();
        return readmethodCodeBuilder;
    }

    private MethodCodeBuilder buildWirteMethodSpec(String key) {
        String methodName = String.format("set%s", AnyCommon.toUpper(key));
        String type = AnyCommon.getStringBySplitIndex(typeMap.get(key),"\\.",-1);
        MethodCodeBuilder wirtemethodCodeBuilder = MethodCodeBuilder.getCodeBuilderObject();
        wirtemethodCodeBuilder
                .setName(methodName)
                .setType("void")
                .addParameter(type,key)
                .setInitValue(String.format("this.%s = %s",key,key))
                .ContentBuild();
        return wirtemethodCodeBuilder;
    }

    public void createJavaFile() {
        //System.out.println(this.name);
        if(javaFileName == null){
            return;
        }
        ImportsBuilder importsBuilder = new ImportsBuilder(fullClassName,typeMap);
        importsBuilder.importsBuild();
        ClassWriterEntiy classWriterEntiy = new ClassWriterEntiy(fullClassName);
        classWriterEntiy.setImportsBuilder(importsBuilder);
        for(Object key: typeMap.keySet()){
            classWriterEntiy.addFieldCodeBuilder(buildFieldSpec(key.toString()));
            classWriterEntiy.addMethodCodeBuilder(buildReadMethodSpec(key.toString()));
            classWriterEntiy.addMethodCodeBuilder(buildWirteMethodSpec(key.toString()));
        }
        classWriterEntiy.createWriterEntiy();
        try{
            FileOutputStream fs = new FileOutputStream(new File(javaFileName));
            fs.write(classWriterEntiy.getEntiy().getBytes());
            fs.flush();
            fs.close();
        }
        catch (IOException e){
            logger.error("DynamicJavaBean createJavaFile error: ",e);
        }
    }

    public Class getJavaClass() {
        this.createJavaFile();
        if(javaFileName == null){
            return null;
        }
        DynamicCompiler dynamicCompiler = new DynamicCompiler(javaFileName,subMoudleName);
        return dynamicCompiler.comPilerLoad();
    }


    public Object getJavaObject() {
        Object object = null;
        try{
            object = getJavaClass().newInstance();
        }
        catch (InstantiationException | IllegalAccessException e){
            logger.error("DynamicJavaBean getJavaObject error: ",e);
        }
        return object;
    }
}
