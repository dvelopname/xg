package br.com.c4ll.saml.config;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class SamlAppConfigDetail {
	
	/**
	 * Parâmetros que serão utilizados para criar a SAMLResponse também
	 */
	
	@Property
	private String entityID;
	
	@Property
	private String assertionConsumerServiceUrl;
	
	@Property
	private String singleLogoutServiceUrl;
	
	@Property
	private String audience;
	
	@Property
	private String recipient;
	
	@Property
	private String destination;
	
	@Property
	private String relayState;
	
	@Property
	private String nameIdFormat;
	
	@Property
	private boolean authnRequestSigned;
	
	@Property
	private boolean wantAssertionsSigned;
	
	@Property
	private String x509Cert;

	public SamlAppConfigDetail() {}
	
	public String getEntityID() {
		return entityID;
	}

	public void setEntityID(String entityID) {
		this.entityID = entityID;
	}

	public String getAssertionConsumerServiceUrl() {
		return assertionConsumerServiceUrl;
	}

	public void setAssertionConsumerServiceUrl(String assertionConsumerServiceUrl) {
		this.assertionConsumerServiceUrl = assertionConsumerServiceUrl;
	}

	public String getSingleLogoutServiceUrl() {
		return singleLogoutServiceUrl;
	}

	public void setSingleLogoutServiceUrl(String singleLogoutServiceUrl) {
		this.singleLogoutServiceUrl = singleLogoutServiceUrl;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getRelayState() {
		return relayState;
	}

	public void setRelayState(String relayState) {
		this.relayState = relayState;
	}

	public String getNameIdFormat() {
		return nameIdFormat;
	}

	public void setNameIdFormat(String nameIdFormat) {
		this.nameIdFormat = nameIdFormat;
	}

	public boolean isAuthnRequestSigned() {
		return authnRequestSigned;
	}

	public void setAuthnRequestSigned(boolean authnRequestSigned) {
		this.authnRequestSigned = authnRequestSigned;
	}

	public boolean isWantAssertionsSigned() {
		return wantAssertionsSigned;
	}

	public void setWantAssertionsSigned(boolean wantAssertionsSigned) {
		this.wantAssertionsSigned = wantAssertionsSigned;
	}

	public String getX509Cert() {
		return x509Cert;
	}

	public void setX509Cert(String x509Cert) {
		this.x509Cert = x509Cert;
	}
	
	
}
