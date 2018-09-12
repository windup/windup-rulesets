package org.jboss.windup.rules.tests;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.runner.Description;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class ParameterizedArquillianRunner extends Arquillian {

    private final Collection<Object> parameters;

    private final String name;
    
    public ParameterizedArquillianRunner(Class<?> testClass) throws InitializationError, InvocationTargetException, IllegalAccessException {
        super(testClass);
        Collection<Object> result = null;
        for (Method method : testClass.getMethods()) {
            if (method.isAnnotationPresent(Parameterized.Parameters.class) &&
                    Modifier.isStatic(method.getModifiers()) &&
                    Modifier.isPublic(method.getModifiers()))
            {
                 result =  (Collection<Object>)method.invoke(testClass, false);
                 break;
            }
        }
        if (result != null)
        {
            parameters = result;
        } else
        {
            parameters = new ArrayList<>();
        }
        name = testClass.getName();
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        // scan test class for methods annotated with @Test
        List<FrameworkMethod> superMethods = super.getChildren();
        List<FrameworkMethod> toReturn = new ArrayList<>(parameters.size() * superMethods.size());
        superMethods.forEach(frameworkMethod -> parameters.forEach(o -> toReturn.add(new WindupFrameworkMethod(frameworkMethod.getMethod(), ((File[])o)[0].toString()))));
        return toReturn;
    }

    @Override
    protected Description describeChild(FrameworkMethod method) {
        // create Description based on method name
        if (method instanceof WindupFrameworkMethod)
        {
            return Description.createTestDescription(getTestClass().getJavaClass(),
                    method.getName(), method.getAnnotations());
        } else
        {
            return super.describeChild(method);
        }
    }
}
