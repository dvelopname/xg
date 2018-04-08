package br.com.c4ll.saml.encode;

import java.io.StringWriter;
import java.rmi.server.UID;
import java.util.UUID;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml2.metadata.NameIDFormat;
import org.opensaml.saml2.metadata.SingleLogoutService;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.saml2.metadata.impl.EntityDescriptorBuilder;
import org.opensaml.saml2.metadata.impl.EntityDescriptorMarshaller;
import org.opensaml.saml2.metadata.impl.IDPSSODescriptorBuilder;
import org.opensaml.saml2.metadata.impl.KeyDescriptorBuilder;
import org.opensaml.saml2.metadata.impl.NameIDFormatBuilder;
import org.opensaml.saml2.metadata.impl.SingleLogoutServiceBuilder;
import org.opensaml.saml2.metadata.impl.SingleSignOnServiceBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.X509Certificate;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.signature.impl.KeyInfoBuilder;
import org.opensaml.xml.signature.impl.X509CertificateBuilder;
import org.opensaml.xml.signature.impl.X509DataBuilder;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import br.com.c4ll.saml.config.SamlAppConfig;

@Component
public class SamlEncoder {
	
	
	private boolean isNullOrEmpty(Object object) {
		return object == null || object.toString().trim().length() == 0;
	}
	
	private Response createSamlDefaultResponse(AuthnRequest request, SamlAppConfig app) {
		final ResponseBuilder responseBuilder = new ResponseBuilder();
		Response response = responseBuilder.buildObject();
		response.setID(UUID.randomUUID().toString()); 
		response.setVersion(SAMLVersion.VERSION_20);
		response.setInResponseTo(request.getID());
		
		final DateTime date = new DateTime().now();
		response.setIssueInstant(date);
		
		final String destinationValue = !isNullOrEmpty(app.getDetail().getDestination()) 
				? app.getDetail().getDestination()
				: app.getDetail().getEntityID();
		
		response.setDestination(destinationValue);
		
		final String issuerValue = app.getDetail().getEntityID();
		
		IssuerBuilder issuerBuilder = new IssuerBuilder();
		Issuer issuer = issuerBuilder.buildObject();
		issuer.setValue(issuerValue);
		
		response.setIssuer(issuer);
		
		// ADD CREDENTIALS (SIGNATURE NA RESPONSE) ?? ACHO QUE SIM
		
		boolean versionSenderIsNotSuported = false;
		boolean protocolBindingNotKnown = false;
		boolean requesterSendRequestWithError = false;
		boolean authnLoginSucess = true;
		
		StatusBuilder statusBuilder = new StatusBuilder();
		Status status = statusBuilder.buildObject();
		
		StatusCodeBuilder statusCodeBuilder = new StatusCodeBuilder();
		StatusCode statusCode = statusCodeBuilder.buildObject();

		if(versionSenderIsNotSuported) { // Versão não suportada
			statusCode.setValue(StatusCode.VERSION_MISMATCH_URI);
		} else if(protocolBindingNotKnown) { // Protocolo binding não suportado (ou não conhecido, vem um valor diferente)
			statusCode.setValue(StatusCode.UNSUPPORTED_BINDING_URI);
		} else if(requesterSendRequestWithError) { // Enviou, por exemplo, com destination inválido
			statusCode.setValue(StatusCode.REQUESTER_URI);
		} else if(!authnLoginSucess) { // Falha no login
			statusCode.setValue(StatusCode.AUTHN_FAILED_URI);
		} else { // Sucesso no login
			statusCode.setValue(StatusCode.SUCCESS_URI);
		}
		
		status.setStatusCode(statusCode);
		response.setStatus(status);
		
		return response;
	}
	
	public void createSamlResponseWith(AuthnRequest request, SamlAppConfig app) {
		Response response = createSamlDefaultResponse(request, app);
		
	}
	
	
	
	public void createSamlResponse(AuthnRequest authnRequest, SamlAppConfig app) throws MarshallingException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {

//		1) An unsigned SAML Response
//		    - with an unsigned Assertion
//		    - with a signed Assertion
//    
//		    - with an encrypted Assertion
//		    - with an encrypted signed Assertion
//
//    	2) An signed SAML Response
//		    - with an unsigned Assertion
//		    - with a signed Assertion
//		    
//		    - with an encrypted Assertion
//		    - with an encrypted signed Assertion
		
		boolean signedSamlResponse = false;
		boolean signedAssertionOfSamlResponse = false;
		boolean encryptedAssertionOfSamlResponse = false;
		
		boolean signedAssertionOfAssertion = false;
		boolean encryptedAssertionOfAssertion = false;
		
		// Inherited from samlp:StatusResponseType
		
		Response response = createSamlDefaultResponse(authnRequest, app);
		
		
		// Validando a response (Se é StatusCode != Sucess) transformar em XML e enviar
		if(!response.getStatus().getStatusCode().getValue().equals(StatusCode.SUCCESS_URI)) { // mudar dps
			ResponseMarshaller responseMarshaller = new ResponseMarshaller();
			Element marshall = responseMarshaller.marshall(response);
			
			TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(marshall);
            StreamResult result = new StreamResult(new StringWriter());

            transformer.transform(source, result);

            String strObject = result.getWriter().toString();
            
			System.out.println(strObject);
		}
		
		
		// An unsigned SAML Response with an unsigned Assertion
		if(!signedSamlResponse && !signedAssertionOfSamlResponse) {
			
		}
			
	}
	
	public String createIdpMetadata(final String tenantId, final String entityId) throws ConfigurationException, MarshallingException {
		EntityDescriptor idpEntityDescriptor = createIDPEntityDescriptor(tenantId, entityId);
		String str = convertEntityDescriptorToXMLString(idpEntityDescriptor);
		
		return str;
	}

