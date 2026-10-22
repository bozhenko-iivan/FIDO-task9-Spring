package Fido.quotes_backend.repository;

import Fido.quotes_backend.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    boolean existsByAuthorAndText(String author, String text);
    Page<Quote> findAllByAuthor(String author, Pageable pageable);
}