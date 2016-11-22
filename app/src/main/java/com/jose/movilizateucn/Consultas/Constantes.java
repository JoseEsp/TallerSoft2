package com.jose.movilizateucn.Consultas;

public class Constantes {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta característica.
     */
    private static final String PUERTO_HOST = "3306";

    /**
     * Dirección IP
     */
    private static final String IP = "http://movilizate.webcindario.com";
    /**
     * URLs del Web Service
     */
    public static final String INSERTUSUARIO = IP + "/consultas/insertarUsuario.php";
    public static final String GETUSUARIOBYRUT = IP + "/consultas/obtenerUsuario.php";
    public static final String GETCHOFERBYRUT = IP + "/consultas/obtenerChofer.php";
    public static final String GETPASAJEROBYRUT = IP + "/consultas/obtenerPasajero.php";
    public static final String GETLOGIN = IP + "/consultas/obtenerUsuarioLogueado.php";
    public static final String UPDATEFECHAINICIO = IP + "/consultas/actualizarFechaInicioConexion.php";
    public static final String UPDATEFECHAFIN = IP + "/consultas/actualizarFechaFinConexion.php";
    public static final String OBTENERVIAJES = IP + "/consultas/obtenerViajes.php";
    public static final String OBTENERCALIFICACIONVIAJES = IP + "/consultas/obtenerCalificacionViajes.php";
    public static final String RUTAOPTIMAGENERARSOLICITUD = "https://maps.googleapis.com/maps/";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

}