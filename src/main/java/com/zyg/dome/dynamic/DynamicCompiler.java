package com.zyg.dome.dynamic;

import com.example.utils.AnyCommon;
import com.example.utils.GlobalAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class DynamicCompiler {
    private static Logger logger = LoggerFactory.getLogger(DynamicCompiler.class);
    private String javaFileName;
    private String className;
    private String fullClassName;
    private String packagePath;
    private String classTargetPath;

    public DynamicCompiler(String javaFileName,String subMoudleName) {
        this.javaFileName = javaFileName;
        this.className = AnyCommon.getStringBySplitIndex(javaFileName, File.separator,-1).replace(".java","");
        String projectRootPath = GlobalAttr.getProjectRootPath(subMoudleName);
        if(projectRootPath == null){
            return;
        }
        String tmp_directoryName = AnyCommon.subStringBySplitIndex(javaFileName,File.separator,-1)
                .replace(projectRootPath,"")
                .replace(String.format("%s%s",File.separator,GlobalAttr.getJavaPathName()),"");
        this.packagePath = tmp_directoryName.substring(1,tmp_directoryName.length());
        this.fullClassName = String.format("%s.%s",packagePath.replace(File.separator,"."),className);
        this.classTargetPath = GlobalAttr.adaptation(String.format("%s/%s",projectRootPath,"target/classes"));
    }


    public Class comPilerLoad(){
        if(classTargetPath == null){
            return null;
        }
        Class resultClass = null;
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null,null,null);
            Iterable<? extends JavaFileObject> javaFileObjects = manager.getJavaFileObjects(javaFileName);
            List<String> options = new ArrayList<>();
            options.add("-d");
            options.add(classTargetPath);
            JavaCompiler.CompilationTask task = compiler.getTask(null,manager,null,options,null,javaFileObjects);
            boolean result = task.call();
            manager.close();
            if(!result){
                return null;
            }
            URL[] urls = new URL[] {new URL(String.format("file:/%s",classTargetPath))};
            URLClassLoader loader = new URLClassLoader(urls);
            resultClass = loader.loadClass(fullClassName);
        }
        catch (IOException | ClassNotFoundException e){
            logger.error("comPilerLoad error: ",e);
        }
        return resultClass;
    }
}
