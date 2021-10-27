package com.demo.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

@Singleton
public class KeycloakConfig {

//	@ConfigProperty(name = "keycloak.auth-server-url")
//	private static  String SERVER_URL;
	private static String SERVER_URL = "http://localhost:8080/auth";
	

	public static Keycloak getkeycloak() {
		
		System.out.println("SERVER_URL" + SERVER_URL);
		
		Keycloak keycloak = KeycloakBuilder.builder().serverUrl(SERVER_URL).grantType(OAuth2Constants.PASSWORD)
				.realm("master").clientId("admin-cli").username("admin").password("admin")
				.resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build()).build();

		keycloak.tokenManager().getAccessToken();
		return keycloak;
	}
}
