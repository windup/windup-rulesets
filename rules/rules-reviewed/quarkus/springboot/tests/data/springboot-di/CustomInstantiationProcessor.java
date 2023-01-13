import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

public class CustomInstantiationProcessor implements InstantiationAwareBeanPostProcessor {

    public CustomInstantiationProcessor() {
        log.info("InstantiationAwareBeanPostProcessor In other bean Create before create");
    }

    /** Call before other bean instantiation */
    @Override
    public Object postProcessBeforeInstantiation(Class
  
   beanClass, String beanName) throws BeansException {
        if (beanClass.equals(SimpleBean.class)) {
            log.info("{} About to instantiate", beanName);
        }
        return null;
    }

    /** After the other bean instantiation is called */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (bean instanceof SimpleBean) {
            log.info("{} Instantiation complete", beanName);
        }
        return true;
    }

}