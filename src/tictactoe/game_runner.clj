(ns tictactoe.game-runner
  (:require [clojure.string :as string]
            [tictactoe.board :as board]
            [tictactoe.ui :as ui]
            [tictactoe.ai :as ai]))

(def ^:dynamic *sleep-time* 5000)

(def ^:private game-types
  {"1" "Human vs. Human" "2" "Human vs. Impossible Computer"})

(def ^:private board-sizes
  {"1" "3x3" "2" "4x4"})

(def ^:private markers 
  (set (string/split "abcdefghijklmnopqrstuvwxyz" #"")))

(defn ^:private setup-players 
  [choices]
  (if (= (choices :game) "Human vs. Human")
    [{:type :human :marker (choices :player1)} {:type :human :marker (choices :player2)}]
    [{:type :human :marker (choices :player1)} {:type :computer :marker (choices :player2)}]))

(defmulti ^:private move 
  (fn [current-player next-player board] (current-player :type)))

(defmethod ^:private move :human 
  [current-player next-player board] 
  (ui/get-human-move (board/available-spaces board)))

(defmethod ^:private move :computer 
  [current-player next-player board] 
  (ai/get-computer-move board (current-player :marker) (next-player :marker)))

(defn ^:private next-move
  [current-player next-player board]
  (move current-player next-player board))

(defn ^:private game-loop 
  [current-player next-player board]
  (let [move (next-move current-player next-player board)
        game-board (board/place-move move (current-player :marker) board)]
    (ui/clear-screen)
    (ui/print-message (board/build-board game-board))
    (if (board/game-over? game-board)
      (ui/print-message (board/game-results game-board))
      (recur next-player current-player game-board))))

(defn start 
  "Displays game menu and starts the game"
  []
  (let [options (ui/menu game-types board-sizes markers)
        [player1 player2] (setup-players options)
        board (board/make-board (options :board))]
    (Thread/sleep *sleep-time*)
    (ui/clear-screen)
    (ui/print-message (board/build-board board))
    (game-loop player1 player2 board)))
