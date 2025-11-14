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
    : expression (',' expression)*
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
    : expression op=('*'|'/') expression           # MulDivExpr
    | expression op=('+'|'-') expression           # AddSubExpr
    | expression op=('<'|'>'|'<='|'>='|'=='|'!=') expression  # CompareExpr
    | '-' expression                               # UnaryMinusExpr
    | '!' expression                               # NotExpr
    | functionCall                                 # FuncCallExpr
    | posLiteral                                   # PosLiteralExpr
    | '(' expression ')'                           # ParenExpr
    | literal                                      # LiteralExpr
    | Identifier                                   # IdentifierExpr
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
    : '(' Number ',' Number ',' Number ')'
    ;

Identifier
    : Letter (Letter | Digit | '_')*
    ;

Number
    : Digit+ ('.' Digit+)?
    ;

StringLiteral
    : '"' (~["\\] | '\\' .)* '"'
    ;

BooleanLiteral
    : 'true'
    | 'false'
    ;

fragment Letter
    : [a-zA-Z]
    ;

fragment Digit
    : [0-9]
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;
