package controllers;

import java.util.List;

public class Libro {
    int id;
    String titulo;
    String autor;
    String editorial;
    int num_copias;
    String sinopsis_;
    List<Categoria> categoria;


    public Libro(int id, String titulo, String autor, String editorial, int num_copias, String sinopsis_, List<Categoria> categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.num_copias = num_copias;
        this.sinopsis_ = sinopsis_;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum_copias() {
        return num_copias;
    }

    public void setNum_copias(int num_copias) {
        this.num_copias = num_copias;
    }

    public String getSinopsis_() {
        return sinopsis_;
    }

    public void setSinopsis_(String sinopsis_) {
        this.sinopsis_ = sinopsis_;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Categoria> getCategoria() {
        return categoria;
    }

    public void setCategoria(List<Categoria> categoria) {
        this.categoria = categoria;
    }
}
