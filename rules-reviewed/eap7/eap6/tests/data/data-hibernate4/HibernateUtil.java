

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import java.sql.Connection; 
import java.sql.DatabaseMetaData; 
import java.sql.SQLException; 
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
import org.hibernate.boot.Metadata; 
import org.hibernate.boot.spi.MetadataImplementor; 
import org.hibernate.cfg.Configuration; 
import org.hibernate.engine.jdbc.spi.JdbcServices; 
import org.hibernate.engine.spi.SessionFactoryImplementor; 
import org.hibernate.integrator.spi.Integrator; 
import org.hibernate.service.spi.SessionFactoryServiceRegistry; 
import org.hibernate.usertype.CompositeUserType; 
import org.hibernate.usertype.UserType; 

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
    
    private boolean use42Api(String jdbc42Apis, SessionFactoryImplementor sessionFactory) {

        boolean use42Api;
           if (jdbc42Apis == null) {

               if (JavaVersion.getMajorVersion() >= 1 && JavaVersion.getMinorVersion() >= 8) {
                
                Connection conn = null;
                try {
                       JdbcServices jdbcServices = sessionFactory.getServiceRegistry().getService(JdbcServices.class);
                       conn = jdbcServices.getBootstrapJdbcConnectionAccess().obtainConnection();
                       
                       DatabaseMetaData dmd = conn.getMetaData();
                       int driverMajorVersion = dmd.getDriverMajorVersion();
                       int driverMinorVersion = dmd.getDriverMinorVersion();
                       
                       if (driverMajorVersion >= 5) {
                           use42Api = true;
                       } else if (driverMajorVersion >= 4 && driverMinorVersion >= 2) {
                           use42Api = true;
                       } else {
                           use42Api = false;
                       }
                   } catch (SQLException e) {
                       use42Api = false;
                   } catch (NoSuchMethodError e) {
                     // Occurs in Hibernate 4.2.12
                       use42Api = false;
                   } finally {
                       
                       if (conn != null) {
                           try {
                               conn.close();
                           } catch (SQLException e) {
                               // Ignore
                           }
                       }
                   }
               } else {
                   use42Api = false;
               }
           } else {
               use42Api = Boolean.parseBoolean(jdbc42Apis);
           }
           return use42Api;
       }

}
