package com.zyg.dome.sourcebuild;

public abstract class AbstractCodeBuilder {
    private String modifier;
    private String type;
    private String name;
    private boolean isStatic;
    private boolean isFinal;
    private String initValue;
    private String content;

    protected AbstractCodeBuilder(String modifier, String type, String name, boolean isStatic, boolean isFinal, String initValue){
        this.modifier = modifier;
        this.type = type;
        this.name = name;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.initValue = initValue;
    }

    protected AbstractCodeBuilder setModifier(String modifier, AbstractCodeBuilder abstractCodeBuilder) {
        if(!ModifierAttr.equals(modifier)){
            modifier = ModifierAttr.PUBLIC;
        }
        this.modifier = modifier;
        return abstractCodeBuilder;
    }


    protected AbstractCodeBuilder setType(String type, AbstractCodeBuilder abstractCodeBuilder) {
        this.type = type;
        return abstractCodeBuilder;
    }


    protected AbstractCodeBuilder setName(String name, AbstractCodeBuilder abstractCodeBuilder) {
        this.name = name;
        return abstractCodeBuilder;
    }


    protected AbstractCodeBuilder setStatic(boolean aStatic, AbstractCodeBuilder abstractCodeBuilder) {
        this.isStatic = aStatic;
        return abstractCodeBuilder;
    }


    protected AbstractCodeBuilder setFinal(boolean aFinal, AbstractCodeBuilder abstractCodeBuilder) {
        this.isFinal = aFinal;
        return abstractCodeBuilder;
    }


    protected AbstractCodeBuilder setInitValue(String initValue, AbstractCodeBuilder abstractCodeBuilder) {
        this.initValue = initValue;
        return abstractCodeBuilder;
    }

    protected void setContent(String content){
        this.content = content;
    }

    protected String getModifier() {
        return this.modifier;
    }

    protected String getType() {
        return this.type;
    }

    protected String getName() {
        return this.name;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public boolean isFinal() {
        return this.isFinal;
    }

    protected String getInitValue() {
        return initValue;
    }

    protected String getContent() {
        return this.content;
    }

    protected abstract void ContentBuild();

}
