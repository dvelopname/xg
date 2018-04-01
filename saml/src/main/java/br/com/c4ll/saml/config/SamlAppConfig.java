package br.com.c4ll.saml.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

public class SamlAppConfig {
	
	@Id
	private ObjectId id;
	
	@Property
	private String name;
	
	@Property
	private String clientId;
	
	@Property
	private String issuerUrl; // https://app.onelogin.com/saml/metadata/770320 -> URL gerada automaticamente e o metadata gera automaticamente tbm
	
	@Property
	private String endpointSsoUrl; // https://abn.onelogin.com/trust/saml2/http-post/sso/770320 -> URL de Login SAML para aplicação
	
	@Property
	private String endpointSloUrl; // https://abn.onelogin.com/trust/saml2/http-redirect/slo/770320 -> Url de Logout SAML para aplicação
	
	@Embedded
	private SamlAppConfigDetail detail;
	
	@Embedded
	private SamlAppConfigParameter parameters;
	
	@Property
	private List<String> groups;
	
	public SamlAppConfig() {}
	
	public SamlAppConfig(String name) {
		this.name = name;
		
		clientId = UUID.randomUUID().toString();
		issuerUrl = "https://tenant.enterprise.com.br/idp/saml/metadata/" + clientId;
		endpointSsoUrl = "https://tenant.enterprise.com.br/idp/saml/post/sso/" + clientId;
		endpointSloUrl = "https://tenant.enterprise.com.br/idp/saml/redirect/slo/" + clientId;
		
		detail = new SamlAppConfigDetail();
		parameters = new SamlAppConfigParameter();
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("nameId", "email");
		
		parameters.setCredentials(hashMap);
		groups = Arrays.asList("1", "z");
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getIssuerUrl() {
		return issuerUrl;
	}

	public void setIssuerUrl(String issuerUrl) {
		this.issuerUrl = issuerUrl;
	}

	public String getEndpointSsoUrl() {
		return endpointSsoUrl;
	}

	public void setEndpointSsoUrl(String endpointSsoUrl) {
		this.endpointSsoUrl = endpointSsoUrl;
	}

	public String getEndpointSloUrl() {
		return endpointSloUrl;
	}

	public void setEndpointSloUrl(String endpointSloUrl) {
		this.endpointSloUrl = endpointSloUrl;
	}

	public SamlAppConfigDetail getDetail() {
		return detail;
	}

	public void setDetail(SamlAppConfigDetail detail) {
		this.detail = detail;
	}

	public SamlAppConfigParameter getParameters() {
		return parameters;
	}

	public void setParameters(SamlAppConfigParameter parameters) {
		this.parameters = parameters;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	
}
