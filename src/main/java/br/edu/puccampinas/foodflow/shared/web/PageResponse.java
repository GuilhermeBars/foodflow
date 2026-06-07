package br.edu.puccampinas.foodflow.shared.web;

import java.util.List;
import org.springframework.data.domain.Page;

/** Envelope de paginacao estavel para a API, independente do tipo de pagina interno. */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
