package br.com.c4ll.saml.config;

public class SamlAppConfigDto {
	private String entityId;
	
	private boolean entityIdEnabled;

	private String attributeConsumerService;
	private boolean attributeConsumerServiceEnabled;

	private String singleLogoutService;
	private boolean singleLogoutServiceEnabled;

	private String audience;
	private boolean audienceEnabled;

	private String recipient;
	private boolean recipientEnabled;

	private String destination;
	private boolean destinationEnabled;

	private String relayState;
	private boolean relayStateEnabled;

	private String nameIdFormat;
	private boolean nameIdFormatEnabled;

	private String authnRequestSigned;
	private boolean authnRequestSignedEnabled;

	private String wantAssertionsSignedSigned;
	private boolean wantAssertionsSignedSignedEnabled;

	private String x509Cert;
	private boolean x509CertEnabled;
	
	public SamlAppConfigDto() {}
	
	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public boolean isEntityIdEnabled() {
		return entityIdEnabled;
	}

	public void setEntityIdEnabled(boolean entityIdEnabled) {
		this.entityIdEnabled = entityIdEnabled;
	}

	public String getAttributeConsumerService() {
		return attributeConsumerService;
	}

	public void setAttributeConsumerService(String attributeConsumerService) {
		this.attributeConsumerService = attributeConsumerService;
	}

	public boolean isAttributeConsumerServiceEnabled() {
		return attributeConsumerServiceEnabled;
	}

	public void setAttributeConsumerServiceEnabled(boolean attributeConsumerServiceEnabled) {
		this.attributeConsumerServiceEnabled = attributeConsumerServiceEnabled;
	}

	public String getSingleLogoutService() {
		return singleLogoutService;
	}

	public void setSingleLogoutService(String singleLogoutService) {
		this.singleLogoutService = singleLogoutService;
	}

	public boolean isSingleLogoutServiceEnabled() {
		return singleLogoutServiceEnabled;
	}

	public void setSingleLogoutServiceEnabled(boolean singleLogoutServiceEnabled) {
		this.singleLogoutServiceEnabled = singleLogoutServiceEnabled;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public boolean isAudienceEnabled() {
		return audienceEnabled;
	}

	public void setAudienceEnabled(boolean audienceEnabled) {
		this.audienceEnabled = audienceEnabled;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public boolean isRecipientEnabled() {
		return recipientEnabled;
	}

	public void setRecipientEnabled(boolean recipientEnabled) {
		this.recipientEnabled = recipientEnabled;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isDestinationEnabled() {
		return destinationEnabled;
	}

	public void setDestinationEnabled(boolean destinationEnabled) {
		this.destinationEnabled = destinationEnabled;
	}

	public String getRelayState() {
		return relayState;
	}

	public void setRelayState(String relayState) {
		this.relayState = relayState;
	}

	public boolean isRelayStateEnabled() {
		return relayStateEnabled;
	}

	public void setRelayStateEnabled(boolean relayStateEnabled) {
		this.relayStateEnabled = relayStateEnabled;
	}

	public String getNameIdFormat() {
		return nameIdFormat;
	}

	public void setNameIdFormat(String nameIdFormat) {
		this.nameIdFormat = nameIdFormat;
	}

	public boolean isNameIdFormatEnabled() {
		return nameIdFormatEnabled;
	}

	public void setNameIdFormatEnabled(boolean nameIdFormatEnabled) {
		this.nameIdFormatEnabled = nameIdFormatEnabled;
	}

	public String getAuthnRequestSigned() {
		return authnRequestSigned;
	}

	public void setAuthnRequestSigned(String authnRequestSigned) {
		this.authnRequestSigned = authnRequestSigned;
	}

	public boolean isAuthnRequestSignedEnabled() {
		return authnRequestSignedEnabled;
	}

	public void setAuthnRequestSignedEnabled(boolean authnRequestSignedEnabled) {
		this.authnRequestSignedEnabled = authnRequestSignedEnabled;
	}

	public String getWantAssertionsSignedSigned() {
		return wantAssertionsSignedSigned;
	}

	public void setWantAssertionsSignedSigned(String wantAssertionsSignedSigned) {
		this.wantAssertionsSignedSigned = wantAssertionsSignedSigned;
	}

	public boolean isWantAssertionsSignedSignedEnabled() {
		return wantAssertionsSignedSignedEnabled;
	}

	public void setWantAssertionsSignedSignedEnabled(boolean wantAssertionsSignedSignedEnabled) {
		this.wantAssertionsSignedSignedEnabled = wantAssertionsSignedSignedEnabled;
	}

	public String getX509Cert() {
		return x509Cert;
	}

	public void setX509Cert(String x509Cert) {
		this.x509Cert = x509Cert;
	}

	public boolean isX509CertEnabled() {
		return x509CertEnabled;
	}

	public void setX509CertEnabled(boolean x509CertEnabled) {
		this.x509CertEnabled = x509CertEnabled;
	}

}
