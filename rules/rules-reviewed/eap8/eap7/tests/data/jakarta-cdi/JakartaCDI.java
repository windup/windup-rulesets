package com.test.project;

import java.lang.annotation.Annotation;

public class JakartaCDI {
    public static void main(String[] args) {

        if (javax.enterprise.inject.spi.Bean.isNullable()) {
            AnnotatedType<CdiRequestScope> annotatedType = beanManager.createAnnotatedType(CdiRequestScope.class);
            jakarta.enterprise.inject.spi.BeanManager.createInjectionTarget(annotatedType);

            jakarta.enterprise.inject.spi.BeanManager.fireEvent(new Object(), new Annotation());

            javax.enterprise.inject.spi.BeforeBeanDiscovery.addAnnotatedType(annotatedType);
        }


    }
}
