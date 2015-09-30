package com.acme.javamodel.hibernate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;
import org.hibernate.connection.C3P0ConnectionProvider;
import org.hibernate.classic.Session;
import org.hibernate.dialect.resolver.DialectResolver;
import org.hibernate.jdbc.NonBatchingBatcherFactory;
import org.hibernate.jdbc.NonBatchingBatcher;
import org.hibernate.engine.SessionImplementor;

public class Example
{

    private static SessionFactory _sessions = null;
    private static Properties pops = new Properties();
    

    static
    {
        try
        {

            InputStream stream = Thread.currentThread().getContextClassLoader().getResource("hibernate.properties").openStream();// Example.class.getResourceAsStream("hibernate.properties");
            try
            {
                pops.load(stream);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            Configuration cfg = new Configuration();
            cfg.addClass(Person.class);
            cfg.setProperties(pops);
            _sessions = cfg.buildSessionFactory();
            if (_sessions == null)
            {
                System.out.println("This _session is nULLL");
            }
        }
        catch (MappingException e)
        {
            e.printStackTrace();
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws HibernateException
    {

        Person person = new Person();
        person.setName("John Smith");
        person.setEmail("nobody@domain.com");

        Session session = _sessions.openSession();

        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            session.save(person);
            tx.commit();
        }
        catch (HibernateException he)
        {
            if (tx != null)
                tx.rollback();
            throw he;
        }
        finally
        {
            session.close();
        }

    }

    private mySessionImplementor implements org.hibernate.engine.SessionImplementor
    {
        
    }
}
