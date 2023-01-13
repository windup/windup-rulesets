import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;​
import java.lang.reflect.Constructor;​

public class CustomSmartInstantiationAwareBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {
    @Override    
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
            System.out.println("CustomSmartInstantiationAwareBeanPostProcessor.predictBeanType,beanName:"+beanName);
                    return null;    }
 
    @Override    
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
            System.out.println("CustomSmartInstantiationAwareBeanPostProcessor.determineCandidateConstructors,beanName:"+beanName);        
            return null;    }​    

            
    @Override    
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
    System.out.println("CustomSmartInstantiationAwareBeanPostProcessor.getEarlyBeanReference,beanName:"+beanName);        
    return null;    }
}​