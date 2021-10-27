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

import org.keycloak.representations.idm.RoleRepresentation;

import com.demo.service.RoleService;

@Path("/api/role")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleExampleResource {
	@Inject
	RoleService roleService;

	@GET()
	@Path("/")
	public List<RoleRepresentation> findAll() {
		return roleService.findAll();
	}

	@POST()
	@Path("/{name}")
	public Response createRole(@PathParam("name") String name) {
		roleService.create(name);
		return Response.ok("Role Added Succesfully!!" + "" + name).build();
	}
}
