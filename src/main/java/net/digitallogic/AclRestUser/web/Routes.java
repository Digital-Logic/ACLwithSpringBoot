package net.digitallogic.AclRestUser.web;

public class Routes {
    public static final String API = "/api";
    public static final String USERS = "/users";
    public static final String ROLES = "/roles";
    public static final String AUTHORITIES = "/authorities";

    public static final String USER_ROUTE = API + USERS;
    public static final String ROLE_ROUTE = API + ROLES;
    public static final String AUTHORITY_ROUTE = API + AUTHORITIES;
    public static final String LOGIN_ROUTE = USER_ROUTE + "/login";
    public static final String SIGN_UP_ROUTE = USER_ROUTE;
}
