//package org.jboss.windup.hsearch;


import org.hibernate.search.impl.SearchMappingBuilder;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.Search;
import org.hibernate.CacheMode;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.Interceptor;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionEventListener;
import org.hibernate.SharedSessionBuilder;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.hibernate.search.Environment;
import org.hibernate.search.FullTextFilter;
import org.hibernate.search.indexes.impl.DirectoryBasedIndexManager;
import org.hibernate.search.infinispan.impl.InfinispanDirectoryProvider;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.SearchException;
import org.hibernate.search.spi.MassIndexerFactory;
import org.hibernate.search.spi.SearchFactoryBuilder;
import org.hibernate.search.spi.SearchFactoryIntegrator;
import org.hibernate.search.Version;
import org.hibernate.search.annotations.FieldCacheType;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.cfg.NumericFieldMapping;
import org.hibernate.search.cfg.PropertyDescriptor;
import org.hibernate.search.cfg.EntityDescriptor;
import org.hibernate.search.cfg.SearchMapping;
import org.hibernate.search.cfg.ContainedInMapping;
import org.hibernate.search.query.facet.FacetSortOrder;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.sql.Connection;

import org.hibernate.search.store.Workspace;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;
import org.hibernate.search.impl.FullTextSharedSessionBuilderDelegator;
import org.hibernate.search.backend.configuration.impl.IndexWriterSetting.MAX_THREAD_STATES;
import org.hibernate.search.bridge.spi.ConversionContext;
import org.hibernate.search.FullTextSharedSessionBuilder;
import org.hibernate.search.engine.spi.SearchFactoryImplementor;

import java.util.List;
import java.util.Properties;

import org.hibernate.search.cfg.IndexedMapping;
import org.hibernate.search.engine.spi.DocumentBuilderIndexedEntity;
import org.hibernate.search.query.engine.spi.HSQuery;
import org.hibernate.search.spi.BuildContext;
import org.hibernate.search.store.spi.DirectoryHelper;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.query.dsl.FuzzyContext;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserToken;
import org.apache.lucene.queryParser.QueryParserTokenMgrError;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParserToken;
import org.apache.lucene.queryParser.QueryParserTokenMgrError;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.KeywordMarkerFilter;
import org.apache.lucene.analysis.TeeSinkTokenFilter;
import org.apache.lucene.analysis.CharFilter;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.util.CharacterUtils;
import org.apache.lucene.search.function.CustomScoreQuery;
import org.apache.lucene.search.function.NumericIndexDocValueSource;
import org.apache.lucene.analysis.ReusableAnalyzerBase;
import org.apache.lucene.search.function.ValueSource;
import org.apache.lucene.search.function.ByteFieldSource;
import org.apache.solr.analysis.TokenizerFactory;

import org.hibernate.search.query.dsl.QueryBuilder;

public class HsearchUtil {

    public void main(String[] args) {
        
        FullTextSession fullTextSession = Search.getFullTextSession( s );
        SearchFactoryImplementor searchFactory = (SearchFactoryImplementor) fullTextSession.getSearchFactory();
        
        final QueryBuilder b = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity( Book.class ).get();
        
        b.facet();
        
        
        org.apache.lucene.search.Query luceneQuery =
                    b.keyword()
                        .onField("isbn").boostedTo(3)
                        .matching("12456797")
                        .createQuery();
        
        b.keyword().fuzzy().withThreshold(0.7f);
        
        FuzzyContext fuzzyContext = b. keyword().fuzzy();
        fuzzyContext.withThreshold(0.7f);
        
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery( luceneQuery );;
        fulltextQuery.setSort(new Sort(new SortField("title", SortField.STRING)));
        List result = fullTextQuery.list();
        
        DirectoryHelper.getVerifiedIndexDir("indexname",new Properties(), true);

        fullTextSession
        .createIndexer( Hotel.class )
        .batchSizeToLoadObjects( 25 )
        .cacheMode( CacheMode.NORMAL )
        .threadsToLoadObjects( 5 )
        .threadsForSubsequentFetching( 20 )
        .startAndWait();
        
        HSQuery query = queryContext.getFactory().createHSQuery();
        ExtendedSearchIntegrator searchIntegrator = query.getExtendedSearchIntegrator();
        // hsearch-00218
        BuildContext context = (BuildContext) searchIntegrator;
        context.getIndexingStrategy();
        
    }
    
    private void callConstructors(){
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);
        
        PropertyDescriptor property = new PropertyDescriptor("bean", ElementType.FIELD);
        EntityDescriptor entityDescriptor = new EntityDescriptor();
        entityDescriptor.setCacheInMemory(new HashMap());
        entityDescriptor.getCacheInMemory();

        NumericFieldMapping numMapping = new NumericFieldMapping("foo", property, entityDescriptor, new SearchMapping());

        SearchMapping searchmapping =  new SearchMapping();
        indexMapping indexMapping = new IndexedMapping(searchmapping, entityDescriptor);
        indexMapping.cacheFromIndex(FieldCacheType.CLASS);

        ContainedInMapping containedMapping = new ContainedInMapping(property, new EntityDescriptor(),searchMapping);
        numMapping = containedMapping.numericField();

        //second test for splitting into multiple rows
        searchMapping.entity(Book.class)
        .indexed()
        .property("name", ElementType.FIELD)
        .containedIn()
        .numericField();
        
        FullTextSharedSessionBuilder builder = new FullTextSharedSessionBuilder()
        {
            
            @Override
            public SessionBuilder statementInspector(StatementInspector arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public SessionBuilder eventListeners(SessionEventListener... arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public SessionBuilder clearEventListeners()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder transactionContext()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder tenantIdentifier(String arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSession openSession()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder noInterceptor()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder interceptor(Interceptor arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder interceptor()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder flushBeforeCompletion(boolean arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder flushBeforeCompletion()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder connectionReleaseMode(ConnectionReleaseMode arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder connectionReleaseMode()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder connection(Connection arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder connection()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder autoJoinTransactions(boolean arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder autoJoinTransactions()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder autoClose(boolean arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public FullTextSharedSessionBuilder autoClose()
            {
                // TODO Auto-generated method stub
                return null;
            }
        };
        builder.autoClose();
    }
    
    public QueryParser getQuery() throws  ParseException {
        
        Analyzer analyzer = new SimpleAnalyzer();
        QueryParser parser = new QueryParser(org.apache.lucene.util.Version.LUCENE_4_0, "title", analyzer);
        
        String querystr = "test*";
        Query query = parser.parse(querystr);
    }

    private String objectIdInString(Class<?> entityClass, Serializable id, ConversionContext conversionContext) {
        EntityIndexBinder indexBindingForEntity = searchFactory.getIndexBindingForEntity( entityClass );
        if ( indexBindingForEntity == null ) {
         throw new org.hibernate.search.exception.SearchException( "Unable to find entity type metadata while deserializing: " + entityClass );
        }
        DocumentBuilderIndexedEntity documentBuilder = indexBindingForEntity.getDocumentBuilder();  
        documentBuilder.getFieldCacheOption();
        return documentBuilder.objectToString( documentBuilder.getIdKeywordName(), id, conversionContext );
       }
}
