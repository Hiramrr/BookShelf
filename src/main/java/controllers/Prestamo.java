package controllers;

public class Prestamo {
    int id;
    String fecha_solicitud;
    String fecha_devolucion;
    int estado;
    int id_usuario;
    int id_libro;

    public Prestamo(int id, String fecha_solicitud, String fecha_devolucion, int estado, int id_usuario, int id_libro) {
        this.id = id;
        this.fecha_solicitud = fecha_solicitud;
        this.fecha_devolucion = fecha_devolucion;
        this.estado = estado;
        this.id_usuario = id_usuario;
        this.id_libro = id_libro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha_solicitud() {
        return fecha_solicitud;
    }

    public void setFecha_solicitud(String fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public String getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(String fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_libro() {
        return id_libro;
    }

    public void setId_libro(int id_libro) {
        this.id_libro = id_libro;
    }
}
