package modelo;

import jakarta.persistence.*;

@Entity
@Table
public class Cliente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  public Cliente(String nombreInstitucion, String direccion, String telefono) {
    NombreInstitucion = nombreInstitucion;
    Direccion = direccion;
    Telefono = telefono;
  }

  public int getId() {
    return id;
  }

  @Column
  private String NombreInstitucion;
  @Column
  private String Direccion;
  @Column
  private String Telefono;

  public String getNombreInstitucion() {
    return NombreInstitucion;
  }

  public void setNombreInstitucion(String nombreInstitucion) {
    NombreInstitucion = nombreInstitucion;
  }

  public String getDireccion() {
    return Direccion;
  }

  public void setDireccion(String direccion) {
    Direccion = direccion;
  }

  public String getTelefono() {
    return Telefono;
  }

  public void setTelefono(String telefono) {
    Telefono = telefono;
  }

  public Cliente() {
  }
}
