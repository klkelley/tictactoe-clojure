(ns tictactoe.game-runner
  (:require [clojure.string :as string]
            [tictactoe.board :refer [available-spaces winner place-move 
                                    build-board make-board game-over? game-results]]
            [tictactoe.ui :refer [print-message clear-screen
                                  menu get-human-move]]))

(def ^:dynamic *sleep-time* 5000)

(def ^:private game-types
  {"1" "Human vs. Human" "2" "Human vs. Impossible Computer"})

(def ^:private board-sizes
  {"1" "3x3" "2" "4x4"})

(def ^:private markers 
  (set (string/split "abcdefghijklmnopqrstuvwxyz" #"")))

(defn ^:private setup-players 
  [choices]
  [{:type :human :marker (choices :player1)} {:type :human :marker (choices :player2)}])

(defn ^:private next-move
  [current-player board]
  (get-human-move (available-spaces board)))

(defn ^:private game-loop 
  [current-player next-player board]
  (let [move (next-move current-player board)
        game-board (place-move move (current-player :marker) board)]
    (clear-screen)
    (print-message (build-board game-board))
    (if (game-over? game-board)
      (print-message (game-results game-board))
      (recur next-player current-player game-board))))

(defn start 
  "Displays game menu and starts the game"
  []
  (let [options (menu game-types board-sizes markers)
        [player1 player2] (setup-players options)
        board (make-board (options :board))]
    (Thread/sleep *sleep-time*)
    (clear-screen)
    (print-message (build-board board))
    (game-loop player1 player2 board)))