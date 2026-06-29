package web2.tec.proyectoweb2dannyjohel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import web2.tec.proyectoweb2dannyjohel.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Aplica el interceptor a las rutas web del sistema
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // Rutas publicas necesarias para entrar al sistema
                        "/",
                        "/usuario/login",
                        "/usuario/registro",
                        // La API queda publica para poder probarla desde Postman sin sesion
                        "/api/**",
                        // Recursos estaticos que no deben requerir login
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**",
                        "/favicon.ico"
                );
    }
}
