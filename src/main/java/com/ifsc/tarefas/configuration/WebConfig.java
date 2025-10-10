package com.ifsc.tarefas.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configurações Web adicionais.
 * - Define a página inicial para redirecionar ao /templates (caso autenticado o filtro deixará passar, do contrário redirecionará ao login).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/templates");
    }
}


