(ns tictactoe.core
  (:gen-class)
  (:require [tictactoe.game-runner :as game-runner]))

(defn -main []
  (game-runner/start))
