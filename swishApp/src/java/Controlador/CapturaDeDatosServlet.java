package Controlador;

// ================= IMPORTS =================
import Modelo.Partido;
import Modelo.Equipo;
import Modelo.Jugador;

import Modelo.Dao.PartidoDao;
import Modelo.Dao.EquipoDao;
import Modelo.Dao.EstadisticasDao;
import Modelo.Dao.JugadorDAO;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CapturaDeDatosServlet")

public class CapturaDeDatosServlet extends HttpServlet {

    // ================= DAO =================
    PartidoDao partidoDao = new PartidoDao();

    EquipoDao equipoDao = new EquipoDao();

    JugadorDAO jugadorDao = new JugadorDAO();

    EstadisticasDao estDao = new EstadisticasDao();

    // =====================================================
    // DO GET
    // =====================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null) {

            response.sendRedirect("index.jsp");

            return;
        }

        switch (accion) {

            // =========================================
            // ABRIR PARTIDO
            // =========================================
            case "abrirPartido":

                int idPartido = Integer.parseInt(request.getParameter("idPartido"));

                Partido partido = partidoDao.buscarPorId(idPartido);

                Equipo equipoA = equipoDao.buscarPorId(partido.getId_equipo_a());

                Equipo equipoB = equipoDao.buscarPorId(partido.getId_equipo_b());

                List<Jugador> jugadoresA = jugadorDao.listar_por_equipo(equipoA.getId());

                List<Jugador> jugadoresB = jugadorDao.listar_por_equipo(equipoB.getId());

                request.setAttribute("partido", partido);

                request.setAttribute("equipoA", equipoA);

                request.setAttribute("equipoB", equipoB);

                request.setAttribute("jugadoresA", jugadoresA);

                request.setAttribute("jugadoresB", jugadoresB);

                request.getRequestDispatcher("pages/capturaDeDatos.jsp").forward(request, response);

                return;

            // =========================================
            // DEFAULT
            // =========================================
            default:

                response.sendRedirect("index.jsp");

                return;
        }
    }

    // =====================================================
    // DO POST
    // =====================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null) {

            response.sendRedirect("index.jsp");

            return;
        }

        switch (accion) {

            // =========================================
            // SUMAR PUNTOS
            // =========================================
            case "sumarPuntos":

                int idPartidoP = Integer.parseInt(request.getParameter("idPartido"));

                int idJugadorP = Integer.parseInt(request.getParameter("idJugador"));

                int puntos = Integer.parseInt(request.getParameter("puntos"));

                String equipo = request.getParameter("equipo");
                
                int faltasActuales = estDao.obtenerFaltas(idPartidoP, idJugadorP);

                if (faltasActuales >= 5) {

                    response.sendRedirect("CapturaDeDatosServlet?accion=abrirPartido&idPartido=" + idPartidoP + "&equipo=" + equipo);

                    return;
                }

                // Guardar puntos del jugador
                estDao.sumarPuntos(idPartidoP, idJugadorP, puntos);

                // Actualizar marcador general
                estDao.actualizarMarcador(idPartidoP);

                response.sendRedirect("CapturaDeDatosServlet?accion=abrirPartido&idPartido=" + idPartidoP + "&equipo=" + equipo);

                return;

            // =========================================
            // SUMAR FALTA
            // =========================================
            case "sumarFalta":

                int idPartidoF = Integer.parseInt(request.getParameter("idPartido"));

                int idJugadorF = Integer.parseInt(request.getParameter("idJugador"));

                String equipoF = request.getParameter("equipo");
                
                int faltasActuales1 = estDao.obtenerFaltas(idPartidoF, idJugadorF);

                if (faltasActuales1 >= 5) {

                    response.sendRedirect("CapturaDeDatosServlet?accion=abrirPartido&idPartido=" + idPartidoF + "&equipo=" + equipoF);

                    return;
                }

                // Guardar falta
                estDao.sumarFalta(idPartidoF, idJugadorF);

                response.sendRedirect("CapturaDeDatosServlet?accion=abrirPartido&idPartido=" + idPartidoF + "&equipo=" + equipoF);

                return;
                
            // =========================================
// FINALIZAR PARTIDO
// =========================================
            case "finalizarPartido":

                // Obtener partido
                int idPartido
                        = Integer.parseInt(
                                request.getParameter("idPartido")
                        );

                Partido partido
                        = partidoDao.buscarPorId(idPartido);

                // =========================================
                // OBTENER MARCADOR
                // =========================================
                int puntosA
                        = partido.getPuntos_a();

                int puntosB
                        = partido.getPuntos_b();

                int ganador;

                int perdedor;

                // =========================================
                // DEFINIR GANADOR Y PERDEDOR
                // =========================================
                if (puntosA > puntosB) {

                    ganador
                            = partido.getId_equipo_a();

                    perdedor
                            = partido.getId_equipo_b();

                } else {

                    ganador
                            = partido.getId_equipo_b();

                    perdedor
                            = partido.getId_equipo_a();
                }

                // =========================================
                // ACTUALIZAR DERROTAS
                // =========================================
                Equipo equipoPerdedor
                        = equipoDao.buscarPorId(perdedor);

                equipoPerdedor.setDerrotas(
                        equipoPerdedor.getDerrotas() + 1
                );

                // =========================================
                // ELIMINAR SI TIENE 2 DERROTAS
                // =========================================
                if (equipoPerdedor.getDerrotas() >= 2) {

                    equipoPerdedor.setEstado(
                            "ELIMINADO"
                    );

                } else {

                    equipoPerdedor.setEstado(
                            "ACTIVO"
                    );
                }

                // Guardar equipo
                // Guardar derrotas y estado del equipo perdedor
                equipoDao.actualizarDerrotas(
                        equipoPerdedor
                );

                // =========================================
                // FINALIZAR PARTIDO
                // =========================================
                partido.setEstado("FINALIZADO");

                partido.setGanador(ganador);

                partido.setPerdedor(perdedor);

                // Guardar partido
                partidoDao.actualizar(partido);

                // =========================================
                // GENERAR SIGUIENTE RONDA
                // =========================================
                generarSiguientePartido(partido);

                // =========================================
                // REGRESAR
                // =========================================
                response.sendRedirect(
                        "PartidosServerlet"
                );

                return;
            // =========================================
            // DEFAULT
            // =========================================
            default:

                response.sendRedirect("index.jsp");

                return;
        }
    }
    
