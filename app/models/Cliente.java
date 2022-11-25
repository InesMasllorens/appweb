package models;
import javax.persistence.*;
import java.util.*;
import play.db.jpa.*;

@Entity
public class Cliente extends Model {

    public double saldo;
    public String email;
    public String password;
    public String name;
    public String dni;

    @OneToMany (mappedBy = "comprador")
    public List<Compra> Comp= new ArrayList<Compra>();
    public Cliente(String email, String password, String name,String dni) {
        this.email=email;
        this.password=password;
        this.name=name;
        this.dni=dni;
        this.saldo= 300;

    }


}
