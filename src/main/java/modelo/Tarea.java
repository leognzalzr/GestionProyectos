package modelo;

import jakarta.persistence.*;

@Entity
@Table
public class Tarea {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private int id;

  @Column
  private String descripcion;

  @Column
  private String estado;

  @ManyToOne
  private Desarrollador desarrollador;

  public Desarrollador getDesarrollador() {
    return desarrollador;
  }

  public void setDesarrollador(Desarrollador desarrollador) {
    this.desarrollador = desarrollador;
  }

  public Tarea(String descripcion, String estado, Desarrollador desarrollador) {
    this.descripcion = descripcion;
    this.estado = estado;
    this.desarrollador = desarrollador;
  }

  public int getId() {
    return id;
  }

  public void setId(int idTarea) {
    this.id = idTarea;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public Tarea() {
  }
}
