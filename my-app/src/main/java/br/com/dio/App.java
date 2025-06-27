package br.com.dio;

import java.util.stream.Stream;

import br.com.dio.model.HangmanChar;

public class App {
    public static void main(String[] args) {
        var letter = Stream.of(args)
                .map(a -> a.toLowerCase().charAt(0))
                .map(HangmanChar::new).toList();

        System.out.println(letter);
    }
}
