# Especificação Léxica - BagugaScript (.baguga)
**Alunos**: Mateus Ferreira Milken - 12321BCC021 e Ruan Marra de Barros - 12321BCC003

## 1. Categorias de Tokens
| Token | Notação (regex/EBNF) | Exemplos válidos | Decisões de Implementação |
| :--- | :--- | :--- | :--- |
| **Identificador** | `letra (letra \| dígito \| "_")*` | `baguga`, `patrick_mahomes` , `landonorris4` , `n` | a linguagem é case-sensitive e `_` é permitido no meio ou fim |
| **Palavra reservada** | mesmo padrão do ID + tabela | `if`, `while`, `int` | resolvido após o maximal munch do identificador e obrigatoriamente letras minusculas|
| **String** | `" (qualquer caractere ≠ " e ≠ \n) * "` | `"touchdown"`, `"chiefs 2026"` | sem escapes, e encontrar `\n` ou `EOF` antes de fechar gera um erro léxico, a gente precisa exclusivamente das "" pra fechar |
| **Operador** | `1 ou 2 caracteres` | `=`, `==`, `<=`, `+` | aplica-se a regra de maximal munch para operadores compostos |
| **Literal numérico** | `dígito+ ( "." dígito+ )?` | `42`, `3.14`, `0` | ponto decimal opcional, não tem notação científica ou hexadecimal |

## 2. Regras e Definições Obrigatórias
* **Alfabeto de entrada:** Apenas os caracteres da tabela ASCII
* **Sensibilidade a maiúsculas (Case-sensitive):** A linguagem diferencia letras maiúsculas de minúsculas, e palavras reservadas seguem regras estritas de minúsculas
* **Espaços e Comentários:** Espaços, tabulações e quebras de linha são ignorados, e comentários de linha usam `//` enquanto comentários de bloco usam `/*` e `*/`, sem aninhamento permitido. O fim de arquivo no meio de um comentário de bloco é um erro léxico
* **Regra de Desambiguação (Maximal Munch):** O scanner consome o maior prefixo válido possível antes de decidir o token
* **Lista de Palavras Reservadas:** `int`, `double`, `bool`, `char`, `string`, `if`, `else`, `while`, `fun`, `return`