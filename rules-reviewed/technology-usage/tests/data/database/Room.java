
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQuery(name="Room.findAll", query="SELECT r FROM Room r")
@Entity
@Table(name = "Room")
public class Room implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Integer id;

    @Column(name = "number")
    private String number; // immutable

    @Column(name = "capacity")
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id")
    private Integer building; // immutable

    Room()
    {
        // default constructor
    }

    public Room(Integer building, String number)
    {
        // constructor with required field
        notNull(building, "Method called with null parameter (application)");
        notNull(number, "Method called with null parameter (name)");

        this.building = building;
        this.number = number;
    }

    @Override
    public boolean equals(final Object otherObj)
    {
        if ((otherObj == null) || !(otherObj instanceof Room))
        {
            return false;
        }
        // a room can be uniquely identified by it's number and the building it belongs to; normally I would use a UUID in any case but this is just
        // to illustrate the usage of getId()
        final Room other = (Room) otherObj;
        return new EqualsBuilder().append(getNumber(), other.getNumber())
                    .append(getBuilding().getId(), other.getBuilding().getId())
                    .isEquals();
        // this assumes that Building.id is annotated with @Access(value = AccessType.PROPERTY)
    }

    public Integer getBuilding()
    {
        return building;
    }

    public Integer getId()
    {
        return id;
    }

    public String getNumber()
    {
        return number;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getNumber()).append(getBuilding().getId()).toHashCode();
    }

    public void setCapacity(Integer capacity)
    {
        this.capacity = capacity;
    }

    // no setters for number, building nor id

}
