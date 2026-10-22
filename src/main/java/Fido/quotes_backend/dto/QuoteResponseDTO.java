package Fido.quotes_backend.dto;

public record QuoteResponseDTO(
        Long id,
        String author,
        String text
) { }
