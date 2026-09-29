package src;

import java.util.Map;

public class Scanner {
    private final String source;
    private int pos = 0;
    private int line = 1;
    private int col = 1;

    private static final Map<String, Token.TokenType> KEYWORDS = Map.of(
            "int", Token.TokenType.INT, "double", Token.TokenType.DOUBLE,
            "bool", Token.TokenType.BOOL, "char", Token.TokenType.CHAR,
            "string", Token.TokenType.STRING_TYPE, "if", Token.TokenType.IF,
            "else", Token.TokenType.ELSE, "while", Token.TokenType.WHILE,
            "fun", Token.TokenType.FUN, "return", Token.TokenType.RETURN
    );

    public Scanner(String source) {
        this.source = source;
    }

    private char peek() {
        if (pos >= source.length()) return '\0';
        return source.charAt(pos);
    }

    private char advance() {
        if (pos >= source.length()) return '\0';
        char c = source.charAt(pos++);
        if (c == '\n') {
            line++;
            col = 1;
        } else {
            col++;
        }
        return c;
    }

    private boolean hasNext() {
        return pos < source.length();
    }

    private void skipWhitespaceAndComments() {
        while (hasNext()) {
            char c = peek();
            if (Character.isWhitespace(c)) {
                advance();
            } else if (c == '/' && pos + 1 < source.length() && source.charAt(pos + 1) == '/') {
                // comentario de linha (//)
                while (hasNext() && peek() != '\n') advance();
            } else if (c == '/' && pos + 1 < source.length() && source.charAt(pos + 1) == '*') {
                // comentario de bloco (/* */)
                advance(); advance(); // consome o /*
                boolean closed = false;
                while (hasNext()) {
                    if (peek() == '*' && pos + 1 < source.length() && source.charAt(pos + 1) == '/') {
                        advance(); advance(); // consome o */
                        closed = true;
                        break;
                    }
                    advance();
                }
                // erro lexico
                if (!closed) {
                    System.err.println("Erro Léxico na linha " + line + ": Comentário de bloco não fechado atingiu o EOF.");
                }
            } else {
                break; // para de ignorar se nao for espaco nem comentario
            }
        }
    }

    public Token nextToken() {
        skipWhitespaceAndComments();

        if (!hasNext()) return new Token(Token.TokenType.EOF, "", line, col);

        int startCol = col;
        int startLine = line;
        char c = advance();

        // identificadores e palavras reservas, as regras estao todas no markdown
        if (Character.isLetter(c)) {
            StringBuilder lexeme = new StringBuilder().append(c);
            while (Character.isLetterOrDigit(peek()) || peek() == '_') {
                lexeme.append(advance());
            }
            String text = lexeme.toString();
            Token.TokenType type = KEYWORDS.getOrDefault(text, Token.TokenType.ID);
            return new Token(type, text, startLine, startCol);
        }

        // literais numericos
        if (Character.isDigit(c)) {
            StringBuilder lexeme = new StringBuilder().append(c);
            while (Character.isDigit(peek())) {
                lexeme.append(advance());
            }
            // ponto decimal opcional
            if (peek() == '.') {
                lexeme.append(advance()); // consome o "."
                if (!Character.isDigit(peek())) {
                    System.err.println("Erro Léxico na linha " + line + ", coluna " + col + ": literal numérico malformado.");
                    return nextToken(); // Modo Pânico: recupera e segue
                }
                while (Character.isDigit(peek())) lexeme.append(advance());
                return new Token(Token.TokenType.DOUBLE_LIT, lexeme.toString(), startLine, startCol);
            }
            return new Token(Token.TokenType.INT_LIT, lexeme.toString(), startLine, startCol);
        }

        // strings; regras tambem no markdown
        if (c == '"') {
            StringBuilder lexeme = new StringBuilder();
            while (hasNext() && peek() != '"' && peek() != '\n') {
                lexeme.append(advance());
            }

            // erro por conta de \n ou EOF
            if (peek() == '\n' || !hasNext()) {
                System.err.println("Erro Léxico na linha " + startLine + ", coluna " + startCol + ": A String não foi fechada na mesma linha.");
                return nextToken(); // continua
            }
            advance();
            return new Token(Token.TokenType.STRING_LIT, lexeme.toString(), startLine, startCol);
        }

        // operadores
        switch (c) {
            case '=': return match('=') ? new Token(Token.TokenType.EQEQ, "==", startLine, startCol) : new Token(Token.TokenType.ASSIGN, "=", startLine, startCol);
            case '<': return match('=') ? new Token(Token.TokenType.LESSEQ, "<=", startLine, startCol) : new Token(Token.TokenType.LESS, "<", startLine, startCol);
            case '>': return match('=') ? new Token(Token.TokenType.GREATEREQ, ">=", startLine, startCol) : new Token(Token.TokenType.GREATER, ">", startLine, startCol);
            case '+': return new Token(Token.TokenType.PLUS, "+", startLine, startCol);
            case '-': return new Token(Token.TokenType.MINUS, "-", startLine, startCol);
            case '*': return new Token(Token.TokenType.MULT, "*", startLine, startCol);
            case '/': return new Token(Token.TokenType.DIV, "/", startLine, startCol);
            case ';': return new Token(Token.TokenType.SEMICOLON, ";", startLine, startCol);
            case '(': return new Token(Token.TokenType.LPAREN, "(", startLine, startCol);
            case ')': return new Token(Token.TokenType.RPAREN, ")", startLine, startCol);
            case '{': return new Token(Token.TokenType.LBRACE, "{", startLine, startCol);
            case '}': return new Token(Token.TokenType.RBRACE, "}", startLine, startCol);
        }

        // tratamento de erro lexico generico
        System.err.println("Erro Léxico na linha " + startLine + ", coluna " + startCol + ": caractere inesperado '" + c + "'");
        return nextToken(); // continua
    }

    // funcao auxiliar pro maximal munch
    private boolean match(char expected) {
        if (peek() == expected) {
            advance(); // consome porque deu "match"
            return true;
        }
        return false;
    }
}