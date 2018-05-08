(ns tictactoe.random-ai
  (:require [tictactoe.board :as board]))

(defn ^:private get-random-move
  [board]
  (let [spaces (board/available-spaces board)]
    (rand-nth spaces)))

(defn get-computer-move
  "Computer chooses a move at random"
  [board computer opponent]
  (get-random-move board))

