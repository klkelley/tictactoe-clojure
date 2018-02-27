(ns tictactoe.ai
  (:require [tictactoe.board :refer [place-move winner game-over? available-spaces]]))

(defn ^:private take-turn
  [depth computer opponent]
  (if (not (zero? (mod depth 2)))
    computer
    opponent))

(defn ^:private scores
  [board computer depth]
  (let [winning-player (winner board)]
    (cond
      (= winning-player computer) (- 10 depth)
      (nil? winning-player) 0
      :else (- depth 10))))

(declare moves-and-scores)

(defn ^:private min-or-max 
  [board-state depth]
  (if (not (zero? (mod depth 2)))
    (apply min (vals board-state))
    (apply max  (vals board-state))))

(defn ^:private get-scores
  [board computer opponent depth]
  (let [board-state board]
    (if (game-over? board-state)
      (scores board-state computer depth)
      (min-or-max (moves-and-scores board-state computer opponent (inc depth)) depth))))

(def memoize-get-scores (future (memoize get-scores)))

(defn ^:private moves-and-scores
  [board computer opponent depth]
  (let [moves (available-spaces board)
        scores (map #(@memoize-get-scores (place-move % (take-turn depth computer opponent) board) 
                                          computer opponent depth) moves)]
    (zipmap (available-spaces board) scores)))

(defn ^:private get-best-move
  [scored-moves]
  (key (apply max-key val scored-moves)))

(defn get-computer-move
  [board computer opponent]
  (let [scored-moves (moves-and-scores board computer opponent 1)]
    (get-best-move scored-moves)))