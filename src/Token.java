package src;

public class Token {
    public enum TokenType {
        // Palavras Reservadas
        INT, DOUBLE, BOOL, CHAR, STRING_TYPE, IF, ELSE, WHILE, FUN, RETURN,
        // Identificadores e Literais
        ID, INT_LIT, DOUBLE_LIT, STRING_LIT,
        // Operadores e Pontuacao
        ASSIGN, EQEQ, LESS, LESSEQ, GREATER, GREATEREQ, PLUS, MINUS, MULT, DIV,
        LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON,
        // EOF
        EOF
    }

    public final TokenType type;
    public final String lexeme;
    public final int line;
    public final int column;

    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("<%s, '%s', [%d:%d]>", type, lexeme, line, column);
    }
}