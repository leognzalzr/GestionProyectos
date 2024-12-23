
package org.gestion.proyectos.modelo;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Desarrollador")  // Especifica el valor que identificará a los desarrolladores en la tabla
public class Desarrollador extends Persona {

    @Column(name = "experiencia")
    private int experiencia;  // Atributo adicional: años de experiencia del desarrollador

    @Column(name = "nivel")
    private String nivel; // Atributo adicional: nivel del desarrollador (junior, senior, etc.)

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipo equipo;

    // Constructor por defecto
    public Desarrollador() {
        super();  // Llama al constructor de la clase base Persona
    }

    // Constructor con parámetros
    public Desarrollador(String nombre, String especialidad, int experiencia, String nivel) {
        super();  // Llama al constructor de la clase base Persona
        this.nivel = nivel;
        this.experiencia = experiencia;
    }

    // Getters y Setters
    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
