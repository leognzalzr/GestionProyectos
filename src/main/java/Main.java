import modelos.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.util.*;

public class Main {
  public static void main(String[] args) {
      SessionFactory factory = new Configuration()
      .configure("hibernate.cfg.xml")
      .addAnnotatedClass(Persona.class)
      .addAnnotatedClass(Desarrollador.class)
      .addAnnotatedClass(Cliente.class)
      .addAnnotatedClass(Equipo.class)
      .addAnnotatedClass(Proyecto.class)
      .addAnnotatedClass(Tarea.class)
      .buildSessionFactory();

      JFrame frame = new JFrame("Gestión Proyectos");
      frame.setContentPane(new vistas.Equipo(factory).getPanel());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      // uwu
  }
}