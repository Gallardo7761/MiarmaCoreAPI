package net.miarma.core.common;

public record SingleJsonResponse<T>(T message) {
	public static <T> SingleJsonResponse<T> of(T message) {
		return new SingleJsonResponse<>(message);
	}
}
