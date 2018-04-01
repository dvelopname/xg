package br.com.c4ll.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.c4ll.saml.config.SamlAppConfig;
import br.com.c4ll.saml.config.SamlAppConfigService;

@Controller
@RequestMapping("/app/saml")
public class ApplicationController {
	
	@Autowired
	private SamlAppConfigService service;
	
	@GetMapping
	public String registerSampleParametersApp() {
		return "saml-normal-cfg";
	}
	
	@GetMapping("/{clientId}")
	public String configuration(@PathVariable String clientId, Model model) {
		SamlAppConfig config = service.get(clientId);
		
		if(config == null)
			return "saml-normal-cfg";
		
		model.addAttribute("clientId", config.getClientId());
		model.addAttribute("name", config.getName());
		model.addAttribute("issuerUrl", config.getIssuerUrl());
		model.addAttribute("endpointSsoUrl", config.getEndpointSsoUrl());
		model.addAttribute("endpointSloUrl", config.getEndpointSloUrl());
		
		model.addAttribute("entityID", config.getDetail().getEntityID());
		model.addAttribute("assertionConsumerService", config.getDetail().getAssertionConsumerServiceUrl());
		model.addAttribute("singleLogoutService", config.getDetail().getSingleLogoutServiceUrl());
		model.addAttribute("audience", config.getDetail().getAudience());
		model.addAttribute("recipient", config.getDetail().getRecipient());
		model.addAttribute("destination", config.getDetail().getDestination());
		model.addAttribute("relayState", config.getDetail().getRelayState());
		model.addAttribute("nameIdFormat", config.getDetail().getNameIdFormat());
		model.addAttribute("authnRequestSigned", config.getDetail().isAuthnRequestSigned());
		model.addAttribute("wantAssertionsSigned", config.getDetail().isWantAssertionsSigned());
		model.addAttribute("x509SpCert", config.getDetail().getX509Cert());
		
		
		return "saml-configuration";
	}
	
}
