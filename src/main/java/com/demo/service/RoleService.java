package com.demo.service;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;

import com.demo.config.KeycloakConfig;
@ApplicationScoped
public class RoleService {
	Keycloak keycloak = KeycloakConfig.getkeycloak();
	// Get realm
	RealmResource realmResource = keycloak.realm("quarkus");
	RolesResource rolesResource = realmResource.roles();

	public void create(String name) {
		RoleRepresentation role = new RoleRepresentation();
		role.setName(name);
		rolesResource.create(role);
	}

	public List<RoleRepresentation> findAll() {
		return rolesResource.list();
	}

	public RoleRepresentation findByName(String roleName) {
		return rolesResource.get(roleName).toRepresentation();
	}
	
}


