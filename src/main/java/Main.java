import org.gestion.proyectos.modelo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Equipo.class)
                .buildSessionFactory();
            Equipo equipo = new Equipo();
            equipo.setIdEquipo(1);
            equipo.setNombre("Harry Potter");
        // Crear algunos desarrolladores
        Desarrollador dev1 = new Desarrollador("Juan Pérez", "Backend", 5, "Senior");
        Desarrollador dev2 = new Desarrollador("Ana Gómez", "Frontend", 3, "Junior");

        // Crear una lista de desarrolladores
        List<Desarrollador> desarrolladores = new ArrayList<>();
        desarrolladores.add(dev1);
        desarrolladores.add(dev2);

        equipo.setDesarrolladores(desarrolladores);

        Proyecto p1 = new Proyecto("Desarrollo Web", "Desarrollo de una aplicación web", "2024-01-01", "2024-06-01");
        Proyecto p2 = new Proyecto("Sistema de Gestión", "Desarrollo de un sistema para gestionar proyectos", "2024-02-01", "2024-07-01");

        // Crear una lista de proyectos
        List<Proyecto> proyectos = new ArrayList<>();
        proyectos.add(p1);
        proyectos.add(p2);
        equipo.setProyectos(proyectos);
    }
}