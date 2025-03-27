package gm.tienda_libros.vista;

import gm.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

public class LibroForm extends JFrame {

    private LibroServicio libroServicio;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
    }
}
