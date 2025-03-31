package gm.tienda_libros;

import gm.tienda_libros.vista.LibroForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class TiendaLibrosApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext springContext = new SpringApplicationBuilder(TiendaLibrosApplication.class).
				headless(false).web(WebApplicationType.NONE).run(args);

		//Código para cargar LibroForm
		EventQueue.invokeLater(()->{
			//obtenemos el objeto form a través de Spring
			LibroForm libroForm = springContext.getBean(LibroForm.class);
			//Lo hacemos visible
			libroForm.setVisible(true);
		});
	}

}
