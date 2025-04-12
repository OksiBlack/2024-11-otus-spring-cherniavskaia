package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.otus.hw.security.acl.CumulativePermissionGrantingStrategy;

import javax.sql.DataSource;

@EnableConfigurationProperties(value = KeycloakProperties.class)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AclSecurityConfig {
    private final DataSource dataSource;

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(
        AclPermissionEvaluator aclPermissionEvaluator
    ) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator);
        return expressionHandler;
    }

    @Bean
    public AclPermissionEvaluator aclPermissionEvaluator(AclService aclService) {

        return new AclPermissionEvaluator(aclService);
    }

    @Bean
    public JdbcMutableAclService aclService(SpringCacheBasedAclCache aclCache, LookupStrategy lookupStrategy) {
        JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
        jdbcMutableAclService.setAclClassIdSupported(true);

        return jdbcMutableAclService;
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new CumulativePermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public SpringCacheBasedAclCache aclCache(
        PermissionGrantingStrategy permissionGrantingStrategy,
        AclAuthorizationStrategy aclAuthorizationStrategy
    ) {
        final ConcurrentMapCache aclCache = new ConcurrentMapCache("acl_cache");
        return new SpringCacheBasedAclCache(aclCache, permissionGrantingStrategy, aclAuthorizationStrategy);
    }

    @Bean
    public LookupStrategy lookupStrategy(
        SpringCacheBasedAclCache aclCache,
        AclAuthorizationStrategy aclAuthorizationStrategy,
        CumulativePermissionGrantingStrategy permissionGrantingStrategy
    ) {
        BasicLookupStrategy basicLookupStrategy = new BasicLookupStrategy(
            dataSource,
            aclCache,
            aclAuthorizationStrategy,
            permissionGrantingStrategy
        );
        basicLookupStrategy.setAclClassIdSupported(true);
        return basicLookupStrategy;
    }
}
