package gm.tienda_libros.vista;

import gm.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Component
public class LibroForm extends JFrame {

    private LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private DefaultTableModel tableModel;

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
        //Definimos el tamaño de la ventana en 900 x 600 px
        setSize(900, 600);
        /*Esto es para centrar la ventana*/
        //Obtenemos la información del sistema
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //Obtenemos las dimensiones de nuestra pantalla
        Dimension tamañoPantalla = toolkit.getScreenSize();
        //Calculamos los ejes X e Y restándole al alto y ancho de nuestra pantalla
        //el alto y ancho de nuestra ventana y dividiendo el resultado entre 2
        int ejeX = (tamañoPantalla.width - getWidth())/2;
        int ejeY = (tamañoPantalla.height - getHeight())/2;
        //Los pasamos como argumentos al método setLocation
        setLocation(ejeX, ejeY);
    }

    private void createUIComponents() {

        //Creamos una instancia de DefaultTableModel, especificamos 0 filas y 5 columnas en el constructor
        tableModel = new DefaultTableModel(0,5);
        //Creamos un array de Strings para los nombres o encabezados de las columnas
        String[] encabezados = {"Título", "Autor", "Editorial", "Año", "Precio", "Existencias"};
        //Llamamos al setColumnIdentifiers y le pasamos nuestro array
        this.tableModel.setColumnIdentifiers(encabezados);
        //Inicializamos el objeto tablaLibros como instancia de JTable y le pasamos tableModel en el constructor
        tablaLibros = new JTable(tableModel);
    }
}
