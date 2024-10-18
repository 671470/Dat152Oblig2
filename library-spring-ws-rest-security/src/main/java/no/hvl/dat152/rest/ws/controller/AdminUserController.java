/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.AdminUserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1/admin")
public class AdminUserController {

	@Autowired
	private AdminUserService userService;

	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updateUserRole(@PathVariable("id") Long id, @RequestParam("role") String role)
			throws UserNotFoundException, RoleNotFoundException {

		User user = userService.updateUserRole(id, role);
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Object> deleteUserRole(@PathVariable("id") Long id, @RequestParam("role") String role)
			throws UserNotFoundException, RoleNotFoundException {

		User user = userService.deleteUserRole(id, role);

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

}
