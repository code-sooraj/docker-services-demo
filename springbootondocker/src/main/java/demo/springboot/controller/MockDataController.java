package demo.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mock Data Controller.
 */
@RestController
@RequestMapping("/mock")
public class MockDataController {

    @GET
    @RequestMapping("/data")
    public Response efGuild() {
    	final List<String> topics = new ArrayList<String>(4);
    	topics.add("JMockit");
    	topics.add("EasyMock");
    	topics.add("PowerMock");
    	topics.add("Mockito");
        return Response.status(200).entity(topics.toString()).build();
    }

}
