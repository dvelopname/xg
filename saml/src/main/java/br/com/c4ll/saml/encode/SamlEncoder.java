package br.com.c4ll.saml.encode;

import org.opensaml.DefaultBootstrap;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.NameIDType;
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

@Component
public class SamlEncoder {
	
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
