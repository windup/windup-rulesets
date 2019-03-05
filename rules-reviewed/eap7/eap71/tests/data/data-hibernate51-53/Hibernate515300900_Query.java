package com.jboss.windup.test;

import org.hibernate.Criteria;

public class Hibernate515300900_Query extends org.hibernate.Criteria {

  @Override
  public long getLimitXXXHandler() {
      return 10L;
  }
}