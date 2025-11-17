grammar MagicScript;

program
    : statement* EOF
    ;

statement
    : variableDeclaration ';'
    | assignment ';'
    | functionCall ';'
    | ifStatement
    | whileStatement
    | forStatement
    | spellDefinition
    | functionDefinition
    | tryCatch
    | block
    ;

block
    : '{' statement* '}'
    ;

variableDeclaration
    : type Identifier '=' expression
    | 'var' Identifier '=' expression
    ;

assignment
    : Identifier '=' expression
    ;

functionCall
    : Identifier '(' argumentList? ')'
    ;

argumentList
    : expr+=expression (',' expr+=expression)*
    ;

ifStatement
    : 'if' '(' expression ')' statement ('else' statement)?
    ;

whileStatement
    : 'while' '(' expression ')' statement
    ;

forStatement
    : 'for' '(' forInit? ';' expression? ';' assignment? ')' statement
    ;

forInit
    : variableDeclaration
    | assignment
    ;

functionDefinition
    : type Identifier '(' paramList? ')' block
    ;

paramList
    : param (',' param)*
    ;

param
    : type Identifier
    ;

spellDefinition
    : 'spell' Identifier block
    ;

tryCatch
    : 'try' block 'catch' '(' Identifier Identifier ')' block
    ;

expression
    : MINUS expression                                # UnaryMinusExpr
    | expression op=(STAR|SLASH) expression           # MulDivExpr
    | expression op=(PLUS|MINUS) expression           # AddSubExpr
    | expression op=(LT|GT|LE|GE|EQ|NEQ) expression   # CompareExpr
    | expression AND expression                       # LogicAndExpr
    | expression OR expression                        # LogicOrExpr
    | expression DOT Identifier                       # PropertyAccessExpr
    | NOT expression                                  # NotExpr
    | functionCall                                    # FuncCallExpr
    | posLiteral                                      # PosLiteralExpr
    | LPAREN expression RPAREN                        # ParenExpr
    | literal                                         # LiteralExpr
    | Identifier                                      # IdentifierExpr
    ;

type
    : 'int'
    | 'float'
    | 'bool'
    | 'string'
    | 'Pos'
    | 'Entity'
    | 'Magic'
    | 'void'
    ;

literal
    : Number
    | StringLiteral
    | BooleanLiteral
    ;

posLiteral
    : 'pos' LPAREN expression COMMA expression COMMA expression RPAREN
    ;

WS : [ \t\r\n]+ -> skip ;
LINE_COMMENT : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT : '/*' .*? '*/' -> skip ;

PLUS    : '+';
MINUS   : '-';
STAR    : '*';
SLASH   : '/';

LE      : '<=';
GE      : '>=';
EQ      : '==';
NEQ     : '!=';
LT      : '<';
GT      : '>';

AND     : '&&';
OR      : '||';
NOT     : '!';

DOT     : '.';
LPAREN  : '(';
RPAREN  : ')';
LBRACE  : '{';
RBRACE  : '}';
COMMA   : ',';
SEMI    : ';';

BooleanLiteral : 'true' | 'false';
StringLiteral  : '"' (~["\\] | '\\' .)* '"';

Identifier
    : (Letter | '_') (Letter | Digit | '_')*
    ;

Number
    : Digit+ ('.' Digit+)?
    ;

fragment Letter : [a-zA-Z] ;
fragment Digit  : [0-9] ;
