/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedUserActionException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.BookRepository;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;
import no.hvl.dat152.rest.ws.security.service.UserDetailsImpl;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private OrderRepository orderRepository;

	public List<User> findAllUsers() {

		List<User> allUsers = (List<User>) userRepository.findAll();

		return allUsers;
	}

	public User saveUser(User user) {

		return userRepository.save(user);
	}

	public void deleteUser(Long id) throws UserNotFoundException {

		User user = findUser(id);

		userRepository.delete(user);
	}

	public Set<Order> getUserOrders(Long userid) {

		verifyPrincipalOfUser(userid);

		Optional<User> user = userRepository.findById(userid);

		return user.get().getOrders();

	}

	public User updateUser(User user, Long id) throws UserNotFoundException {

		User nuser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
		verifyPrincipalOfUser(id);
		nuser.setFirstname(user.getFirstname());
		nuser.setLastname(user.getLastname());

		if (user.getOrders() != null) {
			for (Order order : user.getOrders()) {
				nuser.addOrder(order);
			}
		}

		return userRepository.save(nuser);

	}

	public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {

		verifyPrincipalOfUser(userid);

		Optional<User> user = userRepository.findById(userid);

		if (user.isEmpty())
			throw new UserNotFoundException("User with id: " + userid + " not found");

		Order order = userRepository.findOrderByUserIdAndOrderId(userid, oid);

		if (order == null)
			throw new OrderNotFoundException("Order with id: " + oid + " not found in the order list!");

		return order;

	}

	public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {

		Optional<User> user = userRepository.findById(userid);
//		verifyPrincipalOfUser(userid);
		if (user.isEmpty())
			throw new UserNotFoundException("User with id: " + userid + " not found");

		Order order = userRepository.findOrderByUserIdAndOrderId(userid, oid);

		if (order == null)
			throw new OrderNotFoundException("Order with id: " + oid + " not found in the order list!");

		orderRepository.delete(order);

	}

	public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException, BookNotFoundException {

		verifyPrincipalOfUser(userid);

		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userid + " not found"));

		if (bookRepository.findBookByISBN(order.getIsbn()) == null) {
			throw new BookNotFoundException("Book with ISBN " + order.getIsbn() + " not found.");
		}

		user.addOrder(order);

		orderRepository.save(order);

		return userRepository.save(user);

	}

	public User findUser(Long id) throws UserNotFoundException {

		verifyPrincipalOfUser(id);
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found in the user list!"));

		return user;
	}

	private boolean verifyPrincipalOfUser(Long id) throws UnauthorizedUserActionException {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		System.out.println(userPrincipal.getFirstname());
		for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
			if (authority.getAuthority().equals("ADMIN") || authority.getAuthority().equals("SUPER_ADMIN")) {
				return true;
			}
		}

		String email = userRepository.findEmailByOrderId(id);

		System.out.println(email.equals(userPrincipal.getEmail()));
		if (email.equals(userPrincipal.getEmail()))
			return true;

		throw new UnauthorizedUserActionException("Unauthorized user action!");
	}

}
