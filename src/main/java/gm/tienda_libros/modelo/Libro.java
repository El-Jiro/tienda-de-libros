package gm.tienda_libros.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Libro {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer idLibro;

    private String titulo;
    private String autor;
    private String editorial;
    private Year año;
    private float precio;
    private int existencias;

    @Builder(builderMethodName = "builderSinId")
    public Libro(String titulo, String autor, String editorial, Year año, float precio, int existencias) {
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.año = año;
        this.precio = precio;
        this.existencias = existencias;
    }
}
