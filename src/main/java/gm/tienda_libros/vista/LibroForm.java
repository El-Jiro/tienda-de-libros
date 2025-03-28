package gm.tienda_libros.vista;

import gm.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

public class LibroForm extends JFrame {

    private LibroServicio libroServicio;
    private JPanel panel;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
    }

    private void iniciarForma(){
        //Establecemos nuestro JPanel como panel de contenido
        setContentPane(panel);
        //Dictamos que salga de la aplicación al cerrar la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Lo hacemos visible
        setVisible(true);
        //Definimos el tamaño de la ventana en 900 x 700 px
        setSize(900, 700);
        /*Esto es para centrar la ventana*/
        //Obtenemos la información del sistema
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //Obtenemos las dimensiones de nuestra pantalla
        Dimension tamañoPantalla = toolkit.getScreenSize();
        //Calculamos los ejes x e y restándole al alto y ancho de nuestra pantalla
        //las dimensiones de nuestra ventana dividas entre 2
        int ejeX = (tamañoPantalla.width - (getWidth()/2));
        int ejeY = (tamañoPantalla.height -(getHeight()/2));
        //Los pasamos como argumentos al método setLocation
        setLocation(ejeX, ejeY);
    }
}
