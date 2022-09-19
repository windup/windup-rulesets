
import javax.persistence.JoinColumn;

/**
 *  Sample class for tests.
 */
public class SampleJoinColumns
{
    @JoinColumn("fkSomeEntity")
    private Object someEntity;
}
