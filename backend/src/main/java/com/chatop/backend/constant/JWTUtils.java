package com.chatop.backend.constant;

public class JWTUtils {
    public static final long EXPIRE_ACCESS_TOKEN = 24*60*60*1000;

    public static final long EXPIRE_REFRESH_TOKEN = 7*24*60*60*1000;

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String ISSUER = "springBootApp";

    public static final String SECRET_KEY = "M1wqm7MjDC02Nmq7aGSkIxYIeX6RTunlm1YvHsogksHRXJO9ZhZMwJkkPREbon1y";

    public static final String PUBLIC_KEY = "TTqlIRjKQjD57nTzFtUFJhKOGd7GIo4DPzIBjyYdDo96OdIfbv5BnNsxmbjq1vw8";

    public static final String AUTH_HEADER = "Authorization";
}
