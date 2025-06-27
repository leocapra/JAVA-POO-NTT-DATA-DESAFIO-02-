package br.com.dio;

import java.util.Scanner;
import java.util.stream.Stream;

import br.com.dio.exception.GameIsFinishedException;
import br.com.dio.exception.LetterAlreadyInputtedException;
import br.com.dio.model.HangmanChar;
import br.com.dio.model.HangmanGame;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (args.length == 0) {
            System.out.println("Nenhuma palavra foi passada como argumento!");
            System.exit(1);
        }

        var characters = Stream.of(args)
                .flatMap(word -> word.chars().mapToObj(c -> (char) c))
                .map(Character::toLowerCase)
                .map(HangmanChar::new)
                .toList();

        var hangmanGame = new HangmanGame(characters);

        System.out.println("Bem-vindo ao Jogo da Forca!");
        System.out.println(hangmanGame);

        while (true) {
            System.out.println("\nSelecione uma das opções: ");
            System.out.println("1 - Informar uma letra");
            System.out.println("2 - Verificar status do jogo");
            System.out.println("3 - Sair do jogo");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida! Digite um número.");
                continue;
            }

            switch (option) {
                case 1:
                    System.out.println("Informe uma letra: ");
                    var input = scanner.nextLine();
                    if (input.isEmpty()) {
                        System.out.println("Nenhuma letra informada.");
                        continue;
                    }
                    var character = Character.toLowerCase(input.charAt(0));

                    try {
                        hangmanGame.inputCharacter(character);
                    } catch (LetterAlreadyInputtedException ex) {
                        System.out.println(ex.getMessage());
                    } catch (GameIsFinishedException ex) {
                        System.out.println(ex.getMessage());
                        System.out.println(hangmanGame);
                        System.exit(0);
                    }
                    System.out.println(hangmanGame);
                    break;

                case 2:
                    System.out.println("Status do jogo: " + hangmanGame.getHangmanGameStatus());
                    break;

                case 3:
                    System.out.println("Saindo do jogo...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        }
    }
}
