package com.sigma.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.sigma.domain.Proyecto.class.getName());
            createCache(cm, com.sigma.domain.Proyecto.class.getName() + ".puntoMonitoreos");
            createCache(cm, com.sigma.domain.Proyecto.class.getName() + ".equipos");
            createCache(cm, com.sigma.domain.Proyecto.class.getName() + ".observacions");
            createCache(cm, com.sigma.domain.Proyecto.class.getName() + ".laboratorios");
            createCache(cm, com.sigma.domain.EquipoMonitoreo.class.getName());
            createCache(cm, com.sigma.domain.LaboratorioMonitoreo.class.getName());
            createCache(cm, com.sigma.domain.Observacion.class.getName());
            createCache(cm, com.sigma.domain.Componente.class.getName());
            createCache(cm, com.sigma.domain.Componente.class.getName() + ".tipoComponentes");
            createCache(cm, com.sigma.domain.Componente.class.getName() + ".observacions");
            createCache(cm, com.sigma.domain.TipoComponente.class.getName());
            createCache(cm, com.sigma.domain.TipoComponente.class.getName() + ".paramentros");
            createCache(cm, com.sigma.domain.Paramentro.class.getName());
            createCache(cm, com.sigma.domain.NormaCalidad.class.getName());
            createCache(cm, com.sigma.domain.NormaCalidad.class.getName() + ".paramentros");
            createCache(cm, com.sigma.domain.PuntoMonitoreo.class.getName());
            createCache(cm, com.sigma.domain.PuntoMonitoreo.class.getName() + ".puntoMonitoreoObs");
            createCache(cm, com.sigma.domain.PuntoMonitoreo.class.getName() + ".paramentros");
            createCache(cm, com.sigma.domain.PuntoMonitoreo.class.getName() + ".fotografiaPuntos");
            createCache(cm, com.sigma.domain.PuntoMonitoreoObs.class.getName());
            createCache(cm, com.sigma.domain.PuntoMonitoreoObs.class.getName() + ".fotografiaMonitoreos");
            createCache(cm, com.sigma.domain.FotografiaPunto.class.getName());
            createCache(cm, com.sigma.domain.FotografiaMonitoreo.class.getName());
            createCache(cm, com.sigma.domain.Unidades.class.getName());
            createCache(cm, com.sigma.domain.Unidades.class.getName() + ".paramentros");
            createCache(cm, com.sigma.domain.Resultado.class.getName());
            createCache(cm, com.sigma.domain.Resultado.class.getName() + ".resultadoEmisiones");
            createCache(cm, com.sigma.domain.Resultado.class.getName() + ".resultadoMetereologias");
            createCache(cm, com.sigma.domain.ResultadoEmisiones.class.getName());
            createCache(cm, com.sigma.domain.ResultadoMetereologia.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
