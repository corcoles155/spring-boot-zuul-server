package org.sanchez.corcoles.ana.pruebasconcepto.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);

    /*
    Hay tres tipos de filtros:
     * Pre --> Antes de que se resuelve la ruta, se usa para pasar datos a la request.
     * Post --> Después de que se resuelva la ruta, se usa para modificar la response
     * Route --> Se ejecuta durante el enrutado, se usa para la comunicación con el microservicio.
     * */
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    //Para realizar validaciones, si devuelve true se ejecuta el filtro.
    @Override
    public boolean shouldFilter() {
        return true;
    }

    //Aquí se resuelve la lógica del filtro
    @Override
    public Object run() throws ZuulException {
        final RequestContext requestContext = RequestContext.getCurrentContext();
        final HttpServletRequest httpServletRequest = requestContext.getRequest();

        LOGGER.info("Entrando a post filter");

        final Long tiempoInicioRequest = (Long) httpServletRequest.getAttribute("tiempoInicioRequest");
        final Long tiempoFinalRequest = System.currentTimeMillis();
        final Long tiempoTranscurrido = tiempoFinalRequest - tiempoInicioRequest;

        LOGGER.info(String.format("Tiempo transcurrido en segundos %s", tiempoTranscurrido.doubleValue() / 1000.00));
        LOGGER.info(String.format("Tiempo transcurrido en milisegundos %s", tiempoTranscurrido));

        return null;
    }
}
