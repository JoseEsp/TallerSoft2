package com.jose.movilizateucn.Volley;

public class Url {

    private static final String BASEURL = "http://movilizate.webcindario.com/";
    /**
     * URLs de consultas del Web Service
     */
    public static final String REGISTRARUSUARIO = BASEURL + "consultas/registrarUsuario.php";
    public static final String UPDATEFECHAFIN = BASEURL + "consultas/actualizarFechaFinConexion.php";
    public static final String OBTENERHISTVIAJES = BASEURL + "consultas/obtenerHistViajes.php";
    public static final String OBTENERUSUARIOLOGUEADO = BASEURL + "consultas/obtenerUsuarioLogueado.php";
    public static final String OBTENERCALIFICACIONVIAJES = BASEURL + "consultas/obtenerCalificacionViajes.php";
    public static final String RUTAOPTIMAGENERARSOLICITUD = "https://maps.googleapis.com/maps/";
    public static final String INSERTARSOLICITUD = BASEURL + "consultas/insertarSolicitud.php";
    public static final String DESACTIVARSOLICITUD = BASEURL + "consultas/desactivarSolicitud.php";
    public static final String ACTIVARSOLICITUD = BASEURL + "consultas/activarSolicitud.php";
}