/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 */
public class UnauthorizedUserActionException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedUserActionException(String customMessage) {
		super(customMessage);
	}
	
}
