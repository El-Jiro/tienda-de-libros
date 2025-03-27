package gm.tienda_libros.servicio;

import gm.tienda_libros.modelo.Libro;
import gm.tienda_libros.repositorio.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServicio implements ILibroServicio{

    @Autowired
    private LibroRepositorio libroRepositorio;


    @Override
    public List<Libro> listarLibros() {
        return List.of();
    }

    @Override
    public Libro buscarLibroPorId(Integer idLibro) {
        return null;
    }

    @Override
    public void guardarLibro(Libro libro) {

    }

    @Override
    public void eliminarLibro(Integer idLibro) {

    }

    @Override
    public boolean verificarExistencia(Integer idLibro) {
        return false;
    }
}
