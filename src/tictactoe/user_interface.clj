(ns tictactoe.user-interface)

(defn print-message
  ([message]
  	(println message))
  ([message supplementing-info]
  	(println message "=>" supplementing-info)))

(defn get-input []
  (read-line))

(defn loop-and-print [options]
	(doseq [[k v] (map identity options)] (print-message (name k) v)))
