package br.com.c4ll.saml;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.utils.URLEncodedUtils;
import org.opensaml.common.SAMLException;
import org.opensaml.core.config.InitializationException;
import org.opensaml.messaging.decoder.MessageDecodingException;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.impl.AuthnRequestMarshaller;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xmlsec.signature.impl.SignatureBuilder;
import org.opensaml.xmlsec.signature.impl.SignatureImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import br.com.c4ll.HttpUtils;
import br.com.c4ll.file.TemporaryFileUtils;
import br.com.c4ll.saml.config.SamlAppConfig;
import br.com.c4ll.saml.config.SamlAppConfigDetail;
import br.com.c4ll.saml.config.SamlAppConfigService;
import br.com.c4ll.saml.decoder.SamlDecoder;
import br.com.c4ll.saml.encode.SamlEncoder;
import br.com.c4ll.saml.validate.SamlValidate;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

@Controller
@RequestMapping("/idp/saml")
public class SamlController {
	
	@Autowired
	private SamlEncoder encoder;
	
	@Autowired
	private SamlDecoder decoder;
	
	@Autowired
	private SamlAppConfigService service;
	
	@Autowired
	private TemporaryFileUtils temporary;
	
	@Autowired
	private SamlValidate validate;
	
	@Autowired
	private HttpUtils httpUtils;
	
	@RequestMapping(value = "/metadata/{clientId}", method = RequestMethod.GET)
	public HttpEntity<byte[]> getFile(HttpServletResponse response, @PathVariable String clientId) throws IOException, ConfigurationException, MarshallingException {
		// Cria o metadata do IDP
		String idpMetadata = encoder.createIdpMetadata("localhost", clientId);

		// Cria o arquivo temporário
		File f = temporary.createFile(idpMetadata, clientId, "xml");
		
		// Cria um httpEntity de acordo com o arquivo
		HttpEntity<byte[]> entity = httpUtils.createHttpEntity(f.getPath(), f.getName());
		
		// Remove o arquivo
		f.delete();
		
		// Retorna para download o metadata via xml
        return entity;
	}
	
	@GetMapping("/redirect/sso/{clientId}")
	public String post(HttpServletRequest request, @PathVariable String clientId) throws ConfigurationException, ParserConfigurationException, SAXException, IOException, UnmarshallingException, SAMLException, CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, MarshallingException {
		// http://localhost:8080/idp/saml/redirect/sso/8ac93933-1f97-424b-92ba-e1c55182ab5f?SAMLRequest=fZFPb4MwDMXP%2BxYodyD86YosQOrUwyqtUtVVO%2BwyBchoJHBYHKR%2B%2FIWwQ3epb%2FF7tp9%2BKUmMwwS72V7xLH9mSTa4jQMSeKFis0HQghQBilES2Bbed8c3SCMOk9FWt3pggaunvZtVKKzSWLGrtRNBHKMO5U0tQh%2F1o1BD1OoxakxMqkeFsR897Cv2tWmLrun4s0zzgn9vs7TItqtKNMsDkhVoK5bypAh5HvLkkmSQ5rDJPr3t9BfmRWHnrj1O3qwmgtfL5RSeZaeMbC0LPqQhn9%2BZWF0uDMDfN3dUHq8WRNIsEFi92IQj65oNhtbz6eEojBJlfLe7Xl%2F%2Fv6H%2BBQ%3D%3D&RelayState=9s-ZAJHr19DmLswuEH2zaxjyTCh4aAOe&SigAlg=http%3A%2F%2Fwww.w3.org%2F2000%2F09%2Fxmldsig%23rsa-sha1&Signature=RYTXn1bL52tMDRZQ5voa0gfr%2FOQ45vwGsukDwY%2BvoJg6Ck0N0uzH53AW%2FCwxI8UxCo7%2FL8Ur35z0t0b%2B1uB8Py%2BZC8h6hGE%2FyNEDCTWfpjeIUm6fqd7PAuFZuej%2B1ZwiT3TdqeFyKfkidjN0ps6HwCq%2BUKd0C1yRJcYtebLTJ3TRuFonObCzpUAdx7JjqpkHkHTp%2FCtS7A7DqHR0lUTjNK7u9%2F%2BIXMGHZAGWEhIjHOuSMwXttFAe2smZ%2FzPr2rcwm%2FqifRcvbmybQLbzDqcBH7SXtEHv16AlHhAT0vikdu56jzqP7W%2FRLKvq0RTaxHOaiwZEodrwiFv1wgwnU13HiQ%3D%3D
		// URL Aux: https://github.com/onelogin/java-saml/blob/bb39ee487829deeb0224355300b3f35309b2b4e3/core/src/test/java/com/onelogin/saml2/test/logout/LogoutRequestTest.java
		// URL Aux: https://github.com/onelogin/java-saml/blob/master/core/src/main/java/com/onelogin/saml2/util/Util.java
		// URL Aux: https://github.com/onelogin/java-saml/blob/master/core/src/main/java/com/onelogin/saml2/logout/LogoutRequest.java
		
		// https://www.samltool.com/validate_authn_req.php
		
		final String samlRequest = request.getParameter("SAMLRequest");
		final String relayState = request.getParameter("RelayState");
		final String sigAlg = request.getParameter("SigAlg");
		final String signature = request.getParameter("Signature");
		
		final String username = "c4ll";
		final String email = "c4ll@my-fake-interprise.com.br";
		final List<String> applicationsId = Arrays.asList("11111-0cab-47a5-bca3-f6a2e354a5c3", "8ac93933-1f97-424b-92ba-e1c55182ab5f");
		final List<String> applicationGroups = Arrays.asList("um", "z");
		
		System.out.println("\n///////////////");
		System.out.println("SAMLRequest=" + samlRequest);
		System.out.println("RelayState=" + relayState);
		System.out.println("SigAlg=" + sigAlg);
		System.out.println("Signature=" + signature);
		System.out.println("///////////////\n");
		
		final SamlAppConfig samlAppConfig = service.get(clientId);
		
		// Verificando se a aplicação existe
		if(samlAppConfig == null) {
			return "error";
		}
		
		// Verificando se o usuário possui a aplicação
		if(!applicationsId.contains(clientId)) {
			return "error";
		}
		
		boolean validateGroup = false;
		
		// Verificando se o usuário possui os grupos que a aplicação solicita
		for(String groupId : samlAppConfig.getGroups()) {
			validateGroup = applicationGroups.contains(groupId);
			
			if(validateGroup) {
				break;
			}
		}
		
		if(!validateGroup) {
			return "error";
		}
		
		// Fazendo a validação do RelayState
		final SamlAppConfigDetail detail = samlAppConfig.getDetail();
		if(detail != null) {
			final String relayStateApp = detail.getRelayState();
			
			if(relayStateApp != null && !relayStateApp.toString().trim().equals("") && !relayStateApp.equals(relayState)) {
				return "error";
			}
		}
		
		boolean signatureValidated = validate.validateSignature(samlRequest, relayState, sigAlg, signature, detail.getX509Cert(), clientId);
		System.out.println(signatureValidated);
		
		// Decodificando o SAMLRequest
		AuthnRequest decodeSamlRequest = decoder.decodeSamlRequest(samlRequest);
		
		// essa validação é só redirect, tem outra do post
		// https://rnd.feide.no/anatomy_of_saml_messages/
		
		try {
			validate.validateAuthnRequestRedirectBinding(decodeSamlRequest);
		} catch (Exception e) {
			return "error";
		}
		
		return "login/form";
	}
	
	
}
