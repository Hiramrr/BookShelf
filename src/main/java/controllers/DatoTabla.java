package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Esta clase solo existe porque TableColumn requiere 2 elementos jaja, solo es para ponerle un valor y que se muestre
public class DatoTabla {
    private final StringProperty valor;

    public DatoTabla(String valor) {
        this.valor = new SimpleStringProperty(valor);
    }

    public String getValor() {
        return valor.get();
    }

    public void setValor(String valor) {
        this.valor.set(valor);
    }

    public StringProperty valorProperty() {
        return valor;
    }
} 