package com.zyg.dome.sourcebuild;


public class FieldCodeBuilder extends AbstractCodeBuilder {

    private FieldCodeBuilder(String modifier, String type, String name, boolean isStatic, boolean isFinal, String initValue){
        super(modifier, type, name, isStatic, isFinal, initValue);
    }

    public static FieldCodeBuilder getCodeBuilderObject(){
        return new FieldCodeBuilder(ModifierAttr.PRIVATE,null,null,false,false,null);
    }

    public FieldCodeBuilder setModifier(String modifier) {
        return (FieldCodeBuilder) super.setModifier(modifier,this);
    }

    public FieldCodeBuilder setType(String type) {
        return (FieldCodeBuilder) super.setType(type,this);
    }

    public FieldCodeBuilder setName(String name) {
        return (FieldCodeBuilder)super.setName(name,this);
    }

    public FieldCodeBuilder setStatic(boolean aStatic) {
        return (FieldCodeBuilder)super.setStatic(aStatic,this);
    }

    public FieldCodeBuilder setFinal(boolean aFinal) {
        return (FieldCodeBuilder)super.setFinal(aFinal,this);
    }

    public FieldCodeBuilder setInitValue(String initValue) {
        return (FieldCodeBuilder)super.setInitValue(initValue,this);
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
        if(super.isStatic()){
            static_tmp = "static ";
        }
        if(super.isFinal()){
            final_tmp = "final ";
        }
        String tmp = String.format("%s %s%s%s %s",super.getModifier(),static_tmp,final_tmp,super.getType(),super.getName());
        if(super.getInitValue() != null){
            tmp = String.format("%s = %s",tmp,super.getInitValue());
        }
        super.setContent(String.format("    %s;\n\n",tmp));
    }

}
