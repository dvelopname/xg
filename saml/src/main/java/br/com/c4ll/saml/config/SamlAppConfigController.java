package br.com.c4ll.saml.config;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/idp/saml/config")
public class SamlAppConfigController {
	
	@Autowired
	private SamlAppConfigService service;
	
	@PostMapping
	public void add(@RequestParam String name, HttpServletResponse response) throws IOException {
		String clientId = service.add(name);
		
		response.sendRedirect("/app/saml/" + clientId);
	}
	
	@PostMapping("/advanced")
	public void update(
			@RequestParam String clientId,
			@RequestParam String entityID, @RequestParam boolean entityIDEnabled,
			@RequestParam String assertionConsumerService, @RequestParam boolean assertionConsumerServiceEnabled,
			
			@RequestParam(value="singleLogoutService", required=false) String singleLogoutService, @RequestParam(value="singleLogoutServiceEnabled", required=false) boolean singleLogoutServiceEnabled,
			@RequestParam(value="audience", required=false) String audience, @RequestParam(value="audienceEnabled", required=false) boolean audienceEnabled,
			@RequestParam(value="recipient", required=false) String recipient, @RequestParam(value="recipientEnabled", required=false) boolean recipientEnabled,
			@RequestParam(value="destination", required=false) String destination, @RequestParam(value="destinationEnabled", required=false) boolean destinationEnabled,
			@RequestParam(value="relayState", required=false) String relayState, @RequestParam(value="relayStateEnabled", required=false) boolean relayStateEnabled,
			@RequestParam(value="nameIdFormat", required=false) String nameIdFormat, @RequestParam(value="nameIdFormatEnabled", required=false) boolean nameIdFormatEnabled,
			@RequestParam(value="authnRequestSigned", required=false) boolean authnRequestSigned,
			@RequestParam(value="wantAssertionsSignedSigned", required=false) boolean wantAssertionsSignedSigned,
			@RequestParam(value="x509SpCert", required=false) String x509SpCert, @RequestParam(value="x509SpCertEnabled", required=false) boolean x509SpCertEnabled,
			HttpServletResponse response) throws IOException {
		
		try {
			service.update(clientId, entityID, entityIDEnabled, assertionConsumerService, assertionConsumerServiceEnabled,
					singleLogoutService, singleLogoutServiceEnabled, audience, audienceEnabled, recipient, recipientEnabled,
					destination, destinationEnabled, relayState, relayStateEnabled, nameIdFormat, nameIdFormatEnabled,
					authnRequestSigned, wantAssertionsSignedSigned, x509SpCert, x509SpCertEnabled);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
