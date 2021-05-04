package com.mycompany.uposports;

import clases.Abono;
import com.vaadin.annotations.PreserveOnRefresh;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
@Title("Abono")
@PreserveOnRefresh  //Si volvemos a cargar la url, no perdemos la sesión

public class AbonoUI extends UI {

    final static List<Abono> listaAbonos = new ArrayList<>();//Creamos una lista de abonos, donde se irán guardando y será compartida por todos los usuarios, necesario recargar la pag para ver cambios de otros usuarios
    Label errorTipo = new Label("La duracion y el coste deben ser numéricos");//Etiqueta error de tipo 
    Label errorCampoVacio = new Label("Los campos no pueden estar vacíos");//Etiqueta derror de campo vacío

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layoutMostrarAbonos = new VerticalLayout();//Creamos un layout vertical
        final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutHLabelabelTitulo = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutH2 = new HorizontalLayout();//Creamos un layout horizontal

        Button crearAbono = new Button("Crear Abono", FontAwesome.PLUS_CIRCLE);//Botón para crear abono
        crearAbono.addClickListener(e -> {//Acción del botón
            crearAbono(vaadinRequest);//Accedemos al método crearAbono
        });

        Label l = new Label("<h1 style='text-weight:bold;text-align:center;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
        Label labelEntidad = new Label("<h2 style='text-weight:bold;margin:0'>Abonos - </h2>", ContentMode.HTML);
        layoutHLabelabelTitulo.addComponent(l);

        Button buttonAbonos = new Button("Abonos", FontAwesome.MONEY);//Botón para acceder a la entidad abono
        buttonAbonos.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Abono");//Accedemos a la entidad abono
        });

        Button buttonCliente = new Button("Clientes", FontAwesome.USERS);//Botón para acceder a la entidad instalaciones
        buttonCliente.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Cliente");//Accedemos a la entidad abono
        });

        Button buttonInstalacion = new Button("Instalaciones", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
        buttonInstalacion.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Instalacion");//Accedemos a la entidad abono
        });

        Button buttonEmpleados = new Button("Empleados", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
        buttonEmpleados.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Empleado");//Accedemos a la entidad abono
        });

        Button buttonMateriales = new Button("Materiales", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
        buttonMateriales.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Material");//Accedemos a la entidad abono
        });

        Button buttonReservas = new Button("Reservas", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
        buttonReservas.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Material");//Accedemos a la entidad abono
        });

        Button buttonLogout = new Button("Cerrar Sesión", FontAwesome.SIGN_OUT);//Botón para cerrar sesión
        buttonLogout.addClickListener(e -> {//Acción del botón
            VaadinSession.getCurrent().getSession().invalidate();//Eliminamos la sesión
            getUI().getPage().setLocation("/");//Accedemos a la página principal
        });

        if (layoutMostrarAbonos.getComponentIndex(layoutH) == -1) {//Si el layout horizontal que contiene los botones no se ha añadido, se añaden
            layoutH.addComponents(layoutHLabelabelTitulo, buttonInstalacion, buttonCliente, buttonAbonos,buttonEmpleados,buttonMateriales,buttonReservas, buttonLogout);//Añadimos los componentes al layout horizontal
            //Le metemos margen y espaciado, para mostrarlo posteriormente.
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, crearAbono);
            layoutMostrarAbonos.addComponents(layoutH, layoutH2);
        }

        Table table = new Table();//Creamos la tabla donde meteremos las instancias

        if (listaAbonos.size() > 0) {//Si hay elementos en la lista de abonos
            //Añadimos las columnas de la tabla
            table.addContainerProperty("Tipo", String.class, "");
            table.addContainerProperty("Duración(meses)", Integer.class, "");
            table.addContainerProperty("Coste(€)", Float.class, "");
            table.addContainerProperty("Editar", Button.class, "");
            table.addContainerProperty("Eliminar", Button.class, "");
            for (int i = 0; i < listaAbonos.size(); i++) {//Mientras haya elementos por recorrer
                Abono abono = listaAbonos.get(i);//Obtenemos el objeto de la lista

                Button buttonModificar = new Button("Modificar", FontAwesome.EDIT);//Creamos el botón modificar
                buttonModificar.addClickListener(e -> {//Acción del botón
                    editarAbono(vaadinRequest, abono);//Método para editar la instalación
                });

                Button buttonEliminar = new Button("Eliminar", FontAwesome.CLOSE);//Creamos el botón eliminar
                buttonEliminar.addClickListener(e -> {//Acción del botón
                    listaAbonos.remove(abono);//Eliminamos el objeto de la lista de instalaciones
                    init(vaadinRequest);//Volvemos a ejecutar el método principal
                    Notification.show("Abono - Tipo: " + abono.getTipo(), "Eliminado con éxito",
                            Notification.Type.TRAY_NOTIFICATION);
                });
                //Añadimos la fila a la tabla
                table.addItem(new Object[]{abono.getTipo(), abono.getDuracion(), abono.getCoste(), buttonModificar, buttonEliminar}, i);

                layoutMostrarAbonos.addComponent(table);//Lo añadimos al layout vertical
            }
        }
        //Le añadimos margen y espciado, para mostrarlo posteriormente.
        layoutMostrarAbonos.setMargin(true);
        layoutMostrarAbonos.setSpacing(true);
        setContent(layoutMostrarAbonos);

    }

    protected void crearAbono(VaadinRequest vaadinRequest) {//Método para crear abonos
        final VerticalLayout layout = new VerticalLayout();//Creamos un vertical layout
        final TextField tipo = new TextField();//Campo para insertar el tipo
        tipo.setCaption("Tipo:");//Texto que se muestra en dicho campo
        tipo.setIcon(FontAwesome.TAG);//Icono
        final TextField duracion = new TextField();//Campo para insertar la duracion
        duracion.setCaption("Duración (meses):");//Texto que se muestra en dicho campo
        duracion.setIcon(FontAwesome.CALENDAR);
        final TextField coste = new TextField();//Campo para insertar el coste
        coste.setCaption("Coste (€):");//Texto que se muestra en dicho campo
        coste.setIcon(FontAwesome.EURO);
        Button buttonRegistrar = new Button("Registrar", FontAwesome.CHECK);//Creamo el botón para registrar 
        buttonRegistrar.addClickListener(e -> {//Acción del botón
            vaadinRequest.setAttribute("tipo", tipo.getValue());//Añadimos en la petición el valor del campo tipo
            vaadinRequest.setAttribute("duracion", duracion.getValue());//Añadimos en la petición el valor del campo duración
            vaadinRequest.setAttribute("coste", coste.getValue());//Añadimos en la petición el valor del campo coste
            if (comprobarDatos(vaadinRequest, layout) == true) {//Se comprueban los datos, y si son correctos...
                registrarAbono(vaadinRequest);//Se envían los datos a registro de abono

                init(vaadinRequest);//Se lanza el método principal
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Abono - Tipo: " + tipo.getValue(), "Registrado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);//Nuevo botón para cancelar
        buttonCancelar.addClickListener(e -> {//Acción del botón
            init(vaadinRequest);//Se lanza el método principal
        });

        layout.addComponents(tipo, duracion, coste, buttonRegistrar, buttonCancelar);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);

    }

    //Exactamente igual que el método de crear abono, con la peculiaridad de que a este se le pasa el objeto abono y se prerellenan los campos con sus valores actuales.
    //Ya se ha comentado en el método anterior que realiza cada línea de código.
    protected void editarAbono(VaadinRequest vaadinRequest, Abono abono) {
        final VerticalLayout layout = new VerticalLayout();
        final TextField tipo = new TextField();
        tipo.setCaption("Tipo:");
        tipo.setValue(abono.getTipo());//Insertamos en el campo el valor del atributo tipo
        tipo.setIcon(FontAwesome.TAG);//Icono
        final TextField duracion = new TextField();
        duracion.setCaption("Duración (meses):");
        duracion.setValue(abono.getDuracion().toString());//Insertamos en el campo el valor del atributo duración
        duracion.setIcon(FontAwesome.CALENDAR);
        final TextField coste = new TextField();
        coste.setCaption("Coste:");
        coste.setValue(abono.getCoste().toString());//Insertamos en el campo el valor del atributo coste
        coste.setIcon(FontAwesome.DOLLAR);
        Button buttonRegistrar = new Button("Modificar", FontAwesome.EDIT);

        buttonRegistrar.addClickListener(e -> {
            vaadinRequest.setAttribute("tipo", tipo.getValue());
            vaadinRequest.setAttribute("duracion", duracion.getValue());
            vaadinRequest.setAttribute("coste", coste.getValue());
            if (comprobarDatos(vaadinRequest, layout) == true) {
                modificarAbono(vaadinRequest, abono);//Se lanza el método modificar abono
                init(vaadinRequest);
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Abono - Tipo: " + tipo.getValue(), "Modificado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);
        buttonCancelar.addClickListener(e -> {
            init(vaadinRequest);
        });

        layout.addComponents(tipo, duracion, coste, buttonRegistrar, buttonCancelar);
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    protected void modificarAbono(VaadinRequest vaadinRequest, Abono abono) {//Método para guardar los datos modificados en memoria, no hay persistencia de momento
        abono.setTipo((String) vaadinRequest.getAttribute("tipo"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        abono.setDuracion(Integer.parseInt((String) vaadinRequest.getAttribute("duracion")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        abono.setCoste(Float.parseFloat((String) vaadinRequest.getAttribute("coste")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
    }

    protected void registrarAbono(VaadinRequest vaadinRequest) {//Método para registrar los datos en memoria, no hay persistencia de momento
        Abono abono = new Abono();//Creamos un nuevo objeto abono
        abono.setTipo((String) vaadinRequest.getAttribute("tipo"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        abono.setDuracion(Integer.parseInt((String) vaadinRequest.getAttribute("duracion")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        abono.setCoste(Float.parseFloat((String) vaadinRequest.getAttribute("coste")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        listaAbonos.add(abono);//Añadimos el objeto a la lista de abonos

    }

    //Método para comprobar los datos introducidos en los formularios
    protected boolean comprobarDatos(VaadinRequest vaadinRequest, VerticalLayout layout) {
        boolean b = false;//Variable booleana inicializada a false
        //Comprobamos si algún campo está vacío
        if ((String) vaadinRequest.getAttribute("tipo") != "" && (String) vaadinRequest.getAttribute("duracion") != "" && (String) vaadinRequest.getAttribute("coste") != "") {
            //Comprobamos si la capacidad es numérica llamando al métdo isInteger
            if (isInteger((String) vaadinRequest.getAttribute("duracion")) == true && isFloat((String) vaadinRequest.getAttribute("coste")) == true) {
                b = true;//Si se satisface todas las condiciones, la variables es true
            } else {//Si la duración o el coste no es numérica
                if (layout.getComponentIndex(errorTipo) == -1) {//Si no se ha añadido el componente al layout

                    layout.addComponentAsFirst(errorTipo);//Añadimos el camponente al layout
                }
                //Notificacion de tipo Warning interactiva para el usuario.
                Notification.show("Error Datos Introducidos", "La duracion y el coste deben ser numéricos",
                        Notification.Type.WARNING_MESSAGE);

            }
        } else {//En caso de campo vacío, mostramos 2 tipos de error uno fijo y otro interactivo (para el proyecto final debatiremos este aspecto)
            if (layout.getComponentIndex(errorCampoVacio) == -1) {//Si no se ha añadido el componente al layout

                layout.addComponentAsFirst(errorCampoVacio);//Añadimos el camponente al layout
            }
            //Notificacion de tipo Warning interactiva para el usuario.
            Notification.show("Campo vacío", "Debe rellenar todos los campos",
                    Notification.Type.WARNING_MESSAGE);
        }
        return b;
    }

    //Método para comprobar valor numérico
    protected static boolean isInteger(String cadena) {
        try {//Intentamos parsear el la cadena a entero, si se satisface, devolvemos true
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {//De lo contrario, captura la excepción y devolvemos false
            return false;
        }
    }

    protected static boolean isFloat(String cadena) {
        try {//Intentamos parsear el la cadena a float, si se satisface, devolvemos true
            Float.parseFloat(cadena);
            return true;
        } catch (NumberFormatException nfe) {//De lo contrario, captura la excepción y devolvemos false
            return false;
        }
    }

    @WebServlet(urlPatterns = "/Abono/*", name = "Abono", asyncSupported = true)
    @VaadinServletConfiguration(ui = AbonoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
