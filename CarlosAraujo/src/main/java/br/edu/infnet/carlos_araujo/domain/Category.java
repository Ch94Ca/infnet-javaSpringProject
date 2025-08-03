package br.edu.infnet.carlos_araujo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
    private String description;
    private String color;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

