package api.dto;

public record CompareRequestDto(
    byte[] initialState,
    int timeOutLimit
) {}
