/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/authors")
	public ResponseEntity<Object> getAllAuthor() {

		List<Author> authors = authorService.findAll();

		return new ResponseEntity<>(authors, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/authors/{id}")
	public ResponseEntity<Object> getAuthor(@PathVariable("id") Long id) throws AuthorNotFoundException {

		Author author = authorService.findById(id);

		return new ResponseEntity<>(author, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/authors/{id}/books")
	public ResponseEntity<Object> getBooksByAuthorId(@PathVariable("id") Long id) throws AuthorNotFoundException {

		Set<Book> books = authorService.findBooksByAuthorId(id);

		return new ResponseEntity<>(books, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/authors")
	public ResponseEntity<Object> createAuthor(@RequestBody Author author) throws BookNotFoundException {

		Author nauthor = authorService.createAuthor(author);

		return new ResponseEntity<>(nauthor, HttpStatus.CREATED);

	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/authors/{id}")
	public ResponseEntity<Object> updateAuthor(@PathVariable("id") Integer id, @RequestBody Author author)
			throws AuthorNotFoundException, BookNotFoundException {

		Author nauthor = authorService.updateAuthor(author, id);

		return new ResponseEntity<>(nauthor, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/authors/{id}")
	public ResponseEntity<Object> deleteAuthor(@PathVariable("id") Long id) throws AuthorNotFoundException {

		authorService.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
