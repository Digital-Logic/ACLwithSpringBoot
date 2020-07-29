package net.digitallogic.AclRestUser.security;

public enum Authority {

    ADMIN_USER("ADMIN_USER"),
    CREATE_USER("CREATE_USER"),
    READ_USER("READ_USER"),
    UPDATE_USER("UPDATE_USER"),
    DELETE_USER("DELETE_USER"),
    ;


    public final String name;
    private Authority(String name){ this.name = name; }
}
