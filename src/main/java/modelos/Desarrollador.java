package modelos;
import jakarta.persistence.*;

@Entity
public class Desarrollador extends Persona {
  @Column
  private int experiencia;

  @Column
  private String especialidad;

  @ManyToOne
  private Equipo equipo;

  public Desarrollador() {
    super();
  }

  public Desarrollador(String especialidad, String nombre, int experiencia, Equipo equipo) {
    super(nombre);
    this.experiencia = experiencia;
    this.especialidad = especialidad;
    this.equipo = equipo;
  }

  public int getExperiencia() {
    return experiencia;
  }

  public void setExperiencia(int experiencia) {
    this.experiencia = experiencia;
  }

  public String getEspecialidad() {
    return especialidad;
  }

  public void setEspecialidad(String especialidad) {
    this.especialidad = especialidad;
  }

  public Equipo getEquipo() {
    return equipo;
  }

  public void setEquipo(Equipo equipo) {
    this.equipo = equipo;
  }
}
