package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {
    static String username;
    @Before
    static void connectedUser() {
        username = session.get("Cliente");
    }
    public static void index() {
        Cliente u = new Cliente("a@b.com", "abc", "juani", "1234I").save();
        Cliente u2 = new Cliente("lola@gmail.com", "lolalolita", "lola", "666666R").save();
        Libro p = new Libro("El señor de los anillos", "J.R. Tolkien", 15, 220).save();
        Libro p2 = new Libro("Harry Potter y la Piedra Filosofal", "J.K Rowling", 20, 250).save();
        Libro p3 = new Libro("Las 999 mujeres de Auschwitz", "Heather Dune Macadam", 22, 250).save();
        Libro p4 = new Libro("IT", "Stephen King", 19, 250).save();
        Compra c = new Compra(2, "07/20/2021").save();
        Compra c2 = new Compra(3, "07/20/2021").save();
        c.comprador = u;
        c.producto = p;
        c.save();
        p.Cist.add(c);
        p.save();
        u.Comp.add(c);
        u.save();
        c2.comprador=u2;
        c2.producto = p2;
        c2.save();
        p2.Cist.add(c2);
        p2.save();
        u2.Comp.add(c2);
        u2.save();
        if(username != null) {
            //renderText("Ususari "+ username + " connectat.");
            //Tenim una sessió oberta amb l'uausi "username"
            renderArgs.put("userConnected", username);
            List<Libro> l = Libro.findAll();
            renderArgs.put("products", l);
            renderTemplate("Application/Llibres.html");
        }
            renderTemplate("Application/MainPage.html");
    }
    public static void registre(){

        renderTemplate("Application/register.html");
    }
    public void RegisterData(String e, String p, String n, String d) {
        // hem de buscar si el client ya existeix abans de crear un client
        Cliente r = Cliente.find("byEmailAndDni", e, d).first();
        if (r == null) {
            Cliente u = new Cliente(e, p, n, d).save();
            //cliente ya registrat
            renderArgs.put("registrocompletado","Ya puedes iniciar sesión");
            renderTemplate("Application/MainPage.html");

        }
        else {
            //cliente ya existe
            renderArgs.put("missatgeErr","El client ja existeix");
            renderTemplate("Application/login.html");

        }
    }
    public static void loginbtn(){

        renderTemplate("Application/login.html");
    }
    public void LoginData(String e, String p) {
        Cliente r = Cliente.find("byEmailAndPassword", e, p).first();
        if (r == null) {
            //les dades del login són incorrectes
            renderArgs.put("missatgeError","El login o el password són incorrectes");
            renderTemplate("Application/MainPage.html");
        } else {
            //iniciem la sessio d'usuari

            session.put("Cliente", r.name);
            renderArgs.put("userConnected",r.name);
            List<Libro> l = Libro.findAll();

            renderArgs.put("products",l);
            renderTemplate("Application/Llibres.html");

        }
    }
    public void LoginAndroid(String e, String p) {
        Cliente r = Cliente.find("byEmailAndPassword", e, p).first();
        if (r != null) {

            renderText("Benvingut!");

        }
        else {

            renderText("Datos incorrectos");
        }
    }

    public void RegisterAndroid(String e, String p, String n, String d) {
        Cliente r = Cliente.find("byEmailAndDni", e, d).first();
        if (r == null) {
            Cliente u = new Cliente(e, p, n, d).save();
            renderText("T'has registrat correctament");

        }
        else {

            renderText("Error al registrarse");
        }
    }

    public static void TancaSessio(){
        session.clear();
        renderTemplate("Application/MainPage.html");
    }
    public static void canvipasswordbtn(){

        renderTemplate("Application/CanviarPassword.html");
    }
    public void CanviPassword(String n, String p, String np) {

        Cliente c = Cliente.find("byNameAndPassword", n,p).first();
        if (c != null) {
            c.password = np;
            c.save();
            renderArgs.put("correcto","!!");
            renderTemplate("Application/Llibres.html");
        } else {
            renderArgs.put("nocorrecto","El nombre o el antiguo password son incorrectos");
            renderTemplate("Application/Llibres.html");
        }

    }

    public void BuscarLibro(String n) {

        Libro l = Libro.find("byNombre", n).first();
        if (l != null) {
            renderXml(l);
        } else {
            renderText("El libro no existe");
        }
    }

    public static void buscarlibrobtn(){

        renderTemplate("Application/BuscarLibro.html");
    }
    public static void comprar1btn(){

        renderTemplate("Application/ComprarLibro1.html");
    }
    public static void comprar2btn(){

        renderTemplate("Application/ComprarLibro2.html");
    }
    public static void comprar3btn(){

        renderTemplate("Application/ComprarLibro3.html");
    }
    public static void comprar4btn(){

        renderTemplate("Application/ComprarLibro4.html");
    }
    public static void sinopsis1(){

        renderTemplate("Application/Sinopsis1.html");
    }
    public static void sinopsis4(){

        renderTemplate("Application/Sinopsis4.html");
    }
    public static void sinopsis2(){

        renderTemplate("Application/Sinopsis2.html");
    }
    public static void sinopsis3(){

        renderTemplate("Application/Sinopsis3.html");
    }
    public void ComprarLibrobusqueda(String n, String email) {

        Libro l = Libro.find("byNombre", n).first();
        if (l != null) {
            if (l.stock > 0) {
                Cliente c = Cliente.find("byEmail", email).first();
                if (c != null) {
                    if (c.saldo >= l.precio) {
                        c.saldo = c.saldo - l.precio;
                        l.stock = l.stock - 1;
                        l.ventas=l.ventas+1;
                        Compra u = new Compra(1, "07/20/2021").save();
                        u.comprador = c;
                        u.producto = l;
                        u.save();
                        l.Cist.add(u);
                        l.save();
                        c.Comp.add(u);
                        c.save();
                        renderText("Compra realitzada amb éxit, saldo restant " + c.saldo + "euros");
                    } else {
                        renderText("No tens suficient saldo, et faltan" + (l.precio - c.saldo) + "euros");
                    }
                }
                else{
                    renderArgs.put("nop","El email introduit és incorrecte");
                    renderTemplate("Application/Llibres.html");
                }
            }
            else {
                renderArgs.put("nope","No queda suficiente stock");
                renderTemplate("Application/Tienda.html");
            }
        }
        else {
            renderArgs.put("nopep","El llibre no existeix");
            renderTemplate("Application/Llibres.html");
        }
    }
    public static void anadirdinero(){

        renderTemplate("Application/AnadirSaldo.html");
    }

    public void AnadirSaldo(String email,Double extrasaldo) {
        Cliente c = Cliente.find("byEmail", email).first();
        if (c != null) {
            c.saldo=c.saldo +extrasaldo;
            c.save();
            renderArgs.put("yas","Saldo añadido correctamente");
            renderTemplate("Application/Llibres.html");
        }
        else{
            renderArgs.put("nok","El email introducido no pertenece a ningún cliente");
            renderTemplate("Application/AnadirSaldo.html");
        }
    }

    public void ElMasVendido(){
        //preguntarle a la profe si esto se harà con un botón, click o algo..
        List<Libro> libros=Libro.findAll();
        Libro l=new Libro();
        l=libros.get(0);
        for (Libro li: libros){
            if(li.ventas>l.ventas){
                l=li;
            }
        }
        renderXml(l);
    }
    //ola

    public void MásBaratoAMásCaro(){

        //List<Libro> libros=Libro.findAll();
        //Libro l=new Libro();
       // l=libros.get(0);
        //for (Libro li: libros){
        //Post.find("order by postDate desc").fetch();
        }
    public static void listarlibrosbtn(){

        renderTemplate("Application/Tienda.html");
    }
    //public void ListarLibros(){
      //  List<Libro> librosall = Libro.findAll();
        //for (Libro lib: librosall){
         //   renderXml(lib);
      //  }
    //}


}
