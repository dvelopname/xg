package br.com.c4ll.oauth2;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.oltu.oauth2.as.issuer.ValueGenerator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

public class SHA512Generator implements ValueGenerator {

	@Override
	public String generateValue() throws OAuthSystemException {
		return generateValue(UUID.randomUUID().toString() + UUID.randomUUID().toString());
	}

	@Override
	public String generateValue(String param) throws OAuthSystemException {
		MessageDigest objSHA;
		try {
			objSHA = MessageDigest.getInstance("SHA-512");
			byte[] bytSHA = objSHA.digest(param.getBytes());
			BigInteger intNumber = new BigInteger(1, bytSHA);
			String strHashCode = intNumber.toString(16);

			while (strHashCode.length() < 128) {
				strHashCode = "0" + strHashCode;
			}
			
			return strHashCode;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}

	}
}
