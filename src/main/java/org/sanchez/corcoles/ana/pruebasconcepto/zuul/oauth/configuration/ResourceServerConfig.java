package org.sanchez.corcoles.ana.pruebasconcepto.zuul.oauth.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${config.security.oauth.jwt.key}")
    private String jwtKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }

    @Override
    //Utilizamos authorizeRequests para establecer permisos sobre nuestros endpoints
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/productos/productos",
                        "/api/usuarios/usuarios",
                        "/api/items/items").permitAll()
                .antMatchers(HttpMethod.GET, "/api/productos/productos/{id}",
                        "/api/usuarios/usuarios/{id}",
                        "/api/items/items/{id}/cantidad/{cantidad}").hasAnyRole("USER", "ADMIN") //El prefijo ROLE_ lo inserta automaticamente
                .antMatchers("/api/productos/productos/**",
                        "/api/usuarios/usuarios/**",
                        "/api/items/items/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().cors().configurationSource(corsConfigurationSource());
                /*.antMatchers(HttpMethod.POST, "/api/productos/productos",
                        "/api/usuarios/usuarios",
                        "/api/items/items").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/productos/productos/{id}",
                        "/api/usuarios/usuarios/{id}",
                        "/api/items/items/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/productos/productos/{id}",
                        "/api/usuarios/usuarios/{id}",
                        "/api/items/items/{id}").hasRole("ADMIN");*/
    }

    //Configuración del cors para todas las rutas (/**)
    @Bean("corsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); //Se pueden añadir los nombres de dominios, también se puede añadir con corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList(HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    //Configuramos un filtro para que se aplique de forma global
    @Bean("corsFiltern")
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        final FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsFilterRegistrationBean;
    }

    @Bean("jwtTokenStore")
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    //Es la configuración por defecto del token
    @Bean("jwtAccessTokenConverter")
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(jwtKey); //Esto debe ser un secreto
        return jwtAccessTokenConverter;
    }
}
