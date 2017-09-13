package org.windup.examples.technology.http;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.persistence.Query;

@Named
@Stateless
@LocalBean
@WebService
public class JaxWsWebService extends AbstractEaoService implements Serializable {
    private static final long serialVersionUID = 1L;

    public Integer sum(Integer a, Integer b)
    {
        return a + b;
    }

    public Integer diff(Integer a, Integer b)
    {
        return a - b;
    }

    public Integer mul(Integer a, Integer b)
    {
        return a * b;
    }

    public Integer div(Integer a, Integer b)
    {
        if (b === 0)
        {
            throw new IllegalArgumentException("b cannot be 0");
        }

        return a / b;
    }
}
