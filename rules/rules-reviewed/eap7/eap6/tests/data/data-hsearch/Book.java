import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.CalendarBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.EncodingType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import org.hibernate.search.annotations.FieldCacheType;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.CacheFromIndex;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.NumericFields;

@Entity
@AnalyzerDef(name = "customanalyzer", tokenizer = @TokenizerDef(factory = org.apache.lucene.analysis.standard.StandardTokenizerFactory.class), filters = {
            @TokenFilterDef(factory = org.apache.lucene.analysis.core.LowerCaseFilterFactory.class),
            @TokenFilterDef(factory = org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "English")
            })
})
@Indexed
@CacheFromIndex( {FieldCacheType.CLASS, FieldCacheType.ID} )
public class Book
{

    private Integer id;
    private String title;
    private String subtitle;
    private Set<Author> authors = new HashSet<Author>();
    private Date publicationDate;
    private Integer isbn;
    private double price;
    
    /**
     * @return the price
     */
    @Field
    public double getPrice()
    {
        return price;
    }

    /**
     * @param price the price to set
     */
    
    public void setPrice(double price)
    {
        this.price = price;
    }

    @NumericFields({
        @NumericField(forField="isbn")
      })
    @Field(store = Store.YES, index = Index.YES, indexNullAs = "-1")
    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer number){
        this.isbn = number;
    }
    
    @IndexedEmbedded
    @ManyToMany
    public Set<Author> getAuthors()
    {
        return authors;
    }

    public void setAuthors(Set<Author> authors)
    {
        this.authors = authors;
    }

    public Book()
    {
    }

    @Field(store = Store.YES)
    @Analyzer(definition = "customanalyzer")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Id
    @DocumentId
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public void setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
    }

    @Field(analyze = Analyze.NO, store = Store.YES)    
    public Date getPublicationDate()
    {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate)
    {
        this.publicationDate = publicationDate;
    }
    
    @CalendarBridge(resolution = Resolution.DAY)
    public Calendar getCalendarPublication() 
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.publicationDate);
        return cal;
    }

    @CalendarBridge(resolution = Resolution.DAY, encoding = EncodingType.NUMERIC)
    public Calendar getEncodedCalendarPublication()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.publicationDate);
        return cal;
    }

    @DateBridge(encoding = EncodingType.STRING, resolution = Resolution.MILLISECOND)
    public Calendar getEncodedDatePublication()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.publicationDate);
        return cal;
    }

    @DateBridge(resolution = Resolution.MILLISECOND)
    public Calendar getDatePublication()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.publicationDate);
        return cal;
    }
}

class Author {

}
