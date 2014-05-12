(ns desql.core
  (:require [instaparse.core :as insta]
            [clojure.string :as string]))

(def parse-bnf (insta/parser (clojure.java.io/resource "bnf2.ebnf")))

(defn snake-case [s]
  (string/replace s #"\s" "_"))

(defn enquote [identifier]
  (if (and (not (.contains identifier "'"))
           (.contains identifier "\""))
    (str \' identifier \')
    (str \" (string/replace identifier #"\"" "\\\"") \"))) 

(defmulti render first)
(defmethod render :literal [[t v]] (render v))
(defmethod render :identifier [[t v]] (enquote v))
(defmethod render :reserved [[t v]] (enquote v))
(defmethod render :default [v] v)

(def bnf->ebnf
  {:syntax (fn [& rules]
             (string/join \newline rules))
   :rule-name (comp snake-case str)
   :rule (fn [rule-name & body]
           (str rule-name " = "
                (string/join " | " (map render body))))})

(defn translate [s]
  (insta/transform bnf->ebnf (parse-bnf s)))
