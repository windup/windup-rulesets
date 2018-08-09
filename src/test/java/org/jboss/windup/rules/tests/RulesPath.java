package org.jboss.windup.rules.tests;

import javax.inject.Singleton;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

@Singleton
public class RulesPath {

    private Iterator<File[]> iterator;
    Collection<File[]> rules;

    public void setRules(Collection<File[]> rules)
    {
        if (this.iterator == null)
        {
            this.iterator = rules.iterator();
            this.rules = rules;
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

    public boolean hasNextRule()
    {
        return iterator.hasNext();
    }

    public void resetIterator()
    {
        this.iterator = rules.iterator();
    }
}
