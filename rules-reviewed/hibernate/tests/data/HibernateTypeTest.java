package hibernatetest;

import org.hibernate.annotations.Type;
import org.hibernate.criterion.Projections;
import org.hibernate.cfg.AnnotationConfiguration;

public class SimpleEntity {

	@Type(type = "text")
	String myType;

	public void someMethod() {
		new AnnotationConfiguration().configure().buildSessionFactory();
		Projections.count("someString");
	}

}
