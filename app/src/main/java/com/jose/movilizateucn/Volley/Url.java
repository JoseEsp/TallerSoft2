package com.jose.movilizateucn.Volley;

public class Url {

    //private static final String BASEURL = "http://movilizate.webcindario.com/";
    private static final String BASEURL = "http://movil.hostyweb.cl/";
    //private static final String BASEURL = "http://170.239.84.252/~hostyweb/movil/";

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
    public static final String ACTUALIZARFECHAVIAJE = BASEURL + "consultas/actualizarFechaViaje.php";
    public static final String GENERARFEEDBACK = BASEURL + "consultas/generarFeedback.php";
    public static final String GENERARACEPTACION = BASEURL + "consultas/generarAceptacion.php";
    public static final String CANCELARACEPTACION = BASEURL + "consultas/cancelarAceptacion.php";
    public static final String CONCRETARACEPTACION = BASEURL + "consultas/concretarAceptacion.php";
    public static final String NOTIFICARPASAJEROSVIAJECONCRETADO = BASEURL + "consultas/notificarPasajerosViajeConcretado.php";
    public static final String OBTENERCALIFICACIONESPARACHOFER = BASEURL + "consultas/obtenerCalificacionesParaChofer.php";
    public static final String ENVIARNOTIFICACION = BASEURL + "consultas/enviarNotificacion.php";
    public static final String ENVIARTOKEN = BASEURL + "consultas/enviarToken.php";

    public static final String ACTUALIZARCOMENTARIOPASAJEROACHOFER = BASEURL + "consultas/actualizarComentarioPasajeroAChofer.php";
    public static final String ACTUALIZARCOMENTARIOCHOFERAPASAJERO = BASEURL + "consultas/actualizarComentarioChoferAPasajero.php";
}