(ns tictactoe.user-interface)

(defn print-message [message]
  (println (str message)))

(defn get-input []
  (read-line)
)
