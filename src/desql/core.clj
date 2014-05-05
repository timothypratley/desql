(ns desql.core
  (:require [instaparse.core :as insta]
            [clojure.string :as string]))


(def parse (insta/parser (clojure.java.io/resource "sql-2003-2.bnf")))

(parse "select x from a where a.y = b.z")


(defn labels [s]
  (string/replace s
                  #"<([^>]*)>"
                  (fn [[_ $1]]
                    (-> $1
                        (string/replace #" " "_")
                        (string/lower-case)))))

(defn eq [s]
  (string/replace s
                  #"::="
                  "="))

(def grammar (labels (eq (slurp (clojure.java.io/resource "sql-92.bnf")))))
(def parse-sql (insta/parser grammar))

(def grammar (slurp (clojure.java.io/resource "sql-92.bnf")))
(def parse-bnf (insta/parser (clojure.java.io/resource "bnf.ebnf")))
(def parse-bnf (insta/parser (clojure.java.io/resource "bnf2.ebnf")))
(insta/transform {:rule-name str} (parse-bnf grammar))
parse-bnf
(parse-bnf "<foo> ::= A ;\n")
(clojure.pprint/pprint (insta/transform {:rule-name str} (parse-bnf
"<SQL terminal character> ::=\n
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
\n")))
