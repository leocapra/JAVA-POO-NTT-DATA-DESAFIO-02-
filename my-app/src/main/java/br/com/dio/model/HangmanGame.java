package br.com.dio.model;

import java.util.List;

import static br.com.dio.model.HangmanGameStatus.PENDING;

public class HangmanGame {

    private final static int HANGMAN_INITIAL_LINE_LENGTH = 9;
    private final static int HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR = 10;

    private final int lineSize;
    // private final List<HangmanChar> hangmanPaths;
    private final List<HangmanChar> characters;

    private String hangman;
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters) {
        var whiteSpaces = " ".repeat(characters.size());
        var caracterSpaces = "-".repeat(characters.size());
        this.lineSize = HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR + whiteSpaces.length();
        this.hangmanGameStatus = PENDING;
        buildHangmanDesign(whiteSpaces, caracterSpaces);
        this.characters = setCharacterSpacesPositionInGame(characters, whiteSpaces.length());
    }

    @Override
    public String toString() {
        return this.hangman;
    }

    private List<HangmanChar> setCharacterSpacesPositionInGame(
            final List<HangmanChar> characters,
            final int whiteSpacesAmount) {
        final int LINE_LETTER = 6;

        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).setPosition(
                    this.lineSize * LINE_LETTER + HANGMAN_INITIAL_LINE_LENGTH + i);
        }

        return characters;
    }

    private void buildHangmanDesign(final String whiteSpaces, final String caracterSpaces) {
        this.hangman = "  -----  " + whiteSpaces + System.lineSeparator() +
                " |     | " + whiteSpaces + System.lineSeparator() +
                " |     | " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                "========" + caracterSpaces + System.lineSeparator();

    }
}
