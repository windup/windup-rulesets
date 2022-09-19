package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Client;

@Path("/hello-resteasy")
public class RestClientResource {

    private final Client client;



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        client = new Client();
        return "Hello RESTEasy";

    }



}