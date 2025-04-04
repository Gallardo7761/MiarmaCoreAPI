package net.miarma.api.util;

import io.vertx.ext.web.Router;
import net.miarma.api.common.Constants;

public class RouterUtil {

    public static void attachLogger(Router router) {
        router.route().handler(ctx -> {
            long startTime = System.currentTimeMillis();

            ctx.addBodyEndHandler(_ -> {
                long duration = System.currentTimeMillis() - startTime;

                String method = ctx.request().method().name();
                String path = ctx.normalizedPath();
                String query = ctx.request().query();
                int status = ctx.response().getStatusCode();

                String statusMessage = getStatusMessage(status);
                String emoji = getEmoji(status);

                String formattedQuery = (query != null && !query.isEmpty()) ? "?" + query : "";

                // No ANSI colors porque los loggers no los renderizan en muchos entornos (a menos que uses uno con soporte explícito)
                String log = String.format(
                        "%s [%d %s] %s %s%s (⏱ %dms)",
                        emoji,
                        status,
                        statusMessage,
                        method,
                        path,
                        formattedQuery,
                        duration
                );

                Constants.LOGGER.info(log);
            });

            ctx.next();
        });
    }

    private static String getStatusMessage(int code) {
        return switch (code) {
            case 100 -> "Continue";
            case 101 -> "Switching Protocols";
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 204 -> "No Content";
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 304 -> "Not Modified";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 415 -> "Unsupported Media Type";
            case 422 -> "Unprocessable Entity";
            case 500 -> "Internal Server Error";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            default -> "Unknown";
        };
    }

    private static String getEmoji(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) return "✅";
        if (statusCode >= 300 && statusCode < 400) return "🔁";
        if (statusCode >= 400 && statusCode < 500) return "❌";
        if (statusCode >= 500) return "💥";
        return "📥";
    }
}
