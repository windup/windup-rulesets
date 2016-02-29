import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import org.hibernate.id.TableGenerator;
import org.hibernate.id.SequenceHiLoGenerator;

import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.type.BasicType;
import org.hibernate.type.CurrencyType;
import org.hibernate.usertype.UserType;
import org.hibernate.metamodel.spi.TypeContributor;
import org.hibernate.metamodel.spi.TypeContributions;

public class HibernateUtil
{

    private static SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory buildSessionFactory()
    {
        try
        {

              Configuration configuration = new Configuration();
              SessionFactory sessionFactory = configuration.configure("hibernate.cfg.xml").addResource("domain.hbm.xml").buildSessionFactory();

            return sessionFactory;

        }
        catch (Throwable ex)
        {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory buildSessionFactoryFromAnnotations()
    {
        try
        {
            sessionFactory = new AnnotationConfiguration()
                        .setNamingStrategy(ImprovedNamingStrategy.INSTANCE)
                        .configure("/hib.cfg.xml")
                        .buildSessionFactory();
            return sessionFactory;
        }
        catch (Throwable ex)
        {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private void addMapping(String xml) {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            configuration.add(xml);
            configuration.setImplicitNamingStrategy();
            configuration.addXML(xml);
            configuration.addCacheableFile("");
            configuration.addURL( new URL(""));
            configuration.addFile(new File("somefile"));
            configuration.addInputStream( new InputStream()
            {
                
                @Override
                public int read() throws IOException
                {
                    // TODO Auto-generated method stub
                    return 0;
                }
            });
            configuration.addResource( "");
            configuration.addClass(ClassTest.class);
            configuration.addAnnotatedClass( UserEntity.class);
            configuration.addPackage("com.example.domain");
            configuration.addJar(new File("jar"));
            configuration.addDirectory( new File("path_to_dir"));
            
            CustomUserType customUserType = new CustomUserType();
            configuration.registerTypeOverride((UserType) customUserType,
                    new String[] { customUserType.returnedClass().getName() });
            configuration.registerTypeContributor(customUserType);
            configuration.registerTypeOverride();

            Properties properties = new Properties();
            properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect");
            properties.put(AvailableSettings.DRIVER, "com.mysql.jdbc.Driver");
            properties.put(AvailableSettings.URL, "jdbc:mysql://127.0.0.1:3306/test");
            properties.put(AvailableSettings.USER, "root");
            properties.put(AvailableSettings.PASS, "");

            configuration.setProperties(properties);
            configuration.addProperties(properties);
            
            configuration.setInterceptor(new MyInterceptor ());
            configuration.setEntityNotFoundDelegate(new MyEntityNotFoundDelegate());
            configuration.setPhysicalNamingStrategy(new UpperCaseNamingStrategy());
            configuration.setSessionFactoryObserver(new SessionFactoryObserver() {
                private static final long serialVersionUID = 1L;

                @Override
                public void sessionFactoryCreated(SessionFactory factory) {
                }

                @Override
                public void sessionFactoryClosed(SessionFactory factory) {
                    
                }
            });
            configuration.setCurrentTenantIdentifierResolver((CurrentTenantIdentifierResolver) new CurrencyType());
            
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public static void shutdown()
    {
        getSessionFactory().close();
    }

}
