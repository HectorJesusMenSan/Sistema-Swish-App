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
    public void generarSiguientePartido(Partido partidoFinalizado) {

        int idTorneo = partidoFinalizado.getId_torneo();

        // =========================================
        // GRAN FINAL 2 (DESEMPATE) -> TORNEO TERMINA
        // Es el partido definitivo, no genera nada mas
        // =========================================
        if ("GRAN_FINAL_2".equals(partidoFinalizado.getBracket())) {
            return;
        }

        // =========================================
        // GRAN FINAL -> EVALUAR SI HACE FALTA DESEMPATE
        // =========================================
        if ("GRAN_FINAL".equals(partidoFinalizado.getBracket())) {
            procesarGranFinal(partidoFinalizado);
            return;
        }

        // =========================================
        // RECOLECTAR ESTADO ACTUAL DEL TORNEO
        // =========================================
        List<Partido> partidos = partidoDao.listarPorTorneo(idTorneo);

        // Equipos con partido pendiente asignado
        List<Integer> ocupados = new ArrayList<>();

        // Bracket donde quedo pendiente cada equipo ocupado
        // (lo usamos para saber si YA hay un partido
        // pendiente dentro de WINNERS o dentro de LOSERS)
        boolean hayPendientesWinners = false;

        boolean hayPendientesLosers = false;

        for (Partido p : partidos) {

            if (!"PENDIENTE".equals(p.getEstado())) {
                continue;
            }

            if (p.getId_equipo_a() != 0) {
                ocupados.add(p.getId_equipo_a());
            }

            if (p.getId_equipo_b() != 0) {
                ocupados.add(p.getId_equipo_b());
            }

            if ("WINNERS".equals(p.getBracket())) {
                hayPendientesWinners = true;
            } else if ("LOSERS".equals(p.getBracket())) {
                hayPendientesLosers = true;
            }
        }

        // =========================================
        // EQUIPOS DISPONIBLES EN WINNERS
        // (0 derrotas, activo, sin partido pendiente)
        // =========================================
        List<Integer> disponiblesWinners = new ArrayList<>();

        List<Equipo> todosEquipos = equipoDao.listar();

        for (Equipo e : todosEquipos) {

            if (e.getId_torneo() != idTorneo) {
                continue;
            }

            if (!"ACTIVO".equals(e.getEstado())) {
                continue;
            }

            if (e.getDerrotas() != 0) {
                continue;
            }

            if (ocupados.contains(e.getId())) {
                continue;
            }

            disponiblesWinners.add(e.getId());
        }

        // =========================================
        // EQUIPOS DISPONIBLES EN LOSERS
        // (1 derrota, activo, sin partido pendiente)
        // =========================================
        List<Integer> disponiblesLosers = new ArrayList<>();

        for (Equipo e : todosEquipos) {

            if (e.getId_torneo() != idTorneo) {
                continue;
            }

            if (!"ACTIVO".equals(e.getEstado())) {
                continue;
            }

            if (e.getDerrotas() != 1) {
                continue;
            }

            if (ocupados.contains(e.getId())) {
                continue;
            }

            disponiblesLosers.add(e.getId());
        }

        // =========================================
        // TOTAL DE EQUIPOS ACTIVOS POR BRACKET
        // (ocupados + disponibles, sirve para decidir
        // si conviene dar BYE a un sobrante)
        // =========================================
        int totalActivosWinners = 0;

        int totalActivosLosers = 0;

        for (Equipo e : todosEquipos) {

            if (e.getId_torneo() != idTorneo) {
                continue;
            }

            if (!"ACTIVO".equals(e.getEstado())) {
                continue;
            }

            if (e.getDerrotas() == 0) {
                totalActivosWinners++;
            } else if (e.getDerrotas() == 1) {
                totalActivosLosers++;
            }
        }

        // =========================================
        // CALCULAR SIGUIENTE RONDA
        // =========================================
        int siguienteRonda = 1;

        for (Partido p : partidos) {

            if ("GRAN_FINAL".equals(p.getBracket())) {
                continue;
            }

            if (p.getRonda() >= siguienteRonda) {
                siguienteRonda = p.getRonda() + 1;
            }
        }

        // =========================================
        // CASO GRAN FINAL:
        // 1 en winners + 1 en losers + sin pendientes
        // =========================================
        if (disponiblesWinners.size() == 1
                && disponiblesLosers.size() == 1) {

            // Verificar que no haya partidos pendientes
            boolean hayPendientes = false;

            for (Partido p : partidos) {

                if ("GRAN_FINAL".equals(p.getBracket())) {
                    continue;
                }

                if ("PENDIENTE".equals(p.getEstado())) {
                    hayPendientes = true;
                    break;
                }
            }

            if (!hayPendientes) {

                boolean existe = partidoDao.existePartidoPendiente(
                        disponiblesWinners.get(0),
                        disponiblesLosers.get(0),
                        idTorneo
                );

                if (!existe) {

                    Partido granFinal = new Partido();

                    granFinal.setNombre("Gran Final");

                    granFinal.setEstado("PENDIENTE");

                    granFinal.setPuntos_a(0);

                    granFinal.setPuntos_b(0);

                    granFinal.setFecha(
                            java.time.LocalDate.now().toString()
                    );

                    granFinal.setBracket("GRAN_FINAL");

                    granFinal.setRonda(siguienteRonda);

                    granFinal.setId_equipo_a(
                            disponiblesWinners.get(0)
                    );

                    granFinal.setId_equipo_b(
                            disponiblesLosers.get(0)
                    );

                    granFinal.setId_torneo(idTorneo);

                    partidoDao.insertar(granFinal);
                }

                return;
            }
        }

        // =========================================
        // CREAR PARTIDOS WINNERS
        // Si hay 2+ disponibles en winners
        // =========================================
        if (disponiblesWinners.size() >= 2) {

            int i;

            for (i = 0; i + 1 < disponiblesWinners.size(); i += 2) {

                int equipoA = disponiblesWinners.get(i);

                int equipoB = disponiblesWinners.get(i + 1);

                boolean existe = partidoDao.existePartidoPendiente(
                        equipoA,
                        equipoB,
                        idTorneo
                );

                if (!existe) {

                    Partido nuevo = new Partido();

                    nuevo.setNombre("WINNERS Ronda " + siguienteRonda);

                    nuevo.setEstado("PENDIENTE");

                    nuevo.setPuntos_a(0);

                    nuevo.setPuntos_b(0);

                    nuevo.setFecha(
                            java.time.LocalDate.now().toString()
                    );

                    nuevo.setBracket("WINNERS");

                    nuevo.setRonda(siguienteRonda);

                    nuevo.setId_equipo_a(equipoA);

                    nuevo.setId_equipo_b(equipoB);

                    nuevo.setId_torneo(idTorneo);

                    partidoDao.insertar(nuevo);
                }
            }

            // =========================================
            // SOBRANTE IMPAR EN WINNERS
            // =========================================
            if (i < disponiblesWinners.size()
                    && !hayPendientesWinners) {

                int sobranteWinners = disponiblesWinners.get(i);

                darByeSiCorresponde(
                        sobranteWinners,
                        "WINNERS",
                        siguienteRonda,
                        idTorneo,
                        totalActivosLosers,
                        partidos
                );
            }

        } else if (disponiblesWinners.size() == 1
                && !hayPendientesWinners) {

            // No hay con quien emparejar dentro de WINNERS
            int sobranteWinners = disponiblesWinners.get(0);

            darByeSiCorresponde(
                    sobranteWinners,
                    "WINNERS",
                    siguienteRonda,
                    idTorneo,
                    totalActivosLosers,
                    partidos
            );
        }

        // =========================================
        // CREAR PARTIDOS LOSERS
        // Si hay 2+ disponibles en losers
        // =========================================
        if (disponiblesLosers.size() >= 2) {

            int j;

            for (j = 0; j + 1 < disponiblesLosers.size(); j += 2) {

                int equipoA = disponiblesLosers.get(j);

                int equipoB = disponiblesLosers.get(j + 1);

                boolean existe = partidoDao.existePartidoPendiente(
                        equipoA,
                        equipoB,
                        idTorneo
                );

                if (!existe) {

                    Partido nuevo = new Partido();

                    nuevo.setNombre("LOSERS Ronda " + siguienteRonda);

                    nuevo.setEstado("PENDIENTE");

                    nuevo.setPuntos_a(0);

                    nuevo.setPuntos_b(0);

                    nuevo.setFecha(
                            java.time.LocalDate.now().toString()
                    );

                    nuevo.setBracket("LOSERS");

                    nuevo.setRonda(siguienteRonda);

                    nuevo.setId_equipo_a(equipoA);

                    nuevo.setId_equipo_b(equipoB);

                    nuevo.setId_torneo(idTorneo);

                    partidoDao.insertar(nuevo);
                }
            }

            // =========================================
            // SOBRANTE IMPAR EN LOSERS
            // =========================================
            if (j < disponiblesLosers.size()
                    && !hayPendientesLosers) {

                int sobranteLosers = disponiblesLosers.get(j);

                darByeSiCorresponde(
                        sobranteLosers,
                        "LOSERS",
                        siguienteRonda,
                        idTorneo,
                        totalActivosWinners,
                        partidos
                );
            }

        } else if (disponiblesLosers.size() == 1
                && !hayPendientesLosers) {

            // No hay con quien emparejar dentro de LOSERS
            int sobranteLosers = disponiblesLosers.get(0);

            darByeSiCorresponde(
                    sobranteLosers,
                    "LOSERS",
                    siguienteRonda,
                    idTorneo,
                    totalActivosWinners,
                    partidos
            );
        }
    }

    // =====================================================
    // PROCESAR GRAN FINAL
    // Decide si hace falta el partido de desempate
    // (cuando el finalista de WINNERS pierde su primera
    // derrota y queda empatado 1-1 contra el finalista
    // de LOSERS).
    // =====================================================
    private void procesarGranFinal(Partido granFinal) {

        int idTorneo = granFinal.getId_torneo();

        int idGanador = granFinal.getGanador();

        int idPerdedor = granFinal.getPerdedor();

        if (idPerdedor == 0) {
            return;
        }

        // =========================================
        // VER ESTADO ACTUAL DEL PERDEDOR
        // (ya se le sumo la derrota antes de llamar
        // a este metodo, en el case "finalizarPartido")
        // =========================================
        Equipo equipoPerdedor = equipoDao.buscarPorId(idPerdedor);

        if (equipoPerdedor == null) {
            return;
        }

        // =========================================
        // SI EL PERDEDOR YA QUEDO ELIMINADO
        // significa que venia de LOSERS (1 derrota previa)
        // y esta fue su 2da derrota -> el torneo TERMINA,
        // no hace falta desempate
        // =========================================
        if ("ELIMINADO".equals(equipoPerdedor.getEstado())) {
            return;
        }

        // =========================================
        // EL PERDEDOR NO QUEDO ELIMINADO
        // significa que venia de WINNERS con 0 derrotas
        // y esta fue su PRIMERA derrota -> ahora ambos
        // equipos tienen 1 derrota -> hace falta el
        // partido de desempate (GRAN_FINAL_2)
        // =========================================
        boolean yaExiste = partidoDao.existePartidoPendiente(
                idGanador,
                idPerdedor,
                idTorneo
        );

        if (yaExiste) {
            return;
        }

        Partido desempate = new Partido();

        desempate.setNombre("Gran Final - Desempate");

        desempate.setEstado("PENDIENTE");

        desempate.setPuntos_a(0);

        desempate.setPuntos_b(0);

        desempate.setFecha(
                java.time.LocalDate.now().toString()
        );

        desempate.setBracket("GRAN_FINAL_2");

        desempate.setRonda(granFinal.getRonda() + 1);

        // El ganador de la Gran Final repite como equipo A
        desempate.setId_equipo_a(idGanador);

        // El que perdio (y quedo con 1 derrota) repite como equipo B
        desempate.setId_equipo_b(idPerdedor);

        desempate.setId_torneo(idTorneo);

        partidoDao.insertar(desempate);
    }

    // =====================================================
    // DAR BYE SI CORRESPONDE
    // Un equipo se queda sin rival dentro de su propio
    // bracket. Si el bracket CONTRARIO todavia tiene mas
    // de 1 equipo activo, le damos BYE para que no se
    // quede esperando y el torneo siga avanzando.
    // Si el bracket contrario ya se redujo a 1 (o 0),
    // NO le damos bye todavia, porque ya estamos a un
    // paso de la Gran Final y hay que esperar a que
    // se estabilice (la evaluamos en la siguiente llamada).
    // =====================================================
    private void darByeSiCorresponde(
            int idEquipo,
            String bracket,
            int ronda,
            int idTorneo,
            int totalActivosBracketContrario,
            List<Partido> partidos
    ) {

        if (totalActivosBracketContrario <= 1) {
            return;
        }

        if (tieneByeEnRonda(partidos, bracket, ronda, idEquipo)) {
            return;
        }

        Equipo eq = equipoDao.buscarPorId(idEquipo);

        Partido bye = new Partido();

        bye.setNombre(
                (eq != null ? eq.getNombre() : "Equipo") + " - BYE"
        );

        bye.setEstado("FINALIZADO");

        bye.setPuntos_a(0);

        bye.setPuntos_b(0);

        bye.setFecha(
                java.time.LocalDate.now().toString()
        );

        bye.setBracket(bracket);

        bye.setRonda(ronda);

        bye.setId_equipo_a(idEquipo);

        bye.setId_equipo_b(0);

        bye.setId_torneo(idTorneo);

        bye.setGanador(idEquipo);

        bye.setPerdedor(0);

        bye.setBye(true);

        partidoDao.insertar(bye);
    }

    // =====================================================
    // VERIFICAR SI YA EXISTE UN BYE PARA ESE EQUIPO
    // EN ESE BRACKET Y RONDA (evita duplicados)
    // =====================================================
    private boolean tieneByeEnRonda(
            List<Partido> partidos,
            String bracket,
            int ronda,
            int idEquipo
    ) {

        for (Partido p : partidos) {

            if (!p.isBye()) {
                continue;
            }

            if (!bracket.equals(p.getBracket())) {
                continue;
            }

            if (p.getRonda() != ronda) {
                continue;
            }

            if (p.getId_equipo_a() == idEquipo) {
                return true;
            }
        }

        return false;
    }
}
