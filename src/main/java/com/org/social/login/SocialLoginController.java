package com.org.social.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@RequestMapping("/sociallogin")
@Controller
public class SocialLoginController {
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager manager; 
	
	@RequestMapping("/google")
	public String loginGoogle(@RequestParam(value = "token", required = false) String id) {
		System.out.println("logging in " + id);
		try {
			final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
			// final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			JacksonFactory jsonFactory = new JacksonFactory();
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					.setAudience(Collections
							.singletonList("331621766217-g19moqkodnpv06ngbimch9knr3rbdpuo.apps.googleusercontent.com"))
					.build();

			// (Receive idTokenString by HTTPS POST)
			GoogleIdToken idToken = GoogleIdToken.parse(jsonFactory, id);
			verifier.verify(idToken);
			if (idToken != null) {
				com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload payload = idToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();
				System.out.println("User ID: " + userId);

				// Get profile information from payload
				String email = payload.getEmail();
				boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
				String name = (String) payload.get("name");
				String pictureUrl = (String) payload.get("picture");
				String locale = (String) payload.get("locale");
				String familyName = (String) payload.get("family_name");
				String givenName = (String) payload.get("given_name");

				if (emailVerified) {

					System.out.println("logged in withuser new");
					// SignInUtils.signin("admin", manager);
					UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("user",
							"user");
					Authentication auth = manager.authenticate(authReq);
					SecurityContextHolder.getContext().setAuthentication(auth);
				}

			} else {
				System.out.println("Invalid ID token.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	public Collection<GrantedAuthority> getAuthorities() {
        //make everyone ROLE_USER
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            //anonymous inner type
            public String getAuthority() {
                return "USER_ROLE";
            }
        }; 
        grantedAuthorities.add(grantedAuthority);
        return grantedAuthorities;
    }
}
