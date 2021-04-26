package com.fan.componentization.apt_compiler;

import com.fan.componentization.apt_annotation.AppLifeCycle;
import com.fan.componentization.apt_annotation.LifeCycleConfig;
import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

//核心的注解处理类，在这里我们可以扫描源代码里所有的注解，找到我们需要的注解，然后做出相应处理
//@AutoService(Processor.class)
public class AppLikeProcessor2 extends AbstractProcessor {

    private Elements mElementUtils;
    private Map<String, AppLikeProxyClassCreator> mMap = new HashMap<>();
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
         messager = processingEnvironment.getMessager();
    }

    /**
     * 返回该注解处理器要解析的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        //返回注解类的全限定类名，我们这里要识别的注解类是 AppLifeCycle
        set.add(AppLifeCycle.class.getCanonicalName());
        return set;
    }

    //支持的源代码 java 版本号
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    //所有逻辑都在这里完成
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //这里返回所有使用了 AppLifeCycle 注解的元素
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AppLifeCycle.class);
        mMap.clear();

        if(elements.isEmpty()) {
            return false;
        }

        StringBuilder sb = new StringBuilder();
        //设置包名
        sb.append("package ").append("com.xx.xx.xx").append(";\n\n");

        //设置import部分
        sb.append("import android.content.Context;\n");
        sb.append("import com.fan.componentization.common.IAppLike;\n");
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            sb.append("import ").append(typeElement.getQualifiedName()).append(";\n\n");
        }

        sb.append("public class ").append("MMMMMMManager").append(" {\n\n");


        sb.append("public static void init(Context context)").append(" {\n\n");

        //遍历所有使用了该注解的元素
        for (Element element : elements) {
            //强制转换为TypeElement，也就是类元素，可以获取使用该注解的类的相关信息
            TypeElement typeElement = (TypeElement) element;

            //该类的全限定类名
            String fullClassName = typeElement.getQualifiedName().toString();


            sb.append("new  " + fullClassName + "().onCreate(context)" ).append(" ;\n\n");

        }

        sb.append(" }\n\n").append(" }\n\n");

        System.out.println("start to generate proxy class code");
        //生成代理类，并写入到文件里，生成逻辑都在AppLikeProxyClassCreator里实现
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile("com.xx.xx.xx.MMMMMMManager");
            Writer writer = jfo.openWriter();
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}