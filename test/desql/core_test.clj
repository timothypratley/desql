(ns desql.core-test
  (:require [clojure.test :refer :all]
            [desql.core :refer :all]))

(deftest basic-test
  (are [x] (vector? x)
    (parse-bnf "<foo> ::= F\n")
    (parse-bnf "<foo> ::= |\n")
    (parse-bnf "<solidus> ::= /\n\n<colon> ::= :\n\n<semicolon> ::= ;\n\n"))
  (are [x] (string? x)
    (translate "<foo> ::= F\n\n<bar> ::= G\n\n")
    (translate "<foo> ::= |\n")))

(deftest helper-test
  (are [x y] (= x y)
    "test_it___out" (snake-case "test it \t out"))
    "\"nothing\"" (enquote "nothing")
    "\"no'thing\"" (enquote "no'thing")
    "'no\"thing'" (enquote "no\"thing"))

(deftest basic-sql-test
  (translate "
<SQL terminal character> ::=\n
		<SQL language character>\n
	|	<SQL embedded language character>\n
\n
<SQL language character> ::=\n
		<simple Latin letter>\n
	|	<digit>\n
	|	<SQL special character>\n
<simple Latin letter> ::=
		<simple Latin upper case letter>
	|	<simple Latin lower case letter>
\n
<simple Latin upper case letter> ::=\n
	A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z\n
\n
<simple Latin lower case letter> ::=\n
	a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z\n
\n
<digit> ::=\n
	0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9\n
<vertical bar> ::= |\n
\n"))

(deftest full-grammar-test
  (translate (slurp (clojure.java.io/resource "basic.bnf"))))

(deftest sql-grammar-test
  (translate (slurp (clojure.java.io/resource "sql-92.bnf"))))
