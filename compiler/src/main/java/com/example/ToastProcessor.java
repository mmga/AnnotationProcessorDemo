package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by Think on 2017/7/10.
 */

@AutoService(Processor.class)
public class ToastProcessor extends AbstractProcessor {

    private Elements elementUtils;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MmgaToast.class);
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            MethodSpec.Builder toastMethodBuilder = MethodSpec.methodBuilder("toast")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(ClassName.get(typeElement.asType()), "activity")
                    .returns(TypeName.VOID)
                    //bestGuess方法是在apt module内无法获取到某个Class，但是在app module里能获取到时采用
                    .addStatement("$T.makeText(activity, $S, Toast.LENGTH_SHORT).show()",
                            ClassName.bestGuess("android.widget.Toast"),
                            element.getAnnotation(MmgaToast.class).value());

            TypeSpec typeSpec = TypeSpec.classBuilder(element.getSimpleName() + "Toaster")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(toastMethodBuilder.build())
                    .build();

            JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec)
                    .build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(MmgaToast.class.getCanonicalName());
    }
}
