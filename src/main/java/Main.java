import org.gestion.proyectos.modelo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.*;

public class Main {
  public static void main(String[] args) {
      try (SessionFactory factory = new Configuration()
              .configure("hibernate.cfg.xml")
              .addAnnotatedClass(Persona.class)
              .addAnnotatedClass(Desarrollador.class)
              .addAnnotatedClass(Cliente.class)
              .addAnnotatedClass(Equipo.class)
              .addAnnotatedClass(Proyecto.class)
              .addAnnotatedClass(Tarea.class)
              .buildSessionFactory()) {

          try (Session session = factory.openSession()) {
              session.beginTransaction();
              Equipo e = new Equipo();
              e.setNombre("equipo 1");
              session.persist(e);
              session.getTransaction().commit();

              session.beginTransaction();
              Desarrollador d = new Desarrollador();
              d.setNombre("pedro");
              d.setExperiencia(1);
              d.setEspecialidad("INGENIERO");
              d.setEquipo(e);
              session.persist(d);
              session.getTransaction().commit();

              session.beginTransaction();
              Cliente c = new Cliente();
              c.setNombreInstitucion("farmacia");
              c.setTelefono("555");
              c.setDireccion("chill de cojones");
              session.persist(c);
              session.getTransaction().commit();

              session.beginTransaction();
              Proyecto p = new Proyecto();
              p.setNombre("sistema de gestion de remedios");
              p.setFechaFin(new Date());
              p.setDescripcion("hola");
              p.setCliente(c);
              session.persist(p);
              session.getTransaction().commit();

              session.beginTransaction();
              Tarea t = new Tarea();
              t.setDescripcion("mejorar el rendimiento");
              t.setEstado("TESTING");
              t.setDesarrollador(d);
              session.persist(t);
              session.getTransaction().commit();
          }
      }
  }
}
