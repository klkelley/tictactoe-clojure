(ns tictactoe.ai
  (:require [tictactoe.board :as board]))

(defn ^:private take-turn
  [depth computer opponent]
  (if (not (zero? (mod depth 2)))
    computer
    opponent))

(defn ^:private scores
  [board computer depth]
  (let [winning-player (board/winner board)]
    (cond
      (= winning-player computer) (- 10 depth)
      (nil? winning-player) 0
      :else (- depth 10))))

(declare moves-and-scores)

(defn ^:private min-or-max 
  [board-state depth]
  (if (not (zero? (mod depth 2)))
    (apply min (vals board-state))
    (apply max (vals board-state))))

(defn ^:private get-scores
  [board computer opponent depth]
  (let [board-state board]
    (if (board/game-over? board-state)
      (scores board-state computer depth)
      (min-or-max (moves-and-scores board-state computer opponent (inc depth)) depth))))

(def memoize-get-scores (memoize get-scores))

(defn ^:private moves-and-scores
  [board computer opponent depth]
  (let [moves (board/available-spaces board)]
  (zipmap moves ((if (= depth 1) pmap map) #(memoize-get-scores (board/place-move % (take-turn depth computer opponent) board) 
                                            computer opponent depth) moves))))

(defn ^:private get-best-move
  [scored-moves]
  (key (apply max-key val scored-moves)))

(defn get-computer-move
  "Computer chooses best move to maximize win"
  [board computer opponent]
  (let [depth 1
        scored-moves (moves-and-scores board computer opponent depth)]
    (get-best-move scored-moves)))
