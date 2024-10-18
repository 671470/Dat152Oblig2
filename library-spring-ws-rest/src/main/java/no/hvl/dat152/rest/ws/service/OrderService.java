/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

/**
 * @author tdoy
 */
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order saveOrder(Order order) {

		order = orderRepository.save(order);

		return order;
	}

	public Order findOrder(Long id) throws OrderNotFoundException {

		Order order = orderRepository.findById(id).orElseThrow(
				() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));

		return order;
	}

	public void deleteOrder(Long id) throws OrderNotFoundException {

		Order order = orderRepository.findById(id).orElseThrow(
				() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));

		orderRepository.delete(order);

	}

	public List<Order> findAllOrders() {

		return orderRepository.findAll();
	}

	public Page<Order> findByExpiryDate(LocalDate expiry, Pageable page) {

		Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by("expiry").descending());

		return orderRepository.findOrderByExpiryAndSorted(expiry, pageable);
	}

	public Order updateOrder(Order order, Long id) throws OrderNotFoundException {

		Order norder = orderRepository.findById(id).orElseThrow(
				() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));

		norder.setExpiry(order.getExpiry());
		norder.setIsbn(order.getIsbn());

		return orderRepository.save(norder);
	}

	public List<Order> findAllSortedByExpiry(int pageNumber, int pageSize) {

		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("expiry").descending());

		Page<Order> orderSorted = orderRepository.findAll(page);

		return orderSorted.getContent();
	}

}
