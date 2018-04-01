package br.com.c4ll.saml.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class SamlAppConfigService {
	
	@Autowired
	private SamlAppConfigRepository repository;

	public String add(String clientName) {
		SamlAppConfig app = new SamlAppConfig(clientName);
		repository.add(app);
		
		return app.getClientId();
	}
	
	public SamlAppConfig get(String clientId) {
		SamlAppConfig config = repository.get(clientId);
		return config;
	}
	
	public boolean update(String clientId,
			 String entityID, boolean entityIDEnabled,
			 String assertionConsumerService, boolean assertionConsumerServiceEnabled,
			 String singleLogoutService, boolean singleLogoutServiceEnabled,
			 String audience, boolean audienceEnabled,
			 String recipient, boolean recipientEnabled,
			 String destination, boolean destinationEnabled,
			 String relayState, boolean relayStateEnabled,
			 String nameIdFormat, boolean nameIdFormatEnabled,
			 boolean authnRequestSigned,
			 boolean wantAssertionsSignedSigned,
			 String x509Cert, boolean x509CertEnabled) throws Exception {
		
		if(isNullOrEmpty(clientId))	
			return false;
		
		SamlAppConfig config = get(clientId);
		
		if(config == null) 
			return false;
		
		config.getDetail().setEntityID(getValueIfIsAbleToUpdateField(entityIDEnabled, entityID, config.getDetail().getEntityID()));
		config.getDetail().setAssertionConsumerServiceUrl(getValueIfIsAbleToUpdateField(assertionConsumerServiceEnabled, assertionConsumerService, config.getDetail().getAssertionConsumerServiceUrl()));
		config.getDetail().setSingleLogoutServiceUrl(getValueIfIsAbleToUpdateField(singleLogoutServiceEnabled, singleLogoutService, config.getDetail().getSingleLogoutServiceUrl()));
		config.getDetail().setAudience(getValueIfIsAbleToUpdateField(audienceEnabled, audience, config.getDetail().getAudience()));
		config.getDetail().setRecipient(getValueIfIsAbleToUpdateField(recipientEnabled, recipient, config.getDetail().getRecipient()));
		config.getDetail().setDestination(getValueIfIsAbleToUpdateField(destinationEnabled, destination, config.getDetail().getDestination()));
		config.getDetail().setRelayState(getValueIfIsAbleToUpdateField(relayStateEnabled, relayState, config.getDetail().getRelayState()));
		config.getDetail().setNameIdFormat(getValueIfIsAbleToUpdateField(nameIdFormatEnabled, nameIdFormat, config.getDetail().getNameIdFormat()));
		config.getDetail().setAuthnRequestSigned(authnRequestSigned);
		config.getDetail().setWantAssertionsSigned(wantAssertionsSignedSigned);
		config.getDetail().setX509Cert(getValueIfIsAbleToUpdateField(x509CertEnabled, x509Cert, config.getDetail().getX509Cert()));
		
		//config.getParameters().getCredentials().put("nameId", "username");
		
		repository.update(config);
		
		return true;
		
	}
	
	public String getValueIfIsAbleToUpdateField(boolean change, String newValue, String oldValue) throws Exception {
		if(change && !isNullOrEmpty(newValue)) {
			return newValue;
		}
		
		return oldValue;
	}
	
	public boolean isNullOrEmpty(String value) {
		return value == null || value.toString().trim() == "";
	}
	
	
}
