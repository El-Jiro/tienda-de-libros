package gm.tienda_libros.servicio;

import gm.tienda_libros.modelo.Libro;
import java.util.List;

public interface ILibroServicio {

     List<Libro> listarLibros();

     Libro buscarLibroPorId(Integer idLibro);

     void guardarLibro(Libro libro);

     void eliminarLibro(Integer idLibro);

     boolean verificarExistencia(Integer idLibro);

}
