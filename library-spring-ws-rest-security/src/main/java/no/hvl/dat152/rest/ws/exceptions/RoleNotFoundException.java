/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 */
public class RoleNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public RoleNotFoundException(String customMessage) {
		super(customMessage);
	}
	
}
