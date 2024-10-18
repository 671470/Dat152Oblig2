/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;

import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public Book saveBook(Book book) {

		return bookRepository.save(book);

	}

	public List<Book> findAll() {

		return (List<Book>) bookRepository.findAll();

	}

	public Book findByISBN(String isbn) throws BookNotFoundException {

		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("Book with isbn = " + isbn + " not found!"));

		return book;
	}

	public void deleteByISBN(String isbn) throws BookNotFoundException {

		Book book = bookRepository.findBookByISBN(isbn);
		if (book == null) {
			throw new BookNotFoundException("Book with ISBN " + isbn + " not found.");
		}

		bookRepository.delete(book);

	}

	public Book updateBook(Book book, String isbn) throws BookNotFoundException {

		Book oldBook = bookRepository.findBookByISBN(isbn);

		if (oldBook == null) {
			throw new BookNotFoundException("Book with ISBN " + isbn + " not found.");
		}

		oldBook.setTitle(book.getTitle());
		oldBook.setAuthors(book.getAuthors());

		return bookRepository.save(oldBook);

	}

	public void deleteById(Long id) throws BookNotFoundException {

		Optional<Book> book = bookRepository.findById(id);

		if (book.isPresent()) {
			bookRepository.delete(book.get());
		} else {
			throw new BookNotFoundException("Book with ID " + id + " not found.");
		}

	}

	public List<Book> findAllPaginate(Pageable page) {

		return bookRepository.findAllPaginate(page.getPageSize(), (int) page.getOffset());

	}

	public Set<Author> findAuthorsOfBookByISBN(String isbn) {

		Book book = bookRepository.findBookByISBN(isbn);

		return book.getAuthors();

	}

}
