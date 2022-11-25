package models;
import javax.persistence.*;

import play.db.jpa.*;
@Entity
public class Compra extends Model {
    public int cantidad;
    public String fecha;
   @ManyToOne
   public Cliente comprador;

    @ManyToOne
    public Libro producto;

    public Compra(int cantidad, String fecha) {
        this.cantidad=cantidad;
        this.fecha=fecha;

    }
}
