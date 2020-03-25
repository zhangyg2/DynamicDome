package com.zyg.dome.sourcebuild;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MethodCodeBuilder extends AbstractCodeBuilder {

    private Map<String,String> parameter = new HashMap<>();

    private MethodCodeBuilder(String modifier, String type, String name, boolean isStatic, boolean isFinal, String initValue){
        super(modifier, type, name, isStatic, isFinal, initValue);
    }

    public static MethodCodeBuilder getCodeBuilderObject(){
        return new MethodCodeBuilder(ModifierAttr.PUBLIC,null,null,false,false,null);
    }

    public MethodCodeBuilder setModifier(String modifier) {
        return (MethodCodeBuilder) super.setModifier(modifier,this);
    }

    public MethodCodeBuilder setType(String type) {
        return (MethodCodeBuilder) super.setType(type,this);
    }

    public MethodCodeBuilder setName(String name) {
        return (MethodCodeBuilder)super.setName(name,this);
    }

    public MethodCodeBuilder setStatic(boolean aStatic) {
        return (MethodCodeBuilder)super.setStatic(aStatic,this);
    }

    public MethodCodeBuilder setFinal(boolean aFinal) {
        return (MethodCodeBuilder)super.setFinal(aFinal,this);
    }

    public MethodCodeBuilder setInitValue(String initValue) {
        return (MethodCodeBuilder)super.setInitValue(String.format("%s%s;\n","    ",initValue),this);
    }

    public MethodCodeBuilder addParameter(String p_type, String p_name){
        parameter.put(p_name,p_type);
        return this;
    }


    public String getContent(){
        return super.getContent();
    }

    @Override
    public void ContentBuild(){
        if(super.getType() == null || super.getName() == null){
            return;
        }
        String static_tmp = "";
        String final_tmp = "";
        String paramter_tmp = "";
        if(super.isStatic()){
            static_tmp = "static ";
        }
        if(super.isFinal()){
            final_tmp = "final ";
        }
        if(this.parameter.size() > 0){

            StringBuilder stringBuilder = new StringBuilder();
            Iterator it = parameter.keySet().iterator();
            while (it.hasNext()){
                String p_name = it.next().toString();
                String p_type = parameter.get(p_name);
                stringBuilder.append(String.format("%s %s ",p_type,p_name));
            }
            String builder_str = stringBuilder.toString();
            paramter_tmp = builder_str.substring(0,builder_str.length()-1);
        }
        String tmp = String.format("%s %s%s%s %s(%s) {\n",super.getModifier(),static_tmp,final_tmp,super.getType(),super.getName(),paramter_tmp);
        if(super.getInitValue() != null){
            tmp = String.format("    %s%s}\n\n",tmp,super.getInitValue());
        }
        super.setContent(tmp);
    }
}
