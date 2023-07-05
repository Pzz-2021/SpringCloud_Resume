package com.resume.position.utils;

public class RedisConstants {
    public static final String LOCK_KEY = "lock:";
    public static final String CACHE_POSITION_KEY = "cache:position:";
    public static final String CACHE_POSITION_TEAM_KEY = "cache:position-team:";

    private static final Long SIXTY = 60L;

    public static final Long CACHE_NULL_TTL = 30L;
    public static final Long CACHE_POSITION_TTL = 30L * SIXTY;
    public static final Long CACHE_POSITION_TEAM_TTL = 30L * SIXTY;



}
