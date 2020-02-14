package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) {
		Lexer lexer = new Lexer(new StringReader(input));
		int i=0;
		Token actual=new Token(MODULE, 0, 0, ""), expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch(Error e) {
					if(expected != null)
						fail(e.getMessage());
					/* return; */
				}
			} while(!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	//all advanced functionality test
	@Test
	public void testWhileLoopWithIfElseAndBreak() {
		runtest("while(1==1){\nif(x==1){\nbreak;\n}\nelse{\nx=x-1;}}", 
				new Token(WHILE, 0, 0, "while"),
				new Token(LPAREN,0, 5, "(" ),
				new Token(INT_LITERAL, 0, 6, "1"),
				new Token(EQEQ, 0, 7, "=="),
				new Token(INT_LITERAL, 0, 9, "1"),
				new Token(RPAREN, 0, 10, ")"),
				new Token(LCURLY, 0, 11, "{"),
				new Token(IF, 1, 0, "if"),
				new Token(LPAREN, 1, 2, "("),
				new Token(ID, 1, 3, "x"),
				new Token(EQEQ, 1, 4, "=="),
				new Token(INT_LITERAL, 1, 6, "1"),
				new Token(RPAREN, 1, 7, ")"),
				new Token(LCURLY, 1, 8, "{"),
				new Token(BREAK, 2, 0, "break"),
				new Token(SEMICOLON, 2, 5, ";"),
				new Token(RCURLY, 3, 0, "}"),
				new Token(ELSE, 4, 0, "else"),
				new Token(LCURLY, 4, 4, "{"),
				new Token(ID, 5, 0, "x"),
				new Token(EQL, 5, 1, "="),
				new Token(ID, 5, 2, "x"),
				new Token(MINUS, 5, 3, "-"),
				new Token(INT_LITERAL, 5, 4, "1"),
				new Token(SEMICOLON, 5, 5, ";"),
				new Token(RCURLY, 5, 6, "}"),
				new Token(RCURLY, 5, 7, "}"),
				new Token(EOF, 5, 8, ""));
	}
	
	@Test
	public void testIfElse() {
		runtest("if (x==1) \n{x=x*2;\n}\n else{\nx=x*3;\n}", 
				new Token(IF, 0, 0, "if"),
				new Token(LPAREN,0, 3, "(" ),
				new Token(ID, 0, 4, "x"),
				new Token(EQEQ, 0, 5, "=="),
				new Token(INT_LITERAL, 0, 7, "1"),
				new Token(RPAREN, 0, 8, ")"),
				new Token(LCURLY, 1, 0, "{"),
				new Token(ID, 1, 1, "x"),
				new Token(EQL, 1, 2, "="),
				new Token(ID, 1, 3, "x"),
				new Token(TIMES, 1, 4, "*"),
				new Token(INT_LITERAL, 1, 5, "2"),
				new Token(SEMICOLON, 1, 6, ";"),
				new Token(RCURLY, 2, 0, "}"),
				new Token(ELSE, 3, 1, "else"),
				new Token(LCURLY, 3, 5, "{"),
				new Token(ID, 4, 0, "x"),
				new Token(EQL, 4, 1, "="),
				new Token(ID, 4, 2, "x"),
				new Token(TIMES, 4, 3, "*"),
				new Token(INT_LITERAL, 4, 4, "3"),
				new Token(SEMICOLON, 4, 5, ";"),
				new Token(RCURLY, 5, 0, "}"),
				new Token(EOF, 5, 1, ""));
	}
	
	@Test
	public void testWhileLoop() {
		runtest("while (1==1) \n{x=x*234;}", 
				new Token(WHILE, 0, 0, "while"),
				new Token(LPAREN,0, 6, "(" ),
				new Token(INT_LITERAL, 0, 7, "1"),
				new Token(EQEQ, 0, 8, "=="),
				new Token(INT_LITERAL, 0, 10, "1"),
				new Token(RPAREN, 0, 11, ")"),
				new Token(LCURLY, 1, 0, "{"),
				new Token(ID, 1, 1, "x"),
				new Token(EQL, 1, 2, "="),
				new Token(ID, 1, 3, "x"),
				new Token(TIMES, 1, 4, "*"),
				new Token(INT_LITERAL, 1, 5, "234"),
				new Token(SEMICOLON, 1, 8, ";"),
				new Token(RCURLY, 1, 9, "}"),
				new Token(EOF, 1, 10, ""));
	}
	
	public void testDoubleQuotesWithSpaces() {
		runtest("\"ezStrings\" \" ",
				new Token(STRING_LITERAL, 0,0, "ezStrings"),
				new Token(STRING_LITERAL, 0, 12, ""),
				new Token(EOF, 0,13,"")
				);
	}
	
	@Test
	public void testStringLiteralWithMultipleEscape() {
		runtest("\"\\\"", 
				new Token(STRING_LITERAL, 0, 0, "\\"),
				new Token(EOF, 0, 3, ""));
	}
	
	@Test
	public void testEmptyStringLiteral() {
		runtest("\"\"", 
				new Token(STRING_LITERAL, 0, 0, ""),
				new Token(EOF, 0, 2, ""));
	}
	
	// all basic functionality test
	
	//also checks if keywords are case sensitive
	@Test
	public void testIdentifier() {
		runtest("abc_123=Boolean",
				new Token(ID,0, 0,"abc_123"),
				new Token(EQL,0,7,"="),
				new Token(ID,0,8,"Boolean"),
				new Token(EOF, 0, 15,"")
				);
	}
	
	//Meant to fail to show that cannot start with "_", must be char
	@Test
	public void testIdentifier2() {
		runtest("_bc_123",
				new Token(ID,0, 0,"_bc_123"),
				new Token(EOF, 0, 7,"")
				);
	}
	
	@Test
	public void testIntLiteral() {
		runtest("123123",
				new Token(INT_LITERAL, 0,0,"123123"),
				new Token(EOF, 0, 6, "")
				);
	}
	
	//with superfluous leading zeroes
	@Test
	public void testIntLiteral2() {
		runtest("0000123123",
				new Token(INT_LITERAL, 0,0,"0000123123"),
				new Token(EOF, 0, 10, "")
				);
	}
	
	@Test
	public void testKWs() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false return while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 0, 13, "return"),
				new Token(WHILE, 0, 20, "while"),
				new Token(EOF, 0, 25, ""));
	}

	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				new Token(STRING_LITERAL, 0, 0, ""),
				new Token(EOF, 0, 3, ""));
	}

	@Test
	public void testStringLiteral() {
		runtest("\"\\n\"", 
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}
	
	@Test
	public void testOperators() {
		runtest("/===>=><=<-!=+*", 
				new Token(DIV, 0, 0,"/"),
				new Token(EQEQ, 0, 1,"=="),
				new Token(EQL, 0, 3,"="),
				new Token(GEQ, 0, 4,">="),
				new Token(GT, 0, 6,">"),
				new Token(LEQ, 0, 7,"<="),
				new Token(LT, 0, 9,"<"),
				new Token(MINUS, 0, 10,"-"),
				new Token(NEQ, 0, 11,"!="),
				new Token(PLUS, 0, 13,"+"),
				new Token(TIMES, 0, 14,"*"),
				new Token(EOF, 0, 15, ""));
		
	}
	
	@Test
	public void testPunctuations() {
		runtest(",[{(]});", 
				new Token(COMMA, 0, 0,","),
				new Token(LBRACKET, 0, 1,"["),
				new Token(LCURLY, 0, 2,"{"),
				new Token(LPAREN, 0, 3,"("),
				new Token(RBRACKET, 0, 4,"]"),
				new Token(RCURLY, 0, 5,"}"),
				new Token(RPAREN, 0, 6,")"),
				new Token(SEMICOLON, 0, 7,";"),
				new Token(EOF, 0, 8, ""));		
	}

}
