package org.writer.annotation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("org.writer.annotation.CsvMasked")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class CsvMaskedCollectionValidator extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        TypeMirror collectionType = elementUtils.getTypeElement(Collection.class.getCanonicalName()).asType();
        TypeMirror mapType = elementUtils.getTypeElement(Map.class.getCanonicalName()).asType();

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(CsvMasked.class)) {
            TypeMirror fieldType = annotatedElement.asType();

            // Проверяем, является ли тип поля подтипом Collection или Map
            // Используем typeUtils.erasure() для работы с "сырыми" типами, чтобы не беспокоиться о дженериках
            if (typeUtils.isSubtype(typeUtils.erasure(fieldType), typeUtils.erasure(collectionType)) ||
                    typeUtils.isSubtype(typeUtils.erasure(fieldType), typeUtils.erasure(mapType))) {
                messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "@CsvMasked не может быть применена к полям типа Collection или Map.",
                        annotatedElement
                );
            }
        }
        return true;
    }
}
