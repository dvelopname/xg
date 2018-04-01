package br.com.c4ll.saml.decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLException;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.impl.AuthnRequestUnmarshaller;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.Base64;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

@Component
public class SamlDecoder {
	
	public AuthnRequest decodeSamlRequest(String samlRequest) throws ConfigurationException, ParserConfigurationException, SAXException, IOException, UnmarshallingException, SAMLException {
	    DefaultBootstrap.bootstrap();
	    
		byte[] decodedBytes = Base64.decode(samlRequest);
		
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(decodedBytes);
        InflaterInputStream inflater = new InflaterInputStream(bytesIn, new Inflater(true));
	    
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		
        Document document = documentBuilderFactory.newDocumentBuilder().parse(inflater);
	    Element element = document.getDocumentElement();
	    
	    AuthnRequestUnmarshaller authnRequestUnmarshaller = new AuthnRequestUnmarshaller();
	    XMLObject authnRequestObject = authnRequestUnmarshaller.unmarshall(element);
	    
	    AuthnRequest request = (AuthnRequest) authnRequestObject;
		return request;
	}
	
	
}
