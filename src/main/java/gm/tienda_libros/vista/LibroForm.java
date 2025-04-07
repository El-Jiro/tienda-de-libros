package gm.tienda_libros.vista;

import gm.tienda_libros.modelo.Libro;
import gm.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;
import java.util.List;
import javax.swing.SwingUtilities;

@Component
public class LibroForm extends JFrame {

    private final LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField libroTextField;
    private JTextField autorTextField;
    private JTextField editorialTextField;
    private JTextField añoTextField;
    private JTextField precioTextField;
    private JTextField existenciastextField;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tableModel;

    //-----------------Constructor e inicializadores de la interfaz gráfica-----------------------
    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();

        agregarButton.addActionListener(e -> agregarLibro());

        modificarButton.addActionListener(e -> {

        });

        eliminarButton.addActionListener(e -> {

        });
    }

    private void iniciarForma(){
        //Establecemos nuestro JPanel como panel de contenido
        setContentPane(panel);
        //Dictamos que salga de la aplicación al cerrar la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Lo hacemos visible
        setVisible(true);
        //Definimos el tamaño de la ventana en 900 x 600 px
        setSize(1200, 800);
        /*Esto es para centrar la ventana*/
        //Obtenemos la información del sistema
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //Obtenemos las dimensiones de nuestra pantalla
        Dimension tamañoPantalla = toolkit.getScreenSize();
        //Calculamos los ejes X e Y restándole al alto y ancho de nuestra pantalla
        //el alto y ancho de nuestra ventana y dividiendo el resultado entre 2
        int ejeX = (tamañoPantalla.width - getWidth())/2;
        int ejeY = (tamañoPantalla.height - getHeight())/2;
        //Los pasamos como argumentos al metodo setLocation
        setLocation(ejeX, ejeY);
    }

    private void createUIComponents() {

        //Creamos una instancia de DefaultTableModel, especificamos 0 filas y 7 columnas en el constructor
        tableModel = new DefaultTableModel(0,7){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //Creamos un array de Strings para los nombres o encabezados de las columnas
        String[] encabezados = {"ID", "Título", "Autor", "Editorial", "Año", "Precio", "Existencias"};
        //Llamamos al setColumnIdentifiers y le pasamos nuestro array
        this.tableModel.setColumnIdentifiers(encabezados);
        //Inicializamos el objeto tablaLibros como instancia de JTable y le pasamos tableModel en el constructor
        tablaLibros = new JTable(tableModel);
        mostrarLibros();
        SwingUtilities.invokeLater(this::autoajustarAlContenido);
        SwingUtilities.invokeLater(this::centrarContenido);
    }

    //-----------Métodos del CRUD--------------
    private void mostrarLibros(){
        //Limpiar la tabla
        tableModel.setRowCount(0);
        //Obtener los libros
        List<Libro> libros = libroServicio.listarLibros();
        //Iteramos sobre la lista con un forEach
        libros.forEach(libro -> {
            //Obtenemos los atributos del libro con los getters y los guardamos en un Array de tipo Object
            Object [] libroFila = {
                    libro.getIdLibro(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getEditorial(),
                    libro.getAño(),
                    libro.getPrecio(),
                    libro.getExistencias()
            };

            //Añadimos la fila a la tabla
            this.tableModel.addRow(libroFila);
        });
    }

    private void agregarLibro(){
        /*
        Obtenemos los valores del título y el autor, si alguno de los dos está vacío mandamos un mensaje
        de error indicando al usuario que rellene al menos esos dos campos
        */
        if(libroTextField.getText().isEmpty()){
            mostrarMensaje("Proporciona el título del libro");
            libroTextField.requestFocusInWindow();
            return;
        } else if (autorTextField.getText().isEmpty()){
            mostrarMensaje("Proporciona el autor del libro");
            return;
        }

        //Si ninguno está vacío, a continuación obtenemos los valores de todos los campos y los guardamos en variables
        var titulo = libroTextField.getText();
        var autor = autorTextField.getText();
        var editorial = editorialTextField.getText();
        var año = Year.parse(añoTextField.getText());
        var precio = Float.parseFloat(precioTextField.getText());
        var existencias = Integer.parseInt(existenciastextField.getText());

        //Llamamos al metodo Builder
        Libro libro = Libro.builderSinId().
                titulo(titulo).
                autor(autor).
                editorial(editorial).
                año(año).
                precio(precio).
                existencias(existencias)
                .build();

        //Llamamos al metodo guardarLibro de nuestro servicio
        libroServicio.guardarLibro(libro);
        //mandamos un mensaje de que se agregó correctamente el libro
        mostrarMensaje("Se ha agregado correctamente el libro: " + titulo);
        //Limpiamos el formulario
        limpiarFormulario();
        //Llamamos a mostrarLibros para que se actualice automáticamente la tabla
        mostrarLibros();
    }

    //Limpiar el formulario
    private void limpiarFormulario(){
        libroTextField.setText("");
        autorTextField.setText("");
        editorialTextField.setText("");
        añoTextField.setText("");
        precioTextField.setText("");
        existenciastextField.setText("");
    }
    //Mostrar un mensaje
    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }
    //---------Ajustar tamaño de las columnas y centrar el texto en las celdas------------
    private void autoajustarAlContenido(){
        tablaLibros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        for(int columna = 0; columna<tablaLibros.getColumnCount(); columna++){
            TableColumn column = tablaLibros.getColumnModel().getColumn(columna);
            int anchoMaximo = 0;

            for(int fila = 0; fila < tablaLibros.getRowCount(); fila++){
                TableCellRenderer renderer = tablaLibros.getCellRenderer(fila, columna);
                java.awt.Component component =  tablaLibros.prepareRenderer(renderer, fila, columna);
                if (component instanceof JComponent) {
                    anchoMaximo = Math.max(((JComponent) component).getPreferredSize().width + 10, anchoMaximo);
                }
            }

            column.setPreferredWidth(anchoMaximo);
        }
    }

    private void centrarContenido(){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int columna = 0; columna<tablaLibros.getColumnCount(); columna++){
            tablaLibros.getColumnModel().getColumn(columna).setCellRenderer(centerRenderer);
        }
    }


}
