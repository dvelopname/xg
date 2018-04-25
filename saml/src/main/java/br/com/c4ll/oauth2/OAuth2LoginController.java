package br.com.c4ll.oauth2;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.opensaml.storage.annotation.Context;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/idp/oauth2")
public class OAuth2LoginController {
	
	@RequestMapping("/authorize")
	public String get(HttpServletRequest request) throws OAuthSystemException, URISyntaxException {
		// http://localhost:8080/idp/oauth2/authorize?response_type=value_rs&client_id=value_cid&redirect_uri=value_redirect&scope=value_scope&state=value_state
		
		final String responseType = request.getParameter(OAuth.OAUTH_RESPONSE_TYPE);
		final String clientId = request.getParameter(OAuth.OAUTH_CLIENT_ID);
		final String redirectUri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
		final String scope = request.getParameter(OAuth.OAUTH_SCOPE);
		final String state = request.getParameter(OAuth.OAUTH_STATE);
		
		System.out.println("response_type=" + responseType);
		System.out.println("client_id=" + clientId);
		System.out.println("redirect_uri=" + redirectUri);
		System.out.println("scope=" + scope);
		System.out.println("state=" + state);
		
		OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new SHA512Generator());
		
		OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);

		String code = oauthIssuerImpl.authorizationCode();
		
		if (responseType.equals(ResponseType.CODE.toString())) {
			builder.setCode(code);
		}
		
		if (responseType.equals(ResponseType.TOKEN.toString())) {
			builder.setAccessToken(oauthIssuerImpl.accessToken());
			builder.setExpiresIn(3600l);
		}
		
		final OAuthResponse response = builder.location(redirectUri).buildQueryMessage();
		URI url = new URI(response.getLocationUri());
		
		System.out.println("****************************");
		System.out.println("Response Type: " + responseType);
		System.out.println("Code         : " + code);
		System.out.println("Redirect URL : " + redirectUri);
		
		// Resposta:
		// 	- code: código de autorização
		// 	- state: se não me engano é para estado da conexão
		
		return "login/form";
	}
	
}
