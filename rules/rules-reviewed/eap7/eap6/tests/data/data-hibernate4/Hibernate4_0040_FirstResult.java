package com.jboss.windup.test;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

public class Hibernate515300900_FirstResult {

    public void listUserByPage(int pageNo, int pageSize) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria();
        long recordTotal = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).longValue();
        criteria.setProjection(null);
        criteria.addOrder(Order.desc("registerDate"));

        criteria.setFirstResult(10);

        criteria.setMaxResults(pageSize);
    }

}