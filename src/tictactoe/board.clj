(ns tictactoe.board
  (:require [clojure.math.numeric-tower :as math]))

(defn make-board 
  "Returns 3x3 or 4x4 one-dimensional vector"
  [board-choice]
  (if (= "3x3" board-choice)
    (vec (take 9 (range)))
    (vec (take 16 (range)))))

(defn grid-size
  "Returns the grid size of the board"
  [board]
  (math/sqrt (count board)))

(defn ^:private empty-space? 
  [space]
  (number? space))

(defn available-spaces
  "Finds all spaces that are available on the board"
  [board]
  (filter empty-space? board))

(defn ^:private rows 
  [board]
  (partition (grid-size board) (range (count board))))

(defn ^:private columns 
  [board]
  (apply map vector (rows board)))

(defn ^:private left-diagonal
  [board]
  (reduce #(conj %1 (+ 0 (* %2 (inc (grid-size board))))) 
          [] (range (grid-size board))))

(defn ^:private right-diagonal
  [board]
  (let [right-corner (dec (grid-size board))]
  (reduce #(conj %1 (+ right-corner (* %2 right-corner)))
          [right-corner] (range 1 (grid-size board)))))

(defn ^:private diagonals
  [board]
  [(left-diagonal board) 
  (right-diagonal board)])

(defn place-move 
  "Updates the board by placing the marker on the specified spot"
  [spot marker board]
  (assoc board spot marker))

(defn ^:private winning-combinations 
  [board]
  (apply concat [(rows board) (columns board) (diagonals board)]))

(defn ^:private has-three-in-a-row?
  [board combo]
  (= 1 (count (set (map #(board %) combo)))))

(defn ^:private winner-exists?
  [board combo]
  (if (has-three-in-a-row? board combo)
    (board (first combo))
    nil))

(defn winner
  "Finds the winning player's marker on the board"
  [board]
  (some #(winner-exists? board %) (winning-combinations board)))

(defn ^:private winner?
  [board]
  (some? (winner board)))

(defn ^:private tie? 
  [board]
  (and (not-any? integer? board) (not (winner? board))))

(defn game-over? 
  "Determines if the game is won or tied"
  [board]
  (or (winner? board) (tie? board)))

(defn game-results 
  "Returns a string representing the game results"
  [board]
  (let [winner (winner board)]
    (if (not (nil? winner))
      (str "Player " winner " wins!")
      "Its a tie!")))

(defn ^:private row-divider
  [length]
  (->> 
    "--"
    (repeat (+ (* length 3) length))
    (apply str)))

(defn ^:private board-dividers
  [number grid-size]
  (cond (>= number (dec (* grid-size grid-size))) 
          "\n"
        (= (dec grid-size) (mod number grid-size)) 
          (str "\n"(row-divider grid-size)"\n")
        :else (str "|")))

(defn ^:private make-space 
  [amount]
  (apply str (repeat amount \space)))

(defn ^:private format-cell 
  [cell]
  (let [length (count (str cell))
        rightside (quot (- 7 length) 2)
        leftside (- 7 (+ length rightside))]
  (str (make-space rightside) cell (make-space leftside))))

(defn build-board 
  "Builds a 3x3 board or 4x4 board for game play"
  [board]
  (let [board-size (range (count board))
        grid (grid-size board)
        formatted-board (map #(str (format-cell (board %)) 
                          (board-dividers % grid)) board-size)]
  (apply str formatted-board)))
