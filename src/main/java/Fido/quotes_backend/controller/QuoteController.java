package Fido.quotes_backend.controller;

import Fido.quotes_backend.dto.QuoteRequestDTO;
import Fido.quotes_backend.dto.QuoteResponseDTO;
import Fido.quotes_backend.model.Quote;
import Fido.quotes_backend.service.QuoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(path="quotes")
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuoteResponseDTO create(@RequestBody QuoteRequestDTO quoteRequestDTO) {
        Quote savedQuote = quoteService.create(quoteRequestDTO);

        return new QuoteResponseDTO(
                savedQuote.getId(),
                savedQuote.getAuthor(),
                savedQuote.getText()
        );
    }

    @GetMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuoteResponseDTO getById(@PathVariable(name = "id") Long id) {
        Quote found = quoteService.findById(id);

        return new QuoteResponseDTO(
                found.getId(),
                found.getAuthor(),
                found.getText()
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QuoteResponseDTO> getAllByAuthor(@RequestParam(name = "author", required = false) String author, Pageable pageable) {
        List<Quote> found = quoteService.findAllByAuthor(author, pageable);
        return found.stream().map(
               quote -> new QuoteResponseDTO(
                       quote.getId(),
                       quote.getAuthor(),
                       quote.getText()
               )
        ).toList();
    }

    @DeleteMapping(path="{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name ="id") Long id) {
        quoteService.deleteById(id);
    }

    @PutMapping(path= "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable(name = "id") Long id, @RequestBody QuoteRequestDTO quoteRequestDTO) {
        quoteService.update(id, quoteRequestDTO);
    }
}