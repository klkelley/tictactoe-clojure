(ns tictactoe.game-menu
  (:require [clojure.string :as string]
            [tictactoe.user-interface :as ui]
            [tictactoe.game-messages :as messages]
            [tictactoe.menu-validation :as validator]
            [tictactoe.marker-validation :as marker-validator]))

(defn welcome-message []
 (ui/print-message (messages/welcome)))

(defn pick-game [game-types] 
  (ui/print-message (messages/game-type))
  (ui/loop-and-print game-types)
  (-> 
      (ui/get-input)
      (keyword)
      (validator/input-validation-loop game-types)
      (keyword)))

(defn pick-board-size [board-sizes] 
  (ui/print-message (messages/board-type))
  (ui/loop-and-print board-sizes)
  (-> 
      (ui/get-input)
      (keyword)
      (validator/input-validation-loop board-sizes)
      (keyword)))

(defn pick-player-one-marker []
  (ui/print-message (messages/player-one-marker))
  (-> 
      (ui/get-input)
      (marker-validator/marker-validation-loop)
      (string/upper-case)))

(defn pick-player-two-marker [player1-marker]
  (ui/print-message (messages/player-two-marker))
  (-> 
      (ui/get-input)
      (marker-validator/marker-validation-loop player1-marker)
      (string/upper-case)))

(defn chosen-game-options [game-options]
  (ui/print-message (messages/game-choices))
  (ui/loop-and-print game-options))

(defn menu [game-type board-sizes]
  (welcome-message)
  (let [game-choice (pick-game game-type)
        board-size (pick-board-size board-sizes)
        player-one-marker (pick-player-one-marker)
        player-two-marker (pick-player-two-marker player-one-marker)]

  (let [choices {:game (get game-type game-choice) 
                      :board (get board-sizes board-size) 
                      :player1 player-one-marker 
                      :player2 player-two-marker}]
  (chosen-game-options choices)
   choices)))