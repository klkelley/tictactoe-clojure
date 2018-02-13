(ns tictactoe.marker-validation
	(:require [tictactoe.user-interface :as ui]
						[clojure.string :as string]))

(defn valid-marker-choice?
	([marker]
		(re-matches #"[a-zA-Z]" marker))
	([marker player1-marker]
		(and (re-matches #"[a-zA-Z]" marker) (not= player1-marker (string/upper-case marker)))))

(defn errors 
	([current-marker]
		(if-not (valid-marker-choice? current-marker)
			{:error "Invalid. Please pick another marker."}
			{:error nil}))
	([current-marker other-player-marker]
		(if-not (valid-marker-choice? current-marker other-player-marker)
		  {:error "Invalid. Please pick another marker."}
			{:error nil})))

(defn invalid-choice [errors]
	(ui/print-message (:error errors))
	(ui/get-input))

(defn marker-validation-loop
	([current-marker]
		(let [possible-error (errors current-marker)]
			(if (:error possible-error)
				(recur (invalid-choice possible-error)) current-marker)))
	([current-marker other-player-marker]
		(let [possible-error (errors current-marker other-player-marker)]
			(if (:error possible-error)
				(recur (invalid-choice possible-error) other-player-marker) current-marker))))
