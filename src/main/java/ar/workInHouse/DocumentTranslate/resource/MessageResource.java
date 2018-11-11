package ar.workInHouse.DocumentTranslate.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ar.workInHouse.DocumentTranslate.model.Message;

// imports...
 
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {
    private static final String HELLO_WORLD_SENTENCE = "Hello world %s!";
    private static final String DEFAULT_NAME = "Nobody";
 
    @GET
    public Message getHelloWorldMessage(@QueryParam("name") String name) {
        return new Message(
                String.format(HELLO_WORLD_SENTENCE, name != null ? name : DEFAULT_NAME)
        );
    }
}