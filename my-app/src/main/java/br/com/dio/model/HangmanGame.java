package br.com.dio.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import br.com.dio.exception.GameIsFinishedException;
import br.com.dio.exception.LetterAlreadyInputtedException;

import static br.com.dio.model.HangmanGameStatus.LOSE;
import static br.com.dio.model.HangmanGameStatus.PENDING;
import static br.com.dio.model.HangmanGameStatus.WIN;

public class HangmanGame {
    private static final int HANGMAN_INITIAL_LINE_LENGTH = 10;
    private static final int HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR = 12;

    private final int lineSize;
    private final int hangmanInitialSize;
    private final LinkedList<HangmanChar> hangmanPaths;
    private final List<HangmanChar> characters;
    private final List<Character> failAttempts = new ArrayList<>();

    private String hangman;
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters) {
        var whiteSpaces = " ".repeat(characters.size());
        var caracterSpaces = "-".repeat(characters.size());

        this.lineSize = HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR + whiteSpaces.length();
        this.hangmanGameStatus = PENDING;
        this.hangmanPaths = new LinkedList<>(buildHangmanPathsPositions());

        buildHangmanDesign(whiteSpaces, caracterSpaces);
        this.characters = setCharacterSpacesPositionInGame(characters, whiteSpaces.length());
        this.hangmanInitialSize = hangman.length();
    }

    public HangmanGameStatus getHangmanGameStatus() {
        return hangmanGameStatus;
    }

    public void inputCharacter(final char character) {
        if (this.hangmanGameStatus != PENDING) {
            var message = this.hangmanGameStatus == WIN
                    ? "Você já venceu o jogo!"
                    : "Você já perdeu o jogo!";
            throw new GameIsFinishedException(message);
        }

        boolean alreadyVisible = this.characters.stream()
                .filter(c -> c.getCharacter() == character)
                .anyMatch(HangmanChar::isVisible);

        if (alreadyVisible || this.failAttempts.contains(character)) {
            throw new LetterAlreadyInputtedException("A letra '" + character + "' já foi informada.");
        }

        var found = this.characters.stream()
                .filter(c -> c.getCharacter() == character)
                .toList();

        if (found.isEmpty()) {
            failAttempts.add(character);
            if (failAttempts.size() >= 6) {
                this.hangmanGameStatus = LOSE;
            }
            rebuildHangman(this.hangmanPaths.removeFirst());
            return;
        }

        found.forEach(HangmanChar::enableVisibility);

        if (this.characters.stream().noneMatch(HangmanChar::isInvisible)) {
            this.hangmanGameStatus = WIN;
        }

        rebuildHangman(found.toArray(HangmanChar[]::new));
    }

    @Override
    public String toString() {
        return this.hangman;
    }

    private List<HangmanChar> buildHangmanPathsPositions() {
        final int HEAD_LINE = 2;
        final int BODY_LINE = 3;
        final int LEGS_LINE = 4;
        return new ArrayList<>(List.of(
                new HangmanChar('O', this.lineSize * HEAD_LINE + 3),
                new HangmanChar('|', this.lineSize * BODY_LINE + 0),
                new HangmanChar('/', this.lineSize * BODY_LINE + -1),
                new HangmanChar('\\', this.lineSize * BODY_LINE + 1),
                new HangmanChar('/', this.lineSize * LEGS_LINE + -4),
                new HangmanChar('\\', this.lineSize * LEGS_LINE + -2)));
    }

    private List<HangmanChar> setCharacterSpacesPositionInGame(List<HangmanChar> characters, int whiteSpacesAmount) {
        final int LINE_LETTER = 5;

        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).setPosition(
                    this.lineSize * LINE_LETTER + HANGMAN_INITIAL_LINE_LENGTH + i);
        }
        return characters;
    }

    private void rebuildHangman(final HangmanChar... hangmanChars) {
        var hangmanBuilder = new StringBuilder(this.hangman);
        Stream.of(hangmanChars).forEach(h -> {
            hangmanBuilder.setCharAt(h.getPosition(), h.getCharacter());
        });
        var failMessage = failAttempts.isEmpty() ? "" : " Falhas: " + failAttempts;
        this.hangman = hangmanBuilder.substring(0, this.hangmanInitialSize) + failMessage;
    }

    private void buildHangmanDesign(final String whiteSpaces, final String caracterSpaces) {
        this.hangman = "  -----  " + whiteSpaces + System.lineSeparator() +
                " |     | " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator();

    }
}
