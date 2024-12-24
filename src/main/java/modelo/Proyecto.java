package modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table
public class Proyecto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private String nombre;

  @Column
  private String descripcion;

  @Column
  private Date fechaFin;

  @ManyToOne
  private Equipo equipo;

  @ManyToOne
  private Cliente cliente;

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public Proyecto() {
  }

  public Proyecto(String nombre, String descripcion, Date fechaFin, Equipo equipo) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.fechaFin = fechaFin;
    this.equipo = equipo;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Date getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(Date fechaFinEstimada) {
    this.fechaFin = fechaFinEstimada;
  }

  public Equipo getEquipo() {
    return equipo;
  }

  public void setEquipo(Equipo equipo) {
    this.equipo = equipo;
  }
}
