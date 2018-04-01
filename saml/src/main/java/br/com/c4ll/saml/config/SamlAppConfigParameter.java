package br.com.c4ll.saml.config;

import java.util.Map;

import org.mongodb.morphia.annotations.Property;

public class SamlAppConfigParameter {
	
	/**
	 * Esses parâmetros serão utilizados quando os usuários logarem (e serão preenchidos na SAML response) para enviar ao SP
	 * O parâmetro 0 é o NameID
	 * 
	 * (Attribute, Value)
	 */
	
	@Property
	private Map<String, String> credentials;

	public Map<String, String> getCredentials() {
		return credentials;
	}

	public void setCredentials(Map<String, String> credentials) {
		this.credentials = credentials;
	}

}
