package entity;

import javax.ejb.EntityBean;

public abstract class MovieBean implements EntityBean {
    public MovieBean() {
    }
    public abstract java.lang.Integer getId();
    public abstract void setId(java.lang.Integer id);
}
