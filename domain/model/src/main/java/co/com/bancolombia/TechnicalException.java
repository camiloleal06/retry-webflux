package co.com.bancolombia;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TechnicalException extends RuntimeException {

    public TechnicalException(String message) {
        super(message);
    }
}