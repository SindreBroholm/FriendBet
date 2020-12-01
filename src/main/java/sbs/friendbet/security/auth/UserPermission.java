package sbs.friendbet.security.auth;

public enum UserPermission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    ADMIN_READ("course:read"),
    ADMIN_WRITE("course:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
