package br.com.c4ll.saml.validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.apache.commons.codec.binary.CharSequenceUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AuthnRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.c4ll.file.TemporaryFileUtils;

@Component
public class SamlValidate {
	
	private static final String VERSION_SUPPORTED = "2.0";
	private static final int MAX_MINUTES_SUPPORTED = 5; // TODO: definir
	
	@Autowired
	private TemporaryFileUtils temporary;
	
	public boolean validateSignature(String samlRequest, String relayState, String sigAlg, String signature, String x509Cert, String clientId) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		File fileTemp = temporary.createFile(x509Cert, clientId, "cer");
		FileInputStream fis = new FileInputStream(fileTemp);
		
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate cert = cf.generateCertificate(fis);
		
		String signedQuery = "SAMLRequest=" + URLEncoder.encode(samlRequest, "UTF-8");
		signedQuery += "&RelayState=" +URLEncoder.encode(relayState, "UTF-8");
		signedQuery += "&SigAlg=" + URLEncoder.encode(sigAlg, "UTF-8");
		byte[] signatureBytes = org.opensaml.xml.util.Base64.decode(signature);
		
		org.apache.xml.security.Init.init();
		String signatureEquivalentAlg = "SHA1withRSA";

		Signature sig = Signature.getInstance(signatureEquivalentAlg);
		sig.initVerify(cert.getPublicKey());
		sig.update(signedQuery.getBytes());
		Boolean valid = sig.verify(signatureBytes);
		
		return valid;
	}
	
	private boolean validateRedirectBindingUri(String bindingUri) {
		System.out.println(bindingUri);
		System.out.println(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
		
		return SAMLConstants.SAML2_REDIRECT_BINDING_URI.equals(bindingUri);
	}
	
	private boolean validateVersion(SAMLVersion version) {
		return version != null && version.toString().equals(VERSION_SUPPORTED);
	}

	public void validateAuthnRequestRedirectBinding(AuthnRequest decodeSamlRequest) throws Exception {
		boolean versionValidated = validateVersion(decodeSamlRequest.getVersion());
		
		if(!versionValidated) {
			throw new Exception("Invalid version");
		}
		
		boolean redirectBindingValidated = validateRedirectBindingUri(decodeSamlRequest.getProtocolBinding());
		
		if(!redirectBindingValidated) {
			throw new Exception("Invalid Binding");
		}
		
		// TODO: descomentar
//		boolean issueInstantValidated = validateIssueInstant(decodeSamlRequest.getIssueInstant());
//		
//		if(!issueInstantValidated) {
//			throw new Exception("Invalid Issue Instant");
//		}
		
		
	}

	private boolean validateIssueInstant(DateTime issueInstant) {
		if(issueInstant == null) {
			return false;
		}
		
		LocalDateTime dateRequest = LocalDateTime.ofInstant(Instant.ofEpochMilli(issueInstant.getMillis()), ZoneId.of("UTC"));
		LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
		
		LocalDateTime tempDateTime = LocalDateTime.from(dateRequest);

		long years = tempDateTime.until( now, ChronoUnit.YEARS);
		tempDateTime = tempDateTime.plusYears( years );

		long months = tempDateTime.until( now, ChronoUnit.MONTHS);
		tempDateTime = tempDateTime.plusMonths( months );

		long days = tempDateTime.until( now, ChronoUnit.DAYS);
		tempDateTime = tempDateTime.plusDays( days );

		long hours = tempDateTime.until( now, ChronoUnit.HOURS);
		tempDateTime = tempDateTime.plusHours( hours );

		long minutes = tempDateTime.until( now, ChronoUnit.MINUTES);
		tempDateTime = tempDateTime.plusMinutes( minutes );
		
		return (int) minutes <= MAX_MINUTES_SUPPORTED;
	}
	
}
