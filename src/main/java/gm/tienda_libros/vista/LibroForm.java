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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.SwingUtilities;

@Component
public class LibroForm extends JFrame {

    private final LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTextField;
    private JTextField libroTextField;
    private JTextField autorTextField;
    private JTextField editorialTextField;
    private JTextField añoTextField;
    private JTextField precioTextField;
    private JTextField existenciasTextField;
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

        modificarButton.addActionListener(e -> modificarLibro());

        eliminarButton.addActionListener(e -> borrarLibro());

        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
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
        //Los pasamos como argumentos al método setLocation
        setLocation(ejeX, ejeY);
    }

    private void createUIComponents() {

        //Creamos un nuevo JTextField vacío llamado idTextField
        idTextField = new JTextField("");
        idTextField.setVisible(false); //Lo hacemos invisible

        //Creamos una instancia de DefaultTableModel, especificamos 0 filas y 7 columnas en el constructor
        tableModel = new DefaultTableModel(0,7){
            /*
            *Deshabilitamos la edición de las celdas para asegurarnos que todo cambio se haga única y exclusivamente mediante
            * el formulario y de esta manera se vea reflejado en la base de datos*/
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
        //Rellenamos la tabla con la información de la base de datos
        mostrarLibros();
        //LLamamos a los métodos para ajustar el tamaño de las celdas y centrar el texto una vez que haya cargado la vista
        SwingUtilities.invokeLater(this::autoajustarAlContenido);
        SwingUtilities.invokeLater(this::centrarContenido);
    }
    //---------------------------------------------------------------------
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

            //Añadimos la fila al modelo de la tabla
            this.tableModel.addRow(libroFila);
        });
    }

    private void agregarLibro(){
        try {
            /*
            * Obtenemos los valores del título y el autor, si alguno de los dos está vacío mandamos un mensaje
            * de error indicando al usuario que rellene al menos esos dos campos
            * */

            if(libroTextField.getText().isEmpty()){
                mostrarMensaje("Proporciona el título del libro");
                libroTextField.requestFocusInWindow();
                return;
            } else if (autorTextField.getText().isEmpty()){
                mostrarMensaje("Proporciona el autor del libro");
                autorTextField.requestFocusInWindow();
                return;
            }

            //Si ninguno está vacío, a continuación obtenemos los valores de todos los campos y los guardamos en variables
            var titulo = libroTextField.getText();
            var autor = autorTextField.getText();
            var editorial = editorialTextField.getText();
            var año = Year.parse(añoTextField.getText());
            float precio = 0;
            int existencias = 0;

            /*
              * Validamos el precio, si hay un error al tratar de parsearlo a float mandamos un mensaje de error
              * y detenemos la ejecución del método */
            if (!precioTextField.getText().isEmpty()){
                try{
                    precio = Float.parseFloat(precioTextField.getText());
                } catch (NumberFormatException e){
                    mostrarMensaje("El precio introducido no es válido");
                    precioTextField.requestFocusInWindow();
                    return;
                }
            }

            //Realizamos lo mismo con las existencias
            if (!existenciasTextField.getText().isEmpty()){
                try {
                    existencias =  Integer.parseInt(existenciasTextField.getText());
                }  catch (NumberFormatException e){
                    mostrarMensaje("Las existencias introducidas no son válidas");
                    precioTextField.requestFocusInWindow();
                    return;
                }
            }

            //Llamamos al método Builder
            Libro libro = Libro.builderSinId().
                    titulo(titulo).
                    autor(autor).
                    editorial(editorial).
                    año(año).
                    precio(precio).
                    existencias(existencias)
                    .build();

            //Llamamos al método guardarLibro de nuestro servicio
            libroServicio.guardarLibro(libro);
            //mandamos un mensaje de que se agregó correctamente el libro
            mostrarMensaje("Se ha agregado correctamente el libro: " + titulo);
            //Limpiamos el formulario
            limpiarFormulario();
            //Llamamos a mostrarLibros para que se actualice automáticamente la tabla
            mostrarLibros();
        } catch (DateTimeParseException e){
            mostrarMensaje("El año introducido no es válido");
        }
    }

    private void modificarLibro(){

        //Si el id está vacío, es decir no se ha seleccionado un libro de la tabla, mandamos un aviso al usuario
        if (idTextField.getText().isEmpty()){
            mostrarMensaje("No se ha seleccionado ningún libro");
            tablaLibros.requestFocusInWindow();
            //Detenemos la ejecución del método
            return;
        }

        if(libroTextField.getText().isEmpty()){
            mostrarMensaje("Proporciona el título del libro");
            libroTextField.requestFocusInWindow();
            return;
        } else if (autorTextField.getText().isEmpty()){
            mostrarMensaje("Proporciona el autor del libro");
            autorTextField.requestFocusInWindow();
            return;
        }

        try {
            //Empezamos a obtener los valores de los textFields y guardamos en variables locales
            var idLibro = Integer.parseInt(idTextField.getText());
            var titulo = libroTextField.getText();
            var autor = autorTextField.getText();
            var editorial = editorialTextField.getText();
            var año = Year.parse(añoTextField.getText());
            float precio = 0;
            int existencias = 0;

          if (!precioTextField.getText().isEmpty()){

              try{
                  precio = Float.parseFloat(precioTextField.getText());
              } catch (NumberFormatException e) {
                  mostrarMensaje("El precio introducido no es válido");
                  precioTextField.requestFocusInWindow();
                  return;
              }
          }

          if(!existenciasTextField.getText().isEmpty()){
              try {
                  existencias = Integer.parseInt(existenciasTextField.getText());
              } catch (NumberFormatException e) {
                  mostrarMensaje("Las existencias introducidas no son válidas");
                  existenciasTextField.requestFocusInWindow();
                  return;
              }
          }

            //Creamos un nuevo objeto Libro con el contructor vacío y usamos los setters para pasarle la información del formulario
            Libro libro = new Libro();
            libro.setIdLibro(idLibro);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setAño(año);
            libro.setPrecio(precio);
            libro.setExistencias(existencias);

            //Actualizamos los datos
            libroServicio.guardarLibro(libro);

            //Mandamos un mensaje de confirmación
            mostrarMensaje("Se ha modificado correctamente el libro: " + titulo);
            //Limpiamos los campos
            limpiarFormulario();
            //recargamos la tabla
            mostrarLibros();
        } catch (DateTimeParseException e){
            mostrarMensaje("El año introducido no es válido");
            añoTextField.requestFocusInWindow();
            return;
        }
    }

    private void borrarLibro() {

        //Verificamos que el id no esté vacío
        if (idTextField.getText().isEmpty()) {
            mostrarMensaje("No se ha seleccionado ningún libro");
            return;
        }

        //Enviamos un mensaje de confirmación y guardamos la respuesta en una variable
        int confirmacion = JOptionPane.showOptionDialog(this,
                "¿Está seguro de que desea eliminar el libro?",
                "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"Sí", "No"}, JOptionPane.NO_OPTION);

        //Si el usuario respondió no (representado con un 1) detenemos la ejecución del método
        if (confirmacion == 1)
            return;

        //Por el contrario, si respondió con un sí, obtenemos el id del libro de nuestro TextField oculto
        var idLibro = Integer.parseInt(idTextField.getText());

        //Obtenemos también el título
        var titulo = libroTextField.getText();

        //Ahora llamamos al método eliminarlibro de nuestro servicio
        libroServicio.eliminarLibro(idLibro);

        //Limpiamos el formulario
        limpiarFormulario();

        //Actualizamos la vista de la tabla
        mostrarLibros();

        //Mandamos un mensaje de confirmación:
        mostrarMensaje("Se ha eliminado correctamente el libro: "+ titulo);
    }
    //------------------------------------------

    //Limpiar el formulario
    private void limpiarFormulario(){

        idTextField.setText("");
        libroTextField.setText("");
        autorTextField.setText("");
        editorialTextField.setText("");
        añoTextField.setText("");
        precioTextField.setText("");
        existenciasTextField.setText("");
    }

    //Mostrar un mensaje
    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //Cargar la info de la fila seleccionada en nuestro formulario
    private void cargarLibroSeleccionado(){

        //Llamamos a getSelectedRow y lo guardamos en la variable registro
        int registro = tablaLibros.getSelectedRow();
        /*
         * Comprobamos que el registro sea mayor a -1 ya que los índices de las filas empiezan
         * a contar desde 0, */
        if (registro > -1){

            /*Obtenemos el ID del registro en cuestión llamando al método getModel de nuestra tabla
            * y posteriormente a getValueAt, donde pasaremos el índice de la fila y el de la columna
            * como si fueran coordenadas, la fila será la seleccionada por el usuario y la columna 0*/
            String idLibro = tablaLibros.getModel().getValueAt(registro, 0).toString();

            //Establecemos el id obtenido como texto de idTextField
            idTextField.setText(idLibro);

            //Realizamos la misma acción para el resto de campos de la tabla
            String titulo = tablaLibros.getModel().getValueAt(registro, 1).toString();
            libroTextField.setText(titulo);

            String autor = tablaLibros.getModel().getValueAt(registro, 2).toString();
            autorTextField.setText(autor);

            String editorial = tablaLibros.getModel().getValueAt(registro, 3).toString();
            editorialTextField.setText(editorial);

            String año = tablaLibros.getModel().getValueAt(registro, 4).toString();
            añoTextField.setText(año);

            String precio = tablaLibros.getModel().getValueAt(registro, 5).toString();
            precioTextField.setText(precio);

            String existencias = tablaLibros.getModel().getValueAt(registro, 6).toString();
            existenciasTextField.setText(existencias);
        }

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
                    anchoMaximo = Math.max((component).getPreferredSize().width + 10, anchoMaximo);
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
