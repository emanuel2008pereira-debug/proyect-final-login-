package loginmvc.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final List<String> SOLO_ADMIN = List.of(
            "/usuarios",
            "/guardar-usuario",
            "/bitacora",
            "/guardar-alumno",
            "/editar-alumno",
            "/eliminar-alumno");

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        String contexto = request.getContextPath();
        String ruta = request.getRequestURI().substring(contexto.length());

        HttpSession session = request.getSession(false);

        boolean logueado = session != null
                && session.getAttribute("usuario") != null;

        if (!logueado) {
            if (ruta.startsWith("/api/")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.sendRedirect(contexto + "/loginmvc");
            }
            return false;
        }

        String rol = (String) session.getAttribute("rol");

        if (esRutaDeAdmin(ruta) && !"ADMIN".equals(rol)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    private boolean esRutaDeAdmin(String ruta) {
        for (String base : SOLO_ADMIN) {
            if (ruta.equals(base) || ruta.startsWith(base + "/")) {
                return true;
            }
        }
        return false;
    }
}