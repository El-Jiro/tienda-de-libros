package gm.tienda_libros.vista;

import gm.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import javax.swing.SwingUtilities;

@Component
public class LibroForm extends JFrame {

    private final LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField libroTextField;
    private JTextField autorTextField;
    private JTextField editorialTextField;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel precioTextField;
    private JTextField existenciastextField;
    private JPanel libroPanel;
    private JLabel autorPanel;
    private JPanel editorialPanel;
    private JPanel añoPanel;
    private JPanel precioPanel;
    private JPanel existenciasPanel;
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
        setSize(1100, 700);
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

        //tablaLibros.getTableHeader().setResizingAllowed(true);
        //tablaLibros.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }


    private void mostrarLibros(){
        //Limpiar la tabla
        tableModel.setRowCount(0);
        //Obtener los libros
        var libros = libroServicio.listarLibros();
        libros.forEach(libro -> {
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

    private void autoajustarAlContenido(){
        tablaLibros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        for(int columna = 0; columna<tablaLibros.getColumnCount(); columna++){
            TableColumn column = tablaLibros.getColumnModel().getColumn(columna);
            int anchoMaximo = 0;

            for(int fila = 0; fila < tablaLibros.getRowCount(); fila++){
                TableCellRenderer renderer = tablaLibros.getCellRenderer(fila, columna);
                java.awt.Component component =  tablaLibros.prepareRenderer(renderer, fila, columna);
                if (component instanceof JComponent) {
                    anchoMaximo = Math.max(((JComponent) component).getPreferredSize().width + 5, anchoMaximo);
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
