package com.example.apt_processor;

import com.example.apt_annotation.ExecutorTest;
import com.example.apt_annotation.Learning;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;

@AutoService(Processor.class)
public class ExecutorProcessor extends AbstractProcessor {

    private Messager mMessager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(ExecutorTest.class.getCanonicalName());
        return hashSet;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "Hello APT");
    }

    public static CodeBlock generateInnerClause(List<? extends Element> elements) {
        Builder mainBuilder = CodeBlock.builder();
        if (elements.size() == 0) {
            return mainBuilder.addStatement("return").build();
        }

        for (Element element : elements) {
            ExecutorTest[] es = element.getAnnotationsByType(ExecutorTest.class);
            if (es[0].name().length() == 0) {
                continue;
            }

            mainBuilder.addStatement("new $T().run()", element.asType());
        }
        mainBuilder.addStatement("return");

        return mainBuilder.build();
    }

    private void generateCode(Set<? extends Element> elements) {
        //生成类
        TypeSpec.Builder classBuilder = TypeSpec
            .classBuilder("ExecutorFactory")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        List<Element> list = new ArrayList<>();
        Types typeUtils = processingEnv.getTypeUtils();
        Elements elementUtils = processingEnv.getElementUtils();
        Element learningElement = elementUtils.getTypeElement(Learning.class.getCanonicalName());
        for (Element element : elements) {
            // 判断是否实现了接口
            if (!typeUtils.isSubtype(element.asType(), learningElement.asType())) {
                mMessager.printMessage(Kind.WARNING, "APT, ignoring: " + element.getClass().getCanonicalName());
                continue;
            }
            list.add(element);
        }

        //生成方法
        MethodSpec method = MethodSpec.methodBuilder("run")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addCode(generateInnerClause(list)).build();

        classBuilder.addMethod(method);

        //包
        JavaFile javaFile = JavaFile
            .builder("com.example.hellokotlin", classBuilder.build())
            .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "process size=" + annotations.size());

        //拿到所有添加Print注解的成员变量
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ExecutorTest.class);
        // apt processor 会执行多次，这里通过 size 判断是否需要产生代码
        if (annotations.size() > 0) {
            // 产生代码
            generateCode(elements);
        } else {
            mMessager.printMessage(Kind.WARNING, "APT, empty");
        }

        return false;
    }
}
