import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.SpatialMode;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;

import javax.persistence.Entity;

@Spatial(spatialMode = SpatialMode.GRID)
@Indexed
@Entity
public class Hotel
{

    @Latitude
    Double latitude;

    @Longitude
    Double longitude;
    
    //try it splitted on more lines
    int
    text;
    
    
}