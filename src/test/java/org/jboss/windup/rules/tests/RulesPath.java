package org.jboss.windup.rules.tests;

import javax.inject.Singleton;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

@Singleton
public class RulesPath {

    private Iterator<File[]> iterator;

    public void setRules(Collection<File[]> rules)
    {
        if (this.iterator == null)
        {
            this.iterator = rules.iterator();
        }
    }

    public File[] getNextRule()
    {
        if (iterator.hasNext())
        {
            return iterator.next();
        }
        return null;
    }
}