	private String convertEntityDescriptorToXMLString(EntityDescriptor idpEntityDescriptor) throws ConfigurationException, MarshallingException {
		DefaultBootstrap.bootstrap();
		final EntityDescriptorMarshaller entityDescriptorMarshaller = new EntityDescriptorMarshaller();
		final Element element = entityDescriptorMarshaller.marshall(idpEntityDescriptor);
		
		final Document document = element.getOwnerDocument();
		final DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
		final LSSerializer serializer = domImplLS.createLSSerializer();
		final String str = serializer.writeToString(element);
		
		return str;
	}
	
	private EntityDescriptor createIDPEntityDescriptor(final String tenantId, final String entityId) {
		final String certificate = "IdpCertificate==";
		final String singleLogoutServiceRedirectLocation = "https://".concat(tenantId).concat(".enterprise.com/idp/saml/redirect/slo/".concat(entityId));
		final String singleSignOnServiceRedirectLocation = "https://".concat(tenantId).concat(".enterprise.com/idp/saml/redirect/sso/".concat(entityId));
		final String singleSignOnServicePostLocation = "https://".concat(tenantId).concat(".enterprise.com/idp/saml/post/sso/".concat(entityId));
		
		EntityDescriptorBuilder entityDescriptorBuilder = new EntityDescriptorBuilder();
		EntityDescriptor entityDescriptor = entityDescriptorBuilder.buildObject();
		entityDescriptor.setEntityID(entityId);
		
		IDPSSODescriptorBuilder idpssoDescriptorBuilder = new IDPSSODescriptorBuilder();
		IDPSSODescriptor idpssoDescriptor = idpssoDescriptorBuilder.buildObject();
		
		final KeyDescriptor keyDescriptorSignIn = createKeyDescriptor(UsageType.SIGNING, certificate);
		final KeyDescriptor keyDescriptorEncryption = createKeyDescriptor(UsageType.ENCRYPTION, certificate);
		
		idpssoDescriptor.getKeyDescriptors().add(keyDescriptorSignIn);
		idpssoDescriptor.getKeyDescriptors().add(keyDescriptorEncryption);
		
		final SingleLogoutService singleLogoutService = createSingleLogout(SAMLConstants.SAML2_REDIRECT_BINDING_URI, singleLogoutServiceRedirectLocation);
		idpssoDescriptor.getSingleLogoutServices().add(singleLogoutService);
		
		final NameIDFormat nameIdFormat = createNameIdFormat(NameIDType.EMAIL);
		idpssoDescriptor.getNameIDFormats().add(nameIdFormat);
		
		final SingleSignOnService singleSignOnServiceRedirect = createSingleSignOnService(SAMLConstants.SAML2_REDIRECT_BINDING_URI, singleSignOnServiceRedirectLocation);
		idpssoDescriptor.getSingleSignOnServices().add(singleSignOnServiceRedirect);
		
		final SingleSignOnService singleSignOnServicePost = createSingleSignOnService(SAMLConstants.SAML2_POST_BINDING_URI, singleSignOnServicePostLocation);
		idpssoDescriptor.getSingleSignOnServices().add(singleSignOnServicePost);
		
		entityDescriptor.getRoleDescriptors().add(idpssoDescriptor);
		
		return entityDescriptor;
	}

	public KeyDescriptor createKeyDescriptor(UsageType type, String x509Cert) {
		KeyDescriptorBuilder keyDescriptorBuilder = new KeyDescriptorBuilder();
		KeyDescriptor keyDescriptor = keyDescriptorBuilder.buildObject();
		keyDescriptor.setUse(type);
		
		KeyInfoBuilder keyInfoBuilder = new KeyInfoBuilder();
		KeyInfo keyInfo = keyInfoBuilder.buildObject();
		
		X509DataBuilder x509DataBuilder = new X509DataBuilder();
		X509Data x509Data = x509DataBuilder.buildObject();
		
		X509CertificateBuilder x509CertificateBuilder = new X509CertificateBuilder();
		X509Certificate x509Certificate = x509CertificateBuilder.buildObject();
		x509Certificate.setValue(x509Cert);
		
		x509Data.getX509Certificates().add(x509Certificate);
		keyInfo.getX509Datas().add(x509Data);
		keyDescriptor.setKeyInfo(keyInfo);
		
		return keyDescriptor;
	}
	
	private SingleLogoutService createSingleLogout(String binding, String location) {
		SingleLogoutServiceBuilder singleLogoutServiceBuilder = new SingleLogoutServiceBuilder();
		SingleLogoutService singleLogoutService = singleLogoutServiceBuilder.buildObject();
		singleLogoutService.setBinding(binding);
		singleLogoutService.setLocation(location);
		
		return singleLogoutService;
	}
	
	public SingleSignOnService createSingleSignOnService(String binding, String location) {
		SingleSignOnServiceBuilder singleSignOnServiceBuilder = new SingleSignOnServiceBuilder();
		SingleSignOnService singleSignOnService = singleSignOnServiceBuilder.buildObject();
		singleSignOnService.setBinding(binding);
		singleSignOnService.setLocation(location);
		
		return singleSignOnService;
	}
	
	private NameIDFormat createNameIdFormat(String type) {
		NameIDFormatBuilder nameIDFormatBuilder = new NameIDFormatBuilder();
		NameIDFormat nameIDFormat = nameIDFormatBuilder.buildObject();
		nameIDFormat.setFormat(type);
		
		return nameIDFormat;
	}
	
}
