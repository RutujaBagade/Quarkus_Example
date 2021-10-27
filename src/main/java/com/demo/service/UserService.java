package com.demo.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.AuthzClient;
import com.demo.config.KeycloakConfig;
import com.demo.model.User;

@ApplicationScoped
public class UserService {
//	@ConfigProperty(name = "keycloak.realm")
//	private static String REALM;
//
//	@ConfigProperty(name = "keycloak.auth-server-url")
//	private static String SERVER_URL;
//
//	@ConfigProperty(name = "keycloak.credentials.clientid")
//	private static String CLIENT_ID;
//
//	@ConfigProperty(name = "keycloak.credentials.secret")
//	private static String CLIENT_SECRET;
	private static String SERVER_URL = "http://localhost:8080/auth";
	private String REALM = "quarkus";
	private String CLIENT_ID = "quarkus-client";
	private String CLIENT_SECRET = "6d6a029e-41ca-4f3e-8ac2-f46e38134af8";

	Keycloak keycloak = KeycloakConfig.getkeycloak();
	RealmResource realmResource = keycloak.realm("quarkus");
	UsersResource usersRessource = realmResource.users();

	// creating user
	public ResponseBuilder create(User userDTO) {

		UserRepresentation user = prepareUserRepresentation(userDTO);

		Response response = usersRessource.create(user);
		if (response.getStatus() == 201) {
			String userId = CreatedResponseUtil.getCreatedId(response);
			CredentialRepresentation password = preparePasswordRepresentation(userDTO.getPassword());
			UserResource userResource = usersRessource.get(userId);
			// Set password credential
			userResource.resetPassword(password);
			// Get realm role
			RoleRepresentation realmRoleUser = realmResource.roles().get(userDTO.getRole()).toRepresentation();
			// Assign realm role student to user
			userResource.roles().realmLevel().add(Arrays.asList(realmRoleUser));
		}
		return Response.ok(response);

	}

	// show all users from keycloack
	public List<UserRepresentation> findAll() {
		return usersRessource.list();
	}

	// based on userId get user
	public UserRepresentation findById(String id) {
		return usersRessource.get(id).toRepresentation();
	}

//	sign in purpose--generate token
	public AccessTokenResponse login(User userDTO) {
		System.out.println("REALM" + REALM);
		System.out.println("SERVER_URL" + SERVER_URL);
		System.out.println("CLIENT_ID" + CLIENT_ID);
		System.out.println("CLIENT_SECRET" + CLIENT_SECRET);
		Map<String, Object> clientCredentials = new HashMap<>();
		clientCredentials.put("secret", CLIENT_SECRET);
		clientCredentials.put("grant_type", "password");
		Configuration configuration = new Configuration(SERVER_URL, REALM, CLIENT_ID, clientCredentials, null);
		AuthzClient authzClient = AuthzClient.create(configuration);
		AccessTokenResponse response = authzClient.obtainAccessToken(userDTO.getEmail(), userDTO.getPassword());
		return response;
	}

//assign roles
	public void assignRole(String userId, RoleRepresentation roleRepresentation) {
		usersRessource.get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation));
	}

	private UserRepresentation prepareUserRepresentation(User userDTO) {
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername(userDTO.getEmail());
		user.setFirstName(userDTO.getFirstname());
		user.setLastName(userDTO.getLastname());
		user.setEmail(userDTO.getEmail());
		return user;
	}

	private CredentialRepresentation preparePasswordRepresentation(String password) {
		{
			CredentialRepresentation passwordCredentials = new CredentialRepresentation();
			passwordCredentials.setTemporary(false);
			passwordCredentials.setType(CredentialRepresentation.PASSWORD);
			passwordCredentials.setValue(password);
			return passwordCredentials;
		}
	}
}
