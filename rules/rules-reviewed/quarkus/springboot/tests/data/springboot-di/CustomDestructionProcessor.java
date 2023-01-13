import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

public class CustomDestructionProcessor implements DestructionAwareBeanPostProcessor {

    public CustomDestructionProcessor() {
        log.info("DestructionAwareBeanPostProcessor In other bean Create before create");
    }

    /** The other bean is destroyed before it is destroyed. */
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (bean instanceof SimpleBean) {
            log.info("{} About to be destroyed", beanName);
        }
    }

}