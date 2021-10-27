package com.demo;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


import org.keycloak.representations.idm.UserRepresentation;

import com.demo.model.User;
import com.demo.service.UserService;

@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserExampleResource {

	@Inject
	UserService userService;

	@GET()
	@Path("/")
	public List<UserRepresentation> findAll() {
		return userService.findAll();
	}

	@GET()
	@Path("/{id}")
	public UserRepresentation findById(@PathParam("id") String id) {
		return userService.findById(id);
	}

	@POST()
	public Response create(User userRequest) {
		ResponseBuilder response = userService.create(userRequest);
		return Response.ok(userRequest).build();
	}      

	@POST()
	@Path("/signin")
	public Response signin(User userDTO) {
		return Response.ok(userService.login(userDTO)).build();
	}
}
