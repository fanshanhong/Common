package com.fan.componentization.apt_compiler;

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

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/4/23 14:46
 * @Modify:
 */

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.fan.componentization.apt_annotation.BindActivity", "com.fan.componentization.apt_annotation.BindView"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BindProcessor extends AbstractProcessor {

    Elements elementUtils;
    Filer filer;
    Messager messager;
    Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindActivity.class);

        for (Element element : elements) {

            if (!(element instanceof TypeElement)) {
                continue;
            }

            TypeElement typeElement = (TypeElement) element;
            List<? extends Element> members = elementUtils.getAllMembers(typeElement);

            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("bindView")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(typeElement.asType()), "activity");

            for (Element item : members) {
                BindView annotation = item.getAnnotation(BindView.class);
                if (annotation == null) {
                    continue;
                }
                bindViewMethodSpecBuilder.addStatement(String.format("activity.%s = (%s) activity.findViewById(%s)", item.getSimpleName(), ClassName.get(item.asType()).toString(), annotation.value()));
            }

            TypeSpec typeSpec = TypeSpec.classBuilder("BBBBB" + element.getSimpleName() + "FuZhuLei")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindViewMethodSpecBuilder.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}
