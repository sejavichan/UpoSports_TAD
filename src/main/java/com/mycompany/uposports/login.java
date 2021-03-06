package com.mycompany.uposports;

import bbdd.AbonoDAO;
import bbdd.AnuncianteDAO;
import bbdd.ClienteDAO;
import bbdd.EmpleadoDAO;
import bbdd.InstalacionDAO;
import bbdd.MaterialDAO;
import bbdd.ReservaDAO;
import clases.Abono;
import clases.Anunciante;
import clases.Cliente;
import clases.Empleado;
import clases.Instalacion;
import clases.Material;
import clases.Reserva;
import log.Broadcaster;
import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Theme("mytheme")
@Title("Login")
@Push(PushMode.AUTOMATIC) //Envía push en modo automático al navegador (es el por defecto)

public class login extends UI implements Broadcaster.BroadcastListener {
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        WrappedSession session = getSession().getSession();//Creamos una sesión
        if (session.getAttribute("nombreUsuario") != null) {//Si ya tenemos una sesión en el navegador
            mostrarEntidades(session);//mostramos el menú de las entidades
        } else {
            try {
                //Si no hay sesión en el navegador, se muestra el campo para acceder
                cargarDatos();
            } catch (ParseException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
            final VerticalLayout layout = new VerticalLayout();//Layout vertical

            final TextField name = new TextField();//TextField para introducir nombre de usuario
            name.setCaption("Inserte su DNI para acceder al sistema:");
            name.setIcon(FontAwesome.KEY);//Icono

            Button button = new Button("Acceder", FontAwesome.SIGN_IN);//Botón para acceder al sistema
            button.addClickListener(e -> {//Acción que accede al sistema
                if (name.getValue() != "") {//Si el dni no es vacio
                    try {
                        Empleado emp = EmpleadoDAO.buscarEmpleado(name.getValue());
                        if (emp != null) {//El empleado existe
                            session.setAttribute("nombreUsuario", emp.getNombre() + " " + emp.getApellidos());//Incorporamos el nombre de usuario como atributo de la sesion
                            Broadcaster.broadcast(name.getValue() + " - " + emp.getNombre() + " " + emp.getApellidos());//mandamos como mensaje el nombre de usuario
                            mostrarEntidades(session);//Lanzamos el metodo mostrarEntidades
                        } else {
                            //Notificacion de tipo Warning interactiva para el usuario.
                            Notification.show("DNI Incorrecto", "Inserte un DNI válido",
                                    Notification.Type.ERROR_MESSAGE);
                        }
                        
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } else {
                    //Notificacion de tipo Warning interactiva para el usuario.
                    Notification.show("Campo vacío", "Inserte su DNI",
                            Notification.Type.WARNING_MESSAGE);
                }
                
            });
            
            layout.addComponents(name, button);//Añadimos los componentes al layout
            //Le metemos el espaciado y margen
            layout.setMargin(true);
            layout.setSpacing(true);
            
            setContent(layout);//Mostramos el contenido del layout
        }
    }

    //Mostramos los distintos tipos de entidades que tenemos para acceder
    protected void mostrarEntidades(WrappedSession session) {
        final HorizontalLayout layoutEntidades = new HorizontalLayout();//Creamo layout horizontal
        final VerticalLayout layoutVentana = new VerticalLayout();//Layout vertical

        Label saludo = new Label("<h1 style='text-weight:bold;margin:0'>UPOSports - Bienvenido/a " + session.getAttribute("nombreUsuario") + " </h1>"
                + "<br/><br/><h3> Seleccione una opción: </h3>", ContentMode.HTML);

        //MENU INICIO
        Button buttonAbonos = new Button("Abonos", FontAwesome.MONEY);
        Button buttonInstalaciones = new Button("Instalaciones", FontAwesome.BUILDING);
        Button buttonClientes = new Button("Clientes", FontAwesome.MALE); //Boton para ir a la página de gestión de clientes
        Button buttonEmpleado = new Button("Empleados", FontAwesome.USERS);
        Button buttonMaterial = new Button("Materiales", FontAwesome.ARCHIVE);
        Button buttonReserva = new Button("Reservas", FontAwesome.CALENDAR);
        Button buttonAnunciantes = new Button("Anunciantes", FontAwesome.BELL);
        
        Button buttonLogout = new Button("Cerrar Sesión", FontAwesome.SIGN_OUT);
        
        buttonAbonos.addClickListener(a -> {//Acción del botón
            getUI().getPage().setLocation("/Abono");//Redirección a página abono
        });
        
        buttonInstalaciones.addClickListener(a -> {//Acción del botón
            getUI().getPage().setLocation("/Instalacion");//Redirección a página instalación
        });
        
        buttonClientes.addClickListener(e -> {  //Establecemos lo que hace el boton clientes
            getUI().getPage().setLocation("/Cliente"); //Redirige a la clase de gestionar clientes
        });
        
        buttonEmpleado.addClickListener(e -> {  //Establecemos lo que hace el boton clientes
            getUI().getPage().setLocation("/Empleado"); //Redirige a la clase de gestionar clientes
        });
        
        buttonMaterial.addClickListener(e -> {  //Establecemos lo que hace el boton clientes
            getUI().getPage().setLocation("/Material"); //Redirige a la clase de gestionar clientes
        });
        
        buttonReserva.addClickListener(e -> {  //Establecemos lo que hace el boton clientes
            getUI().getPage().setLocation("/Reserva"); //Redirige a la clase de gestionar clientes
        });
        
        buttonAnunciantes.addClickListener(e -> {  //Establecemos lo que hace el boton clientes
            getUI().getPage().setLocation("/Anunciante"); //Redirige a la clase de gestionar clientes
        });
        
        buttonLogout.addClickListener(a -> {//Acción del botón logout
            session.invalidate();//Se destruye la sesion
            getUI().getPage().setLocation("/");//Página principal (login)
        });
        //FIN MENU INICIO

        //Añadimos los componentes al layout y le ponemos margen y espaciado
        layoutEntidades.addComponents(buttonReserva, buttonClientes, buttonAbonos, buttonInstalaciones, buttonMaterial, buttonEmpleado, buttonAnunciantes, buttonLogout);
        layoutVentana.addComponents(saludo, layoutEntidades);
        layoutEntidades.setMargin(true);
        layoutEntidades.setSpacing(true);
        layoutVentana.setMargin(true);
        layoutVentana.setSpacing(true);
        
        setContent(layoutVentana);//Mostramos le contenido del layout

    }
    
    public void cargarDatos() throws ParseException {
        Empleado empleado = new Empleado();
        empleado.setNombre("Alberto");
        empleado.setApellidos("González Rivas");
        empleado.setDni("28983187K");
        empleado.setTelefono(695525878);
        try {
            if (EmpleadoDAO.buscarEmpleado(empleado.getDni()) == null) {
                EmpleadoDAO.insertarEmpleado(empleado);
                Abono abono = new Abono("Trimestral", 3, 70.0);
                AbonoDAO.insertarAbono(abono);
                Cliente cliente = new Cliente("49129515L", "Javier", "Gimenez Figueroa", "682468724", "45374", abono);
                ClienteDAO.creaCliente(cliente);
                Instalacion instalacion = new Instalacion("Pista de futbol", "Pista de futbol 11 con cesped artificial", 200);
                InstalacionDAO.insertarInstalacion(instalacion);
                Material material = new Material("Porteria", "Porteria de futbol", 2, instalacion);
                MaterialDAO.creaMaterial(material);                
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date inicioReserva = dateFormat.parse("05/07/2021 19:30:00");
                Date finReserva = dateFormat.parse("05/07/2021 21:00:00");
                Reserva reserva = new Reserva(inicioReserva, finReserva, cliente, instalacion);
                ReservaDAO.creaReserva(reserva);
                Anunciante anunciante = new Anunciante("Industrias Manolo", 465.95, inicioReserva, finReserva);
                AnuncianteDAO.insertarAnunciante(anunciante);

            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Se ejecuta el método access para el log que recibe el push (en este caso no realiza nada, se realiza en el log)
    @Override
    public void receiveBroadcast(final String message) {
        access(new Runnable() {
            @Override
            public void run() {
            }
        });
    }
    
    @WebServlet(urlPatterns = "/*", name = "Login", asyncSupported = true)
    @VaadinServletConfiguration(ui = login.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
