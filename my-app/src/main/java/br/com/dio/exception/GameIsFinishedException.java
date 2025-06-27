package br.com.dio.exception;

public class GameIsFinishedException extends RuntimeException {

    public GameIsFinishedException(String message) {
        super(message);
    }

}
