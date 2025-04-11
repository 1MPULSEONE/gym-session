package com.lovejazz.gymsession.converters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractResourceRoles(jwt);

        return new JwtAuthenticationToken(jwt, authorities, getPrincipleClaimName(jwt));
    }

    private String getPrincipleClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (principleAttribute != null) {
            claimName = principleAttribute;
        }
        return jwt.hasClaim(claimName) ? jwt.getClaimAsString(claimName) : jwt.getSubject();
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        if (!jwt.hasClaim("resource_access")) {
            return Set.of();
        }

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceId == null || !resourceAccess.containsKey(resourceId)) {
            return Set.of();
        }

        Object resourceObj = resourceAccess.get(resourceId);
        if (!(resourceObj instanceof Map)) {
            return Set.of();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceObj;

        if (!resource.containsKey("roles")) {
            return Set.of();
        }

        Object rolesObj = resource.get("roles");
        if (!(rolesObj instanceof Collection)) {
            return Set.of();
        }

        try {
            return ((Collection<?>) rolesObj).stream().filter(String.class::isInstance).map(role -> "ROLE_" + role.toString()).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        } catch (ClassCastException e) {
            System.err.println("Ошибка приведения типа для ролей: " + e.getMessage());
            return Set.of();
        }
    }
}
