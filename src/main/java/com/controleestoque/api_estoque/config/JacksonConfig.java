package com.controleestoque.api_estoque.config; // Ajuste o pacote conforme necessário

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

@Configuration
public class JacksonConfig {

    /**
     * Registra o Hibernate Module para evitar problemas de serialização
     * de Lazy Loading e Proxies do Hibernate (ByteBuddyInterceptor).
     */
    @Bean
    public Hibernate6Module hibernateModule() {
        // Opcional: Desabilita a falha quando dados Lazy não são carregados.
        Hibernate6Module module = new Hibernate6Module();
        // module.disable(Hibernate6Module.Feature.FORCE_LAZY_LOADING);
        
        return module;
    }
}
