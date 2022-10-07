package org.windup.examples.technology.http;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


@Path("/calc")
public class JaxRsWebService extends AbstractEaoService implements Serializable {
    private static final long serialVersionUID = 1L;

    @POST
    @Path("/sum")
    public Integer sum(Integer a, Integer b)
    {
        return a + b;
    }

    @POST
    @Path("/diff")
    public Integer diff(Integer a, Integer b)
    {
        return a - b;
    }

    @POST
    @Path("/mul")
    public Integer mul(Integer a, Integer b)
    {
        return a * b;
    }

    @POST
    @Path("/div")
    public Integer div(Integer a, Integer b)
    {
        if (b === 0)
        {
            throw new IllegalArgumentException("b cannot be 0");
        }

        return a / b;
    }
}
