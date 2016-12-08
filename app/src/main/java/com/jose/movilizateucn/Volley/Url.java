package com.jose.movilizateucn.Volley;

public class Url {

    private static final String BASEURL = "http://movilizate.webcindario.com/";
    //private static final String BASEURL = "http://170.239.84.252/";

    /**
     * URLs de consultas del Web Service
     */
    public static final String RUTAOPTIMA = "https://maps.googleapis.com/maps/";

    public static final String OBTENERUSUARIOLOGUEADO = BASEURL + "consultas/obtenerUsuarioLogueado.php";
    public static final String REGISTRARUSUARIO = BASEURL + "consultas/registrarUsuario.php";
    public static final String UPDATEFECHAFIN = BASEURL + "consultas/actualizarFechaFinConexion.php";
    public static final String OBTENERHISTVIAJES = BASEURL + "consultas/obtenerHistViajes.php";
    public static final String OBTENERCALIFICACIONVIAJES = BASEURL + "consultas/obtenerCalificacionViajes.php";
    public static final String GENERARSOLICITUD = BASEURL + "consultas/generarSolicitud.php";
    public static final String DESACTIVARSOLICITUD = BASEURL + "consultas/desactivarSolicitud.php";
    public static final String ACTIVARSOLICITUD = BASEURL + "consultas/activarSolicitud.php";
    public static final String GENERARVIAJE = BASEURL + "consultas/generarViaje.php";
    public static final String OBTENERSOLICITUDESACTIVAS = BASEURL + "consultas/obtenerSolicitudesActivas.php";
    public static final String GENERARFEEDBACK = BASEURL + "consultas/generarFeedback.php";
}