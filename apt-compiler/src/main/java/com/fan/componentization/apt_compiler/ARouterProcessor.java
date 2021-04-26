package com.fan.componentization.apt_compiler;

import com.fan.componentization.apt_annotation.ARouter;
import com.fan.componentization.apt_annotation.BindActivity;
import com.fan.componentization.apt_annotation.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/4/23 14:46
 * @Modify:
 */

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.fan.componentization.apt_annotation.ARouter"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ARouterProcessor extends AbstractProcessor {

    Elements elementUtils;
    Filer filer;
    Messager messager;
    Types typeUtils;
    Map<String, String> options;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        options = processingEnv.getOptions();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        if (elements.isEmpty()) {
            return false;
        }

        if(options!=null) {
            messager.printMessage(Diagnostic.Kind.NOTE, "the options is : " + options);
        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "the options is null: " );
        }


        MethodSpec.Builder methodBuilder = null;
        int index = 0;
        messager.printMessage(Diagnostic.Kind.NOTE, "the size is : " + elements.size());
        TypeElement typeElement = null;
        methodBuilder = MethodSpec.methodBuilder("findTargetClass")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(Class.class)
                .addParameter(String.class, "path");
        for (Element element : elements) {
            messager.printMessage(Diagnostic.Kind.NOTE, "index is : " + index);
//            if (!(element instanceof TypeElement)) {
//                index++;
//                continue;
//            }

            typeElement = (TypeElement) element;

            ARouter annotation = typeElement.getAnnotation(ARouter.class);


            //addStatement 会给语句后加上   ;\n
            //beginControlFlow 会给语句加上   {\n
            //endControlFlow 会添加一行    }\n

            if (index == 0) {
                messager.printMessage(Diagnostic.Kind.NOTE, "dengyu ===0, 开始 if");
                methodBuilder.beginControlFlow("if (path.equals($S)) ", annotation.path());
                methodBuilder.addStatement("return $T.class ", ClassName.get(typeElement));
//                methodBuilder.addStatement("if (path.equals($S)) {", annotation.path());
//                methodBuilder.addStatement("return $T.class }", ClassName.get(typeElement)).en;
            } else {
                methodBuilder.nextControlFlow("else if (path.equals($S)) ", annotation.path());
                methodBuilder.addStatement("return $T.class ", ClassName.get(typeElement)).endControlFlow();
            }

            if(elements.size()==1) {
                methodBuilder.endControlFlow();
            }

            if (index == elements.size() - 1) {
                methodBuilder.addStatement("return null");
            }

            index++;
        }

        String moduleName = options.get("AROUTER_MODULE_NAME");
        if(moduleName == null || moduleName.trim().isEmpty()) {
            moduleName = "DDDDD";
        }

        TypeSpec typeSpec = TypeSpec.classBuilder("ARouter$$AAA$" + moduleName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodBuilder.build())
                .build();
        JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}
