package furrymatch.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, furrymatch.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, furrymatch.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, furrymatch.domain.User.class.getName());
            createCache(cm, furrymatch.domain.Authority.class.getName());
            createCache(cm, furrymatch.domain.User.class.getName() + ".authorities");
            createCache(cm, furrymatch.domain.PersistentToken.class.getName());
            createCache(cm, furrymatch.domain.User.class.getName() + ".persistentTokens");
            createCache(cm, furrymatch.domain.Owner.class.getName());
            createCache(cm, furrymatch.domain.Owner.class.getName() + ".pets");
            createCache(cm, furrymatch.domain.Pet.class.getName());
            createCache(cm, furrymatch.domain.Pet.class.getName() + ".photos");
            createCache(cm, furrymatch.domain.Pet.class.getName() + ".firstLikees");
            createCache(cm, furrymatch.domain.Pet.class.getName() + ".secondLikees");
            createCache(cm, furrymatch.domain.Pet.class.getName() + ".searchCriteria");
            createCache(cm, furrymatch.domain.Photo.class.getName());
            createCache(cm, furrymatch.domain.Breed.class.getName());
            createCache(cm, furrymatch.domain.Breed.class.getName() + ".pets");
            createCache(cm, furrymatch.domain.SearchCriteria.class.getName());
            createCache(cm, furrymatch.domain.Likee.class.getName());
            createCache(cm, furrymatch.domain.Likee.class.getName() + ".firstMatches");
            createCache(cm, furrymatch.domain.Likee.class.getName() + ".secondMatches");
            createCache(cm, furrymatch.domain.Match.class.getName());
            createCache(cm, furrymatch.domain.Match.class.getName() + ".chats");
            createCache(cm, furrymatch.domain.Contract.class.getName());
            createCache(cm, furrymatch.domain.Chat.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
