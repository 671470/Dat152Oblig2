/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.RoleRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class AdminUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public User saveUser(User user) {

		user = userRepository.save(user);

		return user;
	}

	public User deleteUserRole(Long id, String role) throws RoleNotFoundException, UserNotFoundException {

		User user = findUser(id);

		Role rRole = roleRepository.findByName(role);

		user.removeRole(rRole);

		return userRepository.save(user);
	}

	public User updateUserRole(Long id, String role) throws RoleNotFoundException, UserNotFoundException {

		User user = findUser(id);

		Role rRole = roleRepository.findByName(role);

		user.addRole(rRole);

		return userRepository.save(user);

	}

	public User findUser(Long id) throws UserNotFoundException {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

		return user;
	}
}
