package org.sanchez.corcoles.ana.pruebasconcepto.zuul.oauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

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

                .anyRequest().authenticated();
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

    @Bean("jwtTokenStore")
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    //Es la configuración por defecto del token
    @Bean("jwtAccessTokenConverter")
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("codigo_secreto"); //Esto debe ser un secreto
        return jwtAccessTokenConverter;
    }
}
