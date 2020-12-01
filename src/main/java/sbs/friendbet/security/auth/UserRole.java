package sbs.friendbet.security.auth;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static sbs.friendbet.security.auth.UserPermission.*;

public enum UserRole {
    USER(Sets.newHashSet(USER_READ, USER_WRITE)),
    ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, ADMIN_READ, ADMIN_WRITE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthority(){
        Set<SimpleGrantedAuthority> permission = getPermissions().stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.getPermission()))
                .collect(Collectors.toSet());
        permission.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permission;
    }
}
