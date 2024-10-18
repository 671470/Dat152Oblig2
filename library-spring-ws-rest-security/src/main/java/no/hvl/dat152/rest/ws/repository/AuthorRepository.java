/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import no.hvl.dat152.rest.ws.model.Author;

/**
 * @author tdoy
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	@Query("SELECT a FROM Author a WHERE a.authorId = :authorId")
	Author findAuthorByAuthorId(@Param("authorId") Long authorId);
	

}
