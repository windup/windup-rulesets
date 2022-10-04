import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FOO_PERSON")
public class FooPerson {
    private int id;
    private String name;
    private byte[] photo;
    private Byte[] foo;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "PERSON_ID")
    @GeneratedValue
    public int getId() {
        return id;
    }

    public Byte[] getFoo() {
        return foo;
    }

    public void setFoo(Byte[] foo) {
        this.foo = foo;
    }

}