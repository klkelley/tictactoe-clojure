(ns tictactoe.menu-validation
	(:require [tictactoe.user-interface :as ui]))

(defn valid-choice? [options choice]
  (contains? options choice))

(defn invalid-choice [errors options]
  (ui/print-message (:error errors))
  (keyword (ui/get-input)))

(defn errors [options choice]
  (if-not (valid-choice? options choice)
    {:error "Invalid. Please pick an available option."}
    {:error nil}))

(defn input-validation-loop [choice options]
  (let [possible-error (errors options choice)]
    (if (:error possible-error)
      (recur (invalid-choice possible-error options) options) choice)))
