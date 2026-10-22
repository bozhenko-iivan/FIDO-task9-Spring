package Fido.quotes_backend.service;

import Fido.quotes_backend.dto.QuoteRequestDTO;
import Fido.quotes_backend.model.Quote;
import Fido.quotes_backend.repository.QuoteRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Quote validateQuote(Quote quote) {
        if (quote == null || quote.getAuthor() == null || quote.getAuthor().isBlank() || quote.getAuthor().length() > 200) {
            throw new IllegalArgumentException("Author can not be blank and be greater than 200 characters");
        }
        if (quote.getText() == null || quote.getText().isBlank() || quote.getText().length() > 1000) {
            throw new IllegalArgumentException("Text can not be empty and be greater than 1000");
        }
        return quote;
    }

    public Quote create(QuoteRequestDTO quoteRequestDTO) {
        Quote newQuote = new Quote();

        newQuote.setAuthor(quoteRequestDTO.author());
        newQuote.setText(quoteRequestDTO.text());

        Quote validQuote = validateQuote(newQuote);
        if (quoteRepository.existsByAuthorAndText(validQuote.getAuthor(), validQuote.getText())) {
            throw new IllegalArgumentException("Quote with "
                    + validQuote.getAuthor()
                    + " and "
                    + validQuote.getText()
                    + " already exists"
            );
        }
        return quoteRepository.save(validQuote);
    }

    public Quote findById(Long id) {
        return quoteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Quote with id " + id + " does not exist"));
    }

    public List<Quote> findAllByAuthor(String author, Pageable pageable) {
        if (author == null || author.isBlank()) {
            return quoteRepository.findAll(pageable).getContent();
        }
        return quoteRepository.findAllByAuthor(author, pageable).getContent();
    }

    public void deleteById(Long id) {
        Quote presentQuote = findById(id);
        quoteRepository.delete(presentQuote);
    }

    public void update(Long id, QuoteRequestDTO quoteRequestDTO) {
        Quote presentQuote = findById(id);

        if (quoteRepository.existsByAuthorAndTextAndIdNot(presentQuote.getAuthor(), presentQuote.getText(), id)) {
            throw new IllegalArgumentException("Another quote with this author and text already exists");
        }

        presentQuote.setAuthor(quoteRequestDTO.author());
        presentQuote.setText(quoteRequestDTO.text());

        quoteRepository.save(validateQuote(presentQuote));
    }
}