// =====================================================
// GENERAR SIGUIENTE PARTIDO
// =====================================================
    public void generarSiguientePartido(
            Partido partidoFinalizado
    ) {

        int idTorneo = partidoFinalizado.getId_torneo();

        int rondaActual = partidoFinalizado.getRonda();

        String bracketActual = partidoFinalizado.getBracket();

        // =========================================
        // CASO ESPECIAL: GRAN FINAL TERMINÓ
        // =========================================
        if ("GRAN_FINAL".equals(bracketActual)) {
            return;
        }

        // =========================================
        // VERIFICAR SI TODA LA RONDA TERMINÓ
        // =========================================
        boolean rondaCompleta = verificarRondaCompleta(
                idTorneo,
                bracketActual,
                rondaActual
        );

        if (!rondaCompleta) {
            // Hay partidos pendientes, no hacer nada todavía
            return;
        }

        // =========================================
        // RECOLECTAR GANADORES Y PERDEDORES
        // DE LA RONDA QUE ACABA DE TERMINAR
        // =========================================
        List<Integer> ganadoresRonda = new ArrayList<>();

        List<Integer> perdedoresRonda = new ArrayList<>();

        List<Partido> todosLosPartidos
                = partidoDao.listarPorTorneo(idTorneo);

        for (Partido p : todosLosPartidos) {

            // Solo del bracket y ronda que terminó
            if (!bracketActual.equals(p.getBracket())) {
                continue;
            }

            if (p.getRonda() != rondaActual) {
                continue;
            }

            if (!"FINALIZADO".equals(p.getEstado())) {
                continue;
            }

            // Agregar ganador
            if (!ganadoresRonda.contains(p.getGanador())) {
                ganadoresRonda.add(p.getGanador());
            }

            // Ignorar perdedor 0 (partido BYE no tiene perdedor)
            if (p.getPerdedor() == 0) {
                continue;
            }

            // Guardar perdedor solo si no está eliminado
            Equipo equipoPerdedor
                    = equipoDao.buscarPorId(p.getPerdedor());

            if (equipoPerdedor.getDerrotas() < 2) {

                if (!perdedoresRonda.contains(p.getPerdedor())) {
                    perdedoresRonda.add(p.getPerdedor());
                }
            }
        }

        // =========================================
        // LÓGICA SEGÚN EL BRACKET QUE TERMINÓ
        // =========================================
        if ("WINNERS".equals(bracketActual)) {

            // Ganadores de WINNERS:
            // Ganadores de WINNERS:
            // Si hay 2 o más, se emparejan entre sí
            if (ganadoresRonda.size() >= 2) {

                crearPartidos(
                        ganadoresRonda,
                        "WINNERS",
                        rondaActual + 1,
                        idTorneo
                );

                // Si sobra un ganador sin emparejar (número impar)
                // se le da BYE automático en WINNERS
                if (ganadoresRonda.size() % 2 != 0) {

                    int equipoSobrante
                            = ganadoresRonda.get(ganadoresRonda.size() - 1);

                    // Crear BYE para el sobrante en la siguiente ronda
                    Partido bye = new Partido();

                    bye.setNombre("BYE Ronda " + (rondaActual + 1));

                    bye.setEstado("FINALIZADO");

                    bye.setPuntos_a(0);

                    bye.setPuntos_b(0);

                    bye.setFecha(
                            java.time.LocalDate.now().toString()
                    );

                    bye.setBracket("WINNERS");

                    bye.setRonda(rondaActual + 1);

                    bye.setId_equipo_a(equipoSobrante);

                    bye.setId_equipo_b(0);

                    bye.setId_torneo(idTorneo);

                    bye.setGanador(equipoSobrante);

                    bye.setPerdedor(0);

                    bye.setBye(true);

                    // Solo insertar si no existe ya
                    boolean existeBye = partidoDao.existePartidoPendiente(
                            equipoSobrante,
                            0,
                            idTorneo
                    );

                    if (!existeBye) {
                        partidoDao.insertar(bye);
                    }
                }
            }

            // Perdedores de WINNERS van al LOSERS bracket
            if (perdedoresRonda.size() >= 2) {

                int rondaLosers = obtenerSiguienteRondaLosers(idTorneo);

                crearPartidos(
                        perdedoresRonda,
                        "LOSERS",
                        rondaLosers,
                        idTorneo
                );

            } else if (perdedoresRonda.size() == 1) {

                // Solo 1 perdedor, espera al siguiente perdedor de WINNERS
                // o sube directo si el LOSERS ya tiene alguien esperando
                intentarCruzarConLosers(
                        perdedoresRonda.get(0),
                        idTorneo
                );
            }

        } else if ("LOSERS".equals(bracketActual)) {

            // Ganadores del LOSERS siguen en LOSERS
            // Ganadores del LOSERS siguen en LOSERS
            if (ganadoresRonda.size() >= 2) {

                crearPartidos(
                        ganadoresRonda,
                        "LOSERS",
                        rondaActual + 1,
                        idTorneo
                );

                // Si sobra uno, intentar cruzarlo
                if (ganadoresRonda.size() % 2 != 0) {

                    int sobrante
                            = ganadoresRonda.get(ganadoresRonda.size() - 1);

                    intentarCruzarConLosers(sobrante, idTorneo);
                }

            } else if (ganadoresRonda.size() == 1) {

                intentarCruzarConLosers(
                        ganadoresRonda.get(0),
                        idTorneo
                );
            }
            
        }

        // =========================================
        // VERIFICAR GRAN FINAL
        // =========================================
        verificarGranFinal(idTorneo);
    }

