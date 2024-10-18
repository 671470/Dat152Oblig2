/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/orders")
	public ResponseEntity<Object> getAllBorrowOrders(@RequestParam(required = false) LocalDate expiry,
			@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {

		List<Order> orders = null;
		if (expiry != null) {

			Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("expiry").descending());
			orders = orderService.findByExpiryDate(expiry, pageable).getContent();

		} else {
			orders = orderService.findAllSortedByExpiry(pageNumber, pageSize);
		}

		return new ResponseEntity<>(orders, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/orders/{id}")
	public ResponseEntity<Object> getBorrowOrder(@PathVariable("id") Long id) throws OrderNotFoundException {

		Order order = orderService.findOrder(id);

		return new ResponseEntity<>(order, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('USER')")
	@PutMapping("/orders/{id}")
	public ResponseEntity<Object> updateOrder(@PathVariable("id") Long id, @RequestBody Order order)
			throws OrderNotFoundException {

		Order norder = orderService.updateOrder(order, id);

		return new ResponseEntity<>(norder, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('USER')")
	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Object> deleteBookOrder(@PathVariable("id") Long id) throws OrderNotFoundException {

		orderService.deleteOrder(id);

		return new ResponseEntity<>(HttpStatus.OK);

	}

}
