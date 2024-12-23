package org.gestion.proyectos.modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AsignacionesTareas")
public class AsignacionTarea {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_tarea")
    private Tarea tarea;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_desarrollador")
    private Desarrollador desarrollador;

    @Column(name = "fecha_asignacion")
    private Date fechaAsignacion;

    // Getters y Setters
    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Desarrollador getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
}