// =====================================================
// INTENTAR EMPAREJAR UN EQUIPO QUE ESPERA EN LOSERS
// =====================================================
    public void intentarCruzarConLosers(
            int idEquipo,
            int idTorneo
    ) {

        // Buscar si hay otro equipo esperando en losers
        // (sin partido pendiente asignado)
        List<Partido> partidos
                = partidoDao.listarPorTorneo(idTorneo);

        // Recolectar equipos que YA tienen partido pendiente en losers
        List<Integer> ocupados = new ArrayList<>();

        for (Partido p : partidos) {

            if (!"LOSERS".equals(p.getBracket())) {
                continue;
            }

            if (!"PENDIENTE".equals(p.getEstado())) {
                continue;
            }

            // Evitar agregar id 0 (partido BYE)
            if (p.getId_equipo_a() != 0) {
                ocupados.add(p.getId_equipo_a());
            }

            if (p.getId_equipo_b() != 0) {
                ocupados.add(p.getId_equipo_b());
            }
        }

        // Buscar equipos vivos del torneo que estén en losers
        // (1 derrota, activo) y que NO tengan partido pendiente
        List<Equipo> todosEquipos = equipoDao.listar();

        int equipoEsperando = 0;

        for (Equipo e : todosEquipos) {

            if (e.getId_torneo() != idTorneo) {
                continue;
            }

            // Solo los que tienen exactamente 1 derrota
            // Solo equipos activos con exactamente 1 derrota
            if (!"ACTIVO".equals(e.getEstado())) {
                continue;
            }

            if (e.getDerrotas() != 1) {
                continue;
            }

            // No es el mismo equipo que acaba de llegar
            if (e.getId() == idEquipo) {
                continue;
            }

            // No tiene partido pendiente ya asignado
            if (ocupados.contains(e.getId())) {
                continue;
            }

            equipoEsperando = e.getId();
            break;
        }

        // Si encontramos a alguien esperando, crear el partido
        if (equipoEsperando != 0 && idEquipo != 0) {

            int rondaLosers
                    = obtenerSiguienteRondaLosers(idTorneo);

            List<Integer> par = new ArrayList<>();

            par.add(idEquipo);

            par.add(equipoEsperando);

            crearPartidos(par, "LOSERS", rondaLosers, idTorneo);
        }

        // Si no hay nadie esperando, este equipo queda en espera
        // hasta que llegue otro perdedor
    }

