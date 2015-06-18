package hibernatetest;

import org.hibernate.annotations.Type;

public class SimpleEntity {

	@Type(type = "text")
	String myType;

}
