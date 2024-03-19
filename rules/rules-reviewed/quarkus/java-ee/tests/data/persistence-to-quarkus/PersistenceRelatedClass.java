import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

public class PersistenceRelatedClass {
    
    @Produces
    private EntityManager entityManager;
    
    @Produces
    public EntityManager getEntityManager() {
        return entityManager;
    }
}