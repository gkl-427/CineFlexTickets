package com.example.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerLogger {
    private static final Logger logger = LogManager.getLogger(ServerLogger.class);
    public static void main(String[] args) {
        logger.info("Server started");
        logger.info("Server shutdown");
    }
}
