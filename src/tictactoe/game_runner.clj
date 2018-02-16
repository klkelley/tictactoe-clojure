(ns tictactoe.game-runner
  (:require [clojure.string :as string]))

(def game-types
  {"1" "Human vs. Human" "2" "Human vs. Easy Computer"})

(def board-sizes
  {"1" "3x3" "2" "4x4"})

(def markers 
  (set (string/split "abcdefghijklmnopqrstuvwxyz" #"")))

(defn ^:private welcome-message
  []
  "Welcome to Tic Tac Toe!\n")

(defn ^:private game-type-message
  []
  "Pick a game type:")

(defn ^:private board-type-message
  []
  "What size board would you like to play?")

(defn ^:private player-marker-message
  [player]
  (str "Please pick " player "'s marker (A-Z)"))

(defn ^:private game-choices-message
  []
  "You've chosen:")

(defn ^:private print-message
  ([message]
   (println message))
  ([message supplementing-info]
   (println message "=>" supplementing-info)))

(defn ^:private get-input
  []
  (read-line))

(defn ^:private loop-and-print
  [options]
  (doseq [[number choice] (map identity options)]
    (print-message (name number) choice)))

(defn ^:private valid-choice?
  ([options choice]
   (contains? options choice))
  ([options other-marker choice]
   (and (contains? options choice) (not= (string/lower-case other-marker) choice))))

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
  (loop-and-print options)
  (->
    (get-input)
    (input-validation-loop options)
    (str)))

(defn ^:private pick-marker
  ([]
   (print-message (player-marker-message "player 1"))
   (->
     (get-input)
     (string/lower-case)
     (input-validation-loop markers)
     (string/upper-case)))
  ([player1-marker]
   (print-message (player-marker-message "player 2"))
   (->
     (get-input)
     (string/lower-case)
     (input-validation-loop markers player1-marker)
     (string/upper-case))))

(defn ^:private chosen-game-options
  [game-options]
  (print-message (game-choices-message))
  (loop-and-print game-options))

(defn ^:private menu
  [game-types board-sizes]
  (print-message (welcome-message))
  (let [game-choice (pick game-types (game-type-message))
        board-size (pick board-sizes (board-type-message))
        player-one-marker (pick-marker)
        player-two-marker (pick-marker player-one-marker)]
    (let [choices {:game (get game-types game-choice)
                   :board (get board-sizes board-size)
                   :player1 player-one-marker
                   :player2 player-two-marker}]
  (chosen-game-options choices)
  choices)))

(defn start 
  "Starts the game menu and asks the user for game preferences"
  []
  (menu game-types board-sizes))
