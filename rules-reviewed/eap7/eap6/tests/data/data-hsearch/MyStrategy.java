package org.jboss.windup.hsearch;


import java.io.Serializable;
import java.util.Properties;

import org.apache.lucene.document.Document;
import org.hibernate.search.filter.FullTextFilterImplementor;
import org.hibernate.search.indexes.spi.IndexManager;
import org.hibernate.search.store.IndexShardingStrategy;


public class MyStrategy implements IndexShardingStrategy {

    @Override
    public IndexManager getIndexManagerForAddition(Class<?> arg0, Serializable arg1, String arg2, Document arg3)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IndexManager[] getIndexManagersForAllShards()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IndexManager[] getIndexManagersForDeletion(Class<?> arg0, Serializable arg1, String arg2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IndexManager[] getIndexManagersForQuery(FullTextFilterImplementor[] arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void initialize(Properties arg0, IndexManager[] arg1)
    {
        // TODO Auto-generated method stub
        
    }
    
    
}

