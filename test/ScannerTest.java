package test;

import src.Scanner;
import src.Token;

public class ScannerTest {
    public static void main(String[] args) {
        System.out.println("Teste 1: Casos de Token individuais");
        runTest("patrick_mahomes", Token.TokenType.ID);
        runTest("landonorris4", Token.TokenType.ID);
        runTest("\"O New York Giants é o maior time da NFL!\"", Token.TokenType.STRING_LIT);
        runTest("while", Token.TokenType.WHILE);
        runTest("==", Token.TokenType.EQEQ);
        runTest("3.14", Token.TokenType.DOUBLE_LIT);

        System.out.println("\nTeste 2: Trecho de Código Rodando");
        String codigo = "int total = (pontos + 10); // calcula total\n return total;";
        Scanner sc = new Scanner(codigo);
        Token t;
        do {
            t = sc.nextToken();
            System.out.println(t);
        } while (t.type != Token.TokenType.EOF);

        System.out.println("\nTeste 3: erros");
        // 3 erros obrigatorios
        String erros = "\"string esquecida sem fechar\n @ \n /* comentario esquecido";
        Scanner scErro = new Scanner(erros);
        do {
            t = scErro.nextToken();
        } while (t.type != Token.TokenType.EOF);

    }

    private static void runTest(String input, Token.TokenType expected) {
        Scanner sc = new Scanner(input);
        Token t = sc.nextToken();
        if (t.type == expected) {
            System.out.println("PASSOU: " + input + " -> " + t.type);
        } else {
            System.err.println("FALHOU: " + input + " -> Esperava " + expected + " mas deu " + t.type);
        }
    }
}