// =====================================================
// VERIFICAR SI TODA UNA RONDA ESTÁ COMPLETA
// =====================================================
    public boolean verificarRondaCompleta(
            int idTorneo,
            String bracket,
            int ronda
    ) {

        List<Partido> partidos
                = partidoDao.listarPorTorneo(idTorneo);

        boolean hayAlMenosUno = false;

        for (Partido p : partidos) {

            if (!bracket.equals(p.getBracket())) {
                continue;
            }

            if (p.getRonda() != ronda) {
                continue;
            }

            hayAlMenosUno = true;

            if (!"FINALIZADO".equals(p.getEstado())) {
                return false;
            }
        }

        // Si no encontró ningún partido de esa ronda, no está completa
        return hayAlMenosUno;
    }

// =====================================================
// CREAR PARTIDOS DE LA SIGUIENTE RONDA
// =====================================================
    public void crearPartidos(
            List<Integer> equipos,
            String bracket,
            int ronda,
            int idTorneo
    ) {

        if (equipos.size() < 2) {
            return;
        }

        for (int i = 0; i < equipos.size() - 1; i += 2) {

            int equipoA = equipos.get(i);

            int equipoB = equipos.get(i + 1);

            if (equipoA == equipoB) {
                continue;
            }

            boolean existe = partidoDao.existePartidoPendiente(
                    equipoA,
                    equipoB,
                    idTorneo
            );

            if (existe) {
                continue;
            }

            Partido nuevo = new Partido();

            nuevo.setNombre(bracket + " Ronda " + ronda);

            nuevo.setEstado("PENDIENTE");

            nuevo.setPuntos_a(0);

            nuevo.setPuntos_b(0);

            nuevo.setFecha(
                    java.time.LocalDate.now().toString()
            );

            nuevo.setBracket(bracket);

            nuevo.setRonda(ronda);

            nuevo.setId_equipo_a(equipoA);

            nuevo.setId_equipo_b(equipoB);

            nuevo.setId_torneo(idTorneo);

            partidoDao.insertar(nuevo);
        }
    }

