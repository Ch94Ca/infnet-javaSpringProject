package br.edu.infnet.carlos_araujo.application.use_case.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateCommand {
    private String name;
    private String description;
    private String color;
    private Long userId;
}
