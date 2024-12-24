package modelo;

import jakarta.persistence.*;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private int id;

  @Column
  private String nombre;

  public Persona() {
  }

  public Persona(String nombre) {
    this.nombre = nombre;
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
}
