package com.zyg.dome.sourcebuild;

import com.example.utils.AnyCommon;

import java.util.ArrayList;
import java.util.List;

public class ClassWriterEntiy {
    private static final String MODIFIER = "public";
    private static final String TYPE_NAME = "class";
    private String packageName;
    private String className;
    private ImportsBuilder importsBuilder;
    private List<AbstractCodeBuilder> fieldCodeBuilders;
    private List<AbstractCodeBuilder> methodCodeBuilders;
    private String entiy;

    public String getEntiy() {
        return entiy;
    }

    public ClassWriterEntiy(String fullClassName) {
        this.packageName = AnyCommon.subStringBySplitIndex(fullClassName,"\\.",-1);
        this.className = AnyCommon.getStringBySplitIndex(fullClassName,"\\.",-1);
        this.fieldCodeBuilders = new ArrayList<>();
        this.methodCodeBuilders = new ArrayList<>();
    }

    public void setImportsBuilder(ImportsBuilder importsBuilder) {
        this.importsBuilder = importsBuilder;
    }

    public void addFieldCodeBuilder(FieldCodeBuilder fieldCodeBuilder) {
        fieldCodeBuilders.add(fieldCodeBuilder);
    }

    public void addMethodCodeBuilder(MethodCodeBuilder methodCodeBuilder) {
        methodCodeBuilders.add(methodCodeBuilder);
    }

    private String toStringByCodeBuilders(List<AbstractCodeBuilder> list){
        if(list == null){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(AbstractCodeBuilder abstractCodeBuilder: list){
            String content = abstractCodeBuilder.getContent();
            if(content != null){
                stringBuilder.append(abstractCodeBuilder.getContent());
            }
        }
        return stringBuilder.toString();
    }

    public void createWriterEntiy(){
        String fieldsContent = toStringByCodeBuilders(fieldCodeBuilders);
        String methodsContent = toStringByCodeBuilders(methodCodeBuilders);
        if(fieldsContent == null || methodsContent == null || importsBuilder.getImports() == null){
            return;
        }
        entiy = String.format("package %s;\n\n%s\n%s %s %s {\n%s%s}",packageName,importsBuilder.getImports(),MODIFIER,TYPE_NAME,className,fieldsContent,methodsContent);
    }
}
