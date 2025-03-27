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
        List<Libro> libros = libroRepositorio.findAll();
        return libros;
    }

    @Override
    public Libro buscarLibroPorId(Integer idLibro) {
        Libro libro = libroRepositorio.findById(idLibro).orElse(null);
        return libro;
    }

    @Override
    public void guardarLibro(Libro libro) {
        libroRepositorio.save(libro);
    }

    @Override
    public void eliminarLibro(Integer idLibro) {
        libroRepositorio.deleteById(idLibro);
    }

    @Override
    public boolean verificarExistencia(Integer idLibro) {
        var exists = libroRepositorio.existsById(idLibro);
        return exists;
    }
}
