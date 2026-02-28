package es.rebextech.filters;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Filtro global encargado de establecer la codificación de caracteres (UTF-8)
 * para todas las peticiones y respuestas HTTP entrantes.
 * Garantiza que los caracteres especiales (como tildes y eñes) se procesen,
 * guarden y muestren correctamente en toda la tienda sin corromperse.
 * @author Rebeca Poma
 */

@WebFilter(filterName = "UTF8", urlPatterns = {"/*"})
public class CodeFilter implements Filter {
    
     @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }
   
    /**
     *
     * @param request Petición
     * @param response Respuesta
     * @param chain Cambio
     * @throws IOException Excepción de entrada/salida
     * @throws ServletException Excepción
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF8");
        chain.doFilter(request, response);
    }

    
    @Override
    public void destroy() {
    }
    
}
