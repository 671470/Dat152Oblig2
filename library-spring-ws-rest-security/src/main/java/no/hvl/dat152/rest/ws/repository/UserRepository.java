/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;

/**
 * 
 */
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);

	Optional<User> findById(Long id);

	@Query("SELECT o FROM User u JOIN u.orders o WHERE u.userid = :userId AND o.id = :orderId")
	Order findOrderByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);

	@Query(value = "SELECT email FROM users WHERE userid = :userid", nativeQuery = true)
	String findEmailByOrderId(Long userid);
}
