/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;

/**
 * 
 */
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT o FROM User u JOIN u.orders o WHERE u.userid = :userId AND o.id = :orderId")
	Order findOrderByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);
}
