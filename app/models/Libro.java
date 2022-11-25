package models;
import javax.persistence.*;

import play.db.jpa.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Libro extends Model {
    public String nombre;
    public String autor;
    public float precio;
    public int stock;
    public int ventas;
    @OneToMany (mappedBy = "producto")
    public List<Compra> Cist= new ArrayList<Compra>();
    public Libro(String product, String autor, float price, int stock) {
        this.nombre=product;
        this.autor=autor;
        this.precio=price;
        this.stock=stock;
        this.ventas=0;
    }
    public Libro() {

    }
}
