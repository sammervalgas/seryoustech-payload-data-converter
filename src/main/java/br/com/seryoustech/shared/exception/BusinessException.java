package br.com.seryoustech.shared.exception;

/**
 * Exceção de negócio lançada quando uma regra de validação do domínio é violada.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