// =====================================================
// OBTENER SIGUIENTE NÚMERO DE RONDA EN LOSERS
// =====================================================
    public int obtenerSiguienteRondaLosers(int idTorneo) {

        List<Partido> partidos
                = partidoDao.listarPorTorneo(idTorneo);

        int maxRonda = 0;

        for (Partido p : partidos) {

            if (!"LOSERS".equals(p.getBracket())) {
                continue;
            }

            if (p.getRonda() > maxRonda) {
                maxRonda = p.getRonda();
            }
        }

        return maxRonda + 1;
    }

// =====================================================
// VERIFICAR SI SE PUEDE CREAR LA GRAN FINAL
// =====================================================
    public void verificarGranFinal(int idTorneo) {

        List<Equipo> equipos = equipoDao.listar();

        int invicto = 0;

        int sobreviviente = 0;

        for (Equipo e : equipos) {

            if (e.getId_torneo() != idTorneo) {
                continue;
            }

            if (!"ACTIVO".equals(e.getEstado())) {
                continue;
            }

            if (e.getDerrotas() == 0) {
                invicto = e.getId();
            }

            if (e.getDerrotas() == 1) {
                sobreviviente = e.getId();
            }
        }

        // Necesitamos exactamente uno de cada tipo
        if (invicto == 0 || sobreviviente == 0) {
            return;
        }

        // Verificar que no haya partidos pendientes
        // Solo bloqueamos si hay partidos pendientes que NO involucren
        // al invicto ni al sobreviviente
        List<Partido> partidos
                = partidoDao.listarPorTorneo(idTorneo);

        for (Partido p : partidos) {

            // Ignorar gran final
            if ("GRAN_FINAL".equals(p.getBracket())) {
                continue;
            }

            // Ignorar finalizados
            if ("FINALIZADO".equals(p.getEstado())) {
                continue;
            }

            // Si hay un partido pendiente que involucra
            // al invicto o al sobreviviente, no crear gran final todavía
            boolean involucraInvicto
                    = p.getId_equipo_a() == invicto
                    || p.getId_equipo_b() == invicto;

            boolean involucrasobreviviente
                    = p.getId_equipo_a() == sobreviviente
                    || p.getId_equipo_b() == sobreviviente;

            if (involucraInvicto || involucrasobreviviente) {
                // Todavía tienen partidos por jugar
                return;
            }
        }

        // Verificar que no exista ya la gran final
        boolean existe = partidoDao.existePartidoPendiente(
                invicto,
                sobreviviente,
                idTorneo
        );

        if (existe) {
            return;
        }

        // Crear la gran final
        Partido granFinal = new Partido();

        granFinal.setNombre("Gran Final");

        granFinal.setEstado("PENDIENTE");

        granFinal.setPuntos_a(0);

        granFinal.setPuntos_b(0);

        granFinal.setFecha(
                java.time.LocalDate.now().toString()
        );

        granFinal.setBracket("GRAN_FINAL");

        granFinal.setRonda(1);

        granFinal.setId_equipo_a(invicto);

        granFinal.setId_equipo_b(sobreviviente);

        granFinal.setId_torneo(idTorneo);

        partidoDao.insertar(granFinal);
    }
}