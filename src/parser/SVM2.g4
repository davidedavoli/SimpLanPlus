grammar SVM2;

@header {
import java.util.HashMap;
}

@lexer::members {
public int lexicalErrors=0;
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

assembly: (instruction)* ;

instruction:
    (
    PUSH n=NUMBER
	  | PUSH r1=REGISTER
	  | POP
	  | POP r1=REGISTER
	  | ADD r1=REGISTER r2=REGISTER r3=REGISTER
	  | ADDI r1=REGISTER r2=REGISTER n=NUMBER
	  | SUB r1=REGISTER r2=REGISTER r3=REGISTER
	  | SUBI r1=REGISTER r2=REGISTER n=NUMBER
	  | MULT r1=REGISTER r2=REGISTER r3=REGISTER
	  | MULTI r1=REGISTER r2=REGISTER n=NUMBER
	  | DIV r1=REGISTER r2=REGISTER r3=REGISTER
	  | DIVI r1=REGISTER r2=REGISTER n=NUMBER
	  | OR r1=REGISTER r2=REGISTER r3=REGISTER
	  | NOT r1=REGISTER r2=REGISTER
	  | STOREW r1=REGISTER o=NUMBER LPAR r2=REGISTER RPAR
	  | LOADW r1=REGISTER o=NUMBER LPAR r2=REGISTER RPAR
	  | LOAD r1=REGISTER n=NUMBER
	  | BRANCH l=LABEL
	  | BCOND r1=REGISTER l=LABEL
	  | EQ r1=REGISTER r2=REGISTER r3=REGISTER
	  | LE r1=REGISTER r2=REGISTER r3=REGISTER
	  | LT r1=REGISTER r2=REGISTER r3=REGISTER
	  | GT r1=REGISTER r2=REGISTER r3=REGISTER
      | GE r1=REGISTER r2=REGISTER r3=REGISTER
/*	  | JS
	  | LOADRA
	  | STORERA
	  | LOADRV
	  | STORERV
	  | LOADFP
	  | STOREFP
	  | COPYFP
	  | LOADHP
	  | STOREHP
*/
      | FREE r1=REGISTER
      | NEW r1=REGISTER
	  | PRINT r1=REGISTER
	  | PRINT
	  | HALT
	  | ADDRESS // fake production that allows us to work with addresses as instructions while resolving labels
	  | l=LABEL COL
	  ) ;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

REGISTER    : '$'((('a'|'r')('0'..'9'))|('sp'|'fp'|'hp'|'rv'|'ra'));

PUSH  : 'push' ;
ADDRESS  : 'address' ;
POP	 : 'pop' ;
ADD	 : 'add' ;  	// add two values from two registers in a third
ADDI	 : 'addi' ;  	// add an integer to a value from a register and stores the result in a second register
SUB	 : 'sub' ;	// as for add
SUBI	 : 'subi' ;	// as for addi
MULT	 : 'mult' ;	// as for add
MULTI	 : 'multi' ;	// as for addi
DIV	 : 'div' ;	// as for add
DIVI	 : 'divi' ;	// as for addi
NOT	     : 'not' ;	// logical negation
OR	     : 'or' ;	// logical negation
STOREW	 : 'sw' ; 	// stores the vaue of a register at offset n from the address in a second register
LOADW	 : 'lw' ;	// loads the value at offset n from the address in a register ans sotres it in a second register
BRANCH	 : 'b' ;	// jump to label
BCOND    : 'bc' ;	// jump to label if $r1 == top
LE       : 'le' ;	// r1 = r2 <= r3
LT       : 'lt' ;	//
EQ       : 'eq' ;	//
GE       : 'ge' ;	//
GT       : 'gt' ;	//
/*
JS	 : 'js' ;	// jump to instruction pointed by top of stack and store next instruction in ra
LOADRA	 : 'lra' ;	// load from ra
STORERA  : 'sra' ;	// store top into ra
LOADRV	 : 'lrv' ;	// load from rv
STORERV  : 'srv' ;	// store top into rv
LOADFP	 : 'lfp' ;	// load frame pointer in the stack
STOREFP	 : 'sfp' ;	// store top into frame pointer
COPYFP   : 'cfp' ;      // copy stack pointer into frame pointer
LOADHP	 : 'lhp' ;	// load heap pointer in the stack
STOREHP	 : 'shp' ;	// store top into heap pointer
*/
PRINT	 : 'print' ;	// print top of stack
LOAD	 : 'li' ;	// loads an integer in the register
HALT	 : 'halt' ;	// stop execution
FREE	 : 'free' ;	// frees the address in top
NEW	     : 'new' ;	// allocates a new cell of memory and pushes the result in top

COL	 : ':' ;
LPAR	 : '(' ;
RPAR	 : ')' ;
LABEL	 : ('a'..'z'|'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9')* ;
NUMBER	 : '0' | ('-')?(('1'..'9')('0'..'9')*) ;

WHITESP  : ( '\t' | ' ' | '\r' | '\n' )+   -> channel(HIDDEN);
LINECOMMENTS 	: '//' (~('\n'|'\r'))* -> channel(HIDDEN);
BLOCKCOMMENTS   : '/*'( ~('/'|'*')|'/'~'*'|'*'~'/'|BLOCKCOMMENTS)* '*/' -> channel(HIDDEN);

ERR   	 : . { System.err.println("Invalid char: "+ getText()); lexicalErrors++;  } -> channel(HIDDEN);

