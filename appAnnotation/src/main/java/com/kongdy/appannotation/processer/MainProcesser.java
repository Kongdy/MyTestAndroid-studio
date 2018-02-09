package com.kongdy.appannotation.processer;

import com.google.auto.common.MoreElements;
import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.kongdy.appannotation.annotation.Test;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author kongdy
 * @date 2018/2/6 14:26
 * @describe TODO
 **/

@AutoService(Processor.class)
public class MainProcesser extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportAnnotation()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportAnnotation() {
        return new LinkedHashSet<Class<? extends Annotation>>() {
            {
                add(Test.class);
            }
        };
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

        for (Element element : env.getElementsAnnotatedWith(Test.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;

            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            String packName = MoreElements.getPackage(typeElement).getQualifiedName().toString();
            String className = typeElement.getQualifiedName().toString().substring(packName.length()+1)
                    .replace(".","$")+"_TestGenerate";
            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC);
            JavaFile javaFile = JavaFile.builder(packName,typeBuilder.build()).build();
            try {

                javaFile.writeTo(System.out);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return false;
    }

    private void parseTest() {

    }
}
