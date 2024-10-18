/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private BookRepository bookRepository;

	public Author findById(long id) throws AuthorNotFoundException {

		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + " not found!"));

		return author;
	}

	public Author saveAuthor(Author author) {

		return authorRepository.save(author);

	}

	public Author createAuthor(Author author) throws BookNotFoundException {

		Author nauthor = authorRepository.save(author);

		if (author.getBooks() != null) {
			for (Book book : author.getBooks()) {
				Book nbook = bookRepository.findBookByISBN(book.getIsbn());
				if (nbook == null) {
					throw new BookNotFoundException("Book with ISBN " + book.getIsbn() + " not found.");
				}
				nbook.addAuthor(nauthor);
				bookRepository.save(nbook);
			}
		}

		return nauthor;
	}

	public Author updateAuthor(Author author, long id) throws AuthorNotFoundException, BookNotFoundException {

		Author nauthor = authorRepository.findAuthorByAuthorId(id);

		if (nauthor == null) {
			throw new AuthorNotFoundException("Author with the id: " + id + " not found!");
		}
		nauthor.setFirstname(author.getFirstname());
		nauthor.setLastname(author.getLastname());

		if (author.getBooks() != null) {
			for (Book book : author.getBooks()) {
				Book nbook = bookRepository.findBookByISBN(book.getIsbn());
				if (nbook == null) {
					throw new BookNotFoundException("Book with ISBN " + book.getIsbn() + " not found.");
				}
				nbook.addAuthor(nauthor);
				bookRepository.save(nbook);
			}
		}

		return authorRepository.save(nauthor);

	}

	public List<Author> findAll() {

		return (List<Author>) authorRepository.findAll();

	}

	public void deleteById(Long id) throws AuthorNotFoundException {

		Author author = authorRepository.findById((long) id)
				.orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + " not found!"));

		if (author.getBooks() != null) {
			for (Book book : author.getBooks()) {
				book.removeAuthor(author);
				bookRepository.save(book);
			}

		}
		authorRepository.delete(author);

	}

	public Set<Book> findBooksByAuthorId(Long id) throws AuthorNotFoundException {

		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + " not found!"));

		return author.getBooks();

	}

}
