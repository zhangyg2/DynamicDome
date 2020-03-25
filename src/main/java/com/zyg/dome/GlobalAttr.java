package com.zyg.dome;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

public final class GlobalAttr {
    private static Logger logger = LoggerFactory.getLogger(GlobalAttr.class);
    private static String homePath = FileSystemView.getFileSystemView().getHomeDirectory().getPath();

    private static final String log4jConfigFileName = "log4j";

    private static final String desktopPathName = "Desktop";

    private static final String downloadsPathName = "Downloads";

    private static final String resourcesPathName = "src/main/resources";

    private static final String javaPathName = "src/main/java";

    private static final String OBJECT_CLASS_NAME = "java.lang.Object";

    private static final String MAP_INTERFACE_NAME = "Map";

    public static String getJavaPathName() {
        return javaPathName;
    }

    public static String getLog4jConfigFileName() {
        return log4jConfigFileName;
    }

    public static String getDesktopPathName() {
        return desktopPathName;
    }

    public static String getDownloadsPathName() {
        return downloadsPathName;
    }

    public static String getObjectClassName() {
        return OBJECT_CLASS_NAME;
    }

    public static String getMapInterfaceName() {
        return MAP_INTERFACE_NAME;
    }

    public static String adaptation(String path){
        return AnyCommon.joinByNewSeparator(path,"/",File.separator);
    }

    public static String getDektopPath(){
        return String.format("%s%s%s", homePath,File.separator,desktopPathName);
    }

    public static String getDownloadsPath(){
        return String.format("%s%s%s", homePath,File.separator,downloadsPathName);
    }

    private static String getDefaultProjectRootPath(){
        String defaultProjectRootPath = null;
        try{
            defaultProjectRootPath = new File("").getCanonicalPath();
        }
        catch (IOException e){
            logger.error("getDefaultProjectRootPath error: ",e);
        }
        return defaultProjectRootPath;
    }

    public static String getProjectRootPath(String subMoudleName){
        String defaultProjectRootPath = getDefaultProjectRootPath();
        if(defaultProjectRootPath == null){
            return null;
        }
        if(subMoudleName == null || subMoudleName.isEmpty()){
            return defaultProjectRootPath;
        }
        return adaptation(String.format("%s/%s",defaultProjectRootPath,subMoudleName));
    }

    public static String getResourcesPath(String subMoudleName) {
        String projectRootPath = getProjectRootPath(subMoudleName);
        if(projectRootPath == null){
            return null;
        }
        return adaptation(String.format("%s/%s",projectRootPath,resourcesPathName));
    }

    public static String getJavaPath(String subMoudleName) {
        String projectRootPath = getProjectRootPath(subMoudleName);
        if(projectRootPath == null){
            return null;
        }
        return adaptation(String.format("%s/%s",projectRootPath,javaPathName));
    }

    public static String getJavaFileName(String fullClassName,String subMoudleName){
        String javaPath = getJavaPath(subMoudleName);
        if(javaPath == null){
            return null;
        }
        return adaptation(String.format("%s/%s.java",javaPath,fullClassName.replace(".","/")));
    }
}
