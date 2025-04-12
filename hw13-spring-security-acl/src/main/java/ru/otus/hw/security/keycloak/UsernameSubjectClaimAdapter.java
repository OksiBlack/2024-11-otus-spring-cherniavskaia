package ru.otus.hw.security.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class UsernameSubjectClaimAdapter implements Converter<Map<String, Object>, Map<String, Object>> {
    private static final String PREFERRED_USERNAME = "preferred_username";

    private static final String SUB_CLAIM = "sub";

    private final MappedJwtClaimSetConverter delegate;

    public UsernameSubjectClaimAdapter() {
        this.delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
    }

    @Override
    public Map<String, Object> convert(@Nonnull Map<String, Object> claims) {
        Map<String, Object> convertedClaims = delegate.convert(claims);

        Optional.ofNullable(convertedClaims)
            .map(claimsMap -> claimsMap.get(PREFERRED_USERNAME))
            .map(Object::toString)
            .ifPresent(username -> convertedClaims.put(SUB_CLAIM, username));

        return convertedClaims;
    }
}
