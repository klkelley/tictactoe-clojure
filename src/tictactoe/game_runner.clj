(ns tictactoe.game-runner
  (:require [clojure.string :as string]
            [clojure.math.numeric-tower :as math]))

(def ^:dynamic sleep-time 5000)

(def ^:private game-types
  {"1" "Human vs. Human" "2" "Human vs. Impossible Computer"})

(def ^:private board-sizes
  {"1" "3x3" "2" "4x4"})

(def ^:private markers 
  (set (string/split "abcdefghijklmnopqrstuvwxyz" #"")))

(defn ^:private print-message
  ([message]
   (println message))
  ([message supplementing-info]
   (println message "=>" supplementing-info)))

(defn ^:private get-input
  []
  (read-line))

(defn ^:private display-game-menu-options
  [options]
  (doseq [[number choice] (map identity options)]
    (print-message (name number) choice)))

(defn ^:private valid-choice?
  ([options choice]
   (contains? options choice))
  ([options other-marker choice]
   (and (contains? options (string/lower-case choice))
        (not= (string/lower-case other-marker) (string/lower-case choice)))))

(defn ^:private invalid-choice
  [errors options]
  (print-message (:error errors))
  (get-input))

(defn ^:private marker-errors
  [options other-marker current-marker]
  (if-not (valid-choice? options other-marker current-marker)
    {:error "Invalid. Please pick another marker."}
    {:error nil}))

(defn ^:private menu-errors
  [options choice]
  (if-not (valid-choice? options choice)
    {:error "Invalid. Please pick an available option."}
    {:error nil}))

(defn ^:private errors
  ([options choice]
   (menu-errors options choice))
  ([options other-marker choice]
   (marker-errors options other-marker choice)))

(defn ^:private validate-markers
  [current-marker available-markers other-marker]
  (let [possible-error (errors available-markers other-marker current-marker)]
    (if (:error possible-error)
      (recur (invalid-choice possible-error available-markers)
             available-markers other-marker) current-marker)))

(defn ^:private validate-menu-choice
  [choice options]
  (let [possible-error (errors options choice)]
    (if (:error possible-error)
      (recur (invalid-choice possible-error options) options) choice)))

(defn ^:private input-validation-loop
  ([choice options]
   (validate-menu-choice choice options))
  ([choice options other-marker]
   (validate-markers choice options other-marker)))

(defn ^:private pick
  [options options-message]
  (print-message options-message)
  (display-game-menu-options options)
  (->
    (get-input)
    (input-validation-loop options)
    (str)))

(defn ^:private pick-marker
  ([]
   (print-message "Please pick player 1's marker (A-Z):")
   (->
     (get-input)
     (string/lower-case)
     (input-validation-loop markers)
     (string/upper-case)))
  ([player1-marker]
   (print-message "Please pick player 2's marker (A-Z):")
   (->
     (get-input)
     (string/lower-case)
     (input-validation-loop markers player1-marker)
     (string/upper-case))))

(defn ^:private chosen-game-options
  [game-options]
  (print-message "You've chosen: ")
  (display-game-menu-options game-options))

(defn ^:private menu
  [game-types board-sizes]
  (print-message "Welcome to Tic Tac Toe!\n")
  (let [game-choice (pick game-types "Pick a game type: ")
        board-size (pick board-sizes "What size board would you like to play?")
        player-one-marker (pick-marker)
        player-two-marker (pick-marker player-one-marker)]
    (let [choices {:game (get game-types game-choice)
                   :board (get board-sizes board-size)
                   :player1 player-one-marker
                   :player2 player-two-marker}]
      (chosen-game-options choices)
      choices)))

(defn ^:private setup-players 
  [choices]
  [{:type :human :marker (choices :player1)} {:type :human :marker (choices :player2)}])

(defn ^:private make-board 
  [board-choice]
  (if (= "3x3" board-choice)
    (vec (take 9 (range)))
    (vec (take 16 (range)))))

(defn ^:private empty-space? [space]
  (number? space))

(defn ^:private available-spaces
  [board]
  (filter #(empty-space? %) board))

(defn ^:private to-integer 
  [choice]
  (try
    (Integer. (re-find #"^\d+$" choice))
    (catch Exception e nil)))

(defn ^:private place-move 
  [spot marker board]
  (assoc board spot marker))

(defn ^:private get-human-move
  [available-spaces]
  (println "\nPlease pick an available cell: ")
  (let [choice (to-integer (get-input))]
    (if (and choice (some #(= choice %) available-spaces))
      choice
      (recur available-spaces))))

(defn ^:private next-move
  [current-player board]
  (if (= (current-player :type) :human)
    (get-human-move (available-spaces board))))

(defn ^:private row-divider
  [length]
  (->> 
    "--"
    (repeat (* length length))
    (apply str)))

(defn ^:private board-dividers
  [number grid-size]
  (cond (>= number (dec (* grid-size grid-size))) 
          nil
        (= (dec grid-size) (mod number grid-size)) 
          (str "\n"(row-divider grid-size)"\n")
        :else "|"))

(defn ^:private grid-size
  [board]
  (math/sqrt (count board)))

(defn ^:private clear-screen
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))

(defn ^:private display-board 
  [board]
  (println (apply str (map #(str "  "(board %)"  " (board-dividers % (grid-size board)))
                            (range (count board))))))

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

(defn ^:private winner? [board]
  (some #(winner-exists? board %) (winning-combinations board)))

(defn ^:private game-results [board]
  (let [winner (winner? board)]
    (if (nil? winner)
      (println "\nIts a tie!")
      (println "\nPlayer" winner "wins!"))))

(defn ^:private tie? 
  [board]
  (and (not-any? integer? board) (not (winner? board))))

(defn ^:private game-over? 
  [board]
  (or (winner? board) (tie? board)))

(defn ^:private game-loop 
  [current-player next-player board]
  (let [move (next-move current-player board)
        game-board (place-move move (current-player :marker) board)]
    (clear-screen)
    (display-board game-board)
    (if (game-over? game-board)
      (game-results game-board)
      (recur next-player current-player game-board))))

(defn start 
  "Asks the user for game preferences and starts the game"
  []
  (let [options (menu game-types board-sizes)
        [player1 player2] (setup-players options)
        board (make-board (options :board))]
    (Thread/sleep sleep-time)
    (clear-screen)
    (display-board board)
    (game-loop player1 player2 board)))
