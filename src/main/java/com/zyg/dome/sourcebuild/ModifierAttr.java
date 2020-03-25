package com.zyg.dome.sourcebuild;

public class ModifierAttr {
    public static final String PRIVATE = "private";

    public static final String PUBLIC = "public";

    public static final String PROTECTED = "protected";


    public static boolean equals(String modifier){
        if(modifier == null){
            return false;
        }
        return modifier.equals(PRIVATE) || modifier.equals(PUBLIC) || modifier.equals(PROTECTED);
    }
}
