package com.biblioteca.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate dataRetirada;
    public List<Long> livros;
}
