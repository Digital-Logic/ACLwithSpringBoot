package net.digitallogic.AclRestUser.security;

public enum Roles {

    USER("USER"),
    ADMIN("ADMIN")
    ;

    public final String name;
    private Roles(String name) { this.name = name; }
}
