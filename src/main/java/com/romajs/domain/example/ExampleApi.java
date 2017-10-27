package com.romajs.domain.example;

import com.romajs.soa.ServiceFactory;
import com.romajs.soa.Transformers;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/example")
public class ExampleApi {

	{
		Transformers.register(ExampleRequest.class, Example.class, request -> new Example(request.getName()));
		Transformers.register(Example.class, ExampleResponse.class, example -> {
			ExampleResponse response = new ExampleResponse();
			response.setId(example.getId());
			response.setName(example.getName());
			return response;
		});
	}

	@GET
	@Path("/{id}")
	public ExampleResponse get(@PathParam("id") Long id) {
		Example example = ServiceFactory.getInstance(ExampleService.class).get(id);
		return Transformers.transform(example, ExampleResponse.class);
	}

	@POST
	public Long post(ExampleRequest request) {
		Example example = Transformers.transform(request, Example.class);
		return ServiceFactory.getInstance(ExampleService.class).post(example);
	}

	@PUT
	@Path("/{id}")
	public void put(@PathParam("id") Long id, ExampleRequest request) {
		Example example = Transformers.transform(request, Example.class);
		ServiceFactory.getInstance(ExampleService.class).put(id, example);
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") Long id) {
		ServiceFactory.getInstance(ExampleService.class).delete(id);
	}
}
