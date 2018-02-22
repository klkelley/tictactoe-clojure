(ns tictactoe.ui
  (:require [clojure.string :as string]))

(defn print-message
  "Prints the argument passed in"
  ([message]
   (println (str message)))
  ([message supplementing-info]
   (println message "=>" supplementing-info)))

(defn get-input
  "Gets user input and returns the value"
  []
  (read-line))

(defn clear-screen
  "Clears the terminal screen"
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))

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
  ([markers]
   (print-message "Please pick player 1's marker (A-Z):")
   (->
     (get-input)
     (string/lower-case)
     (input-validation-loop markers)
     (string/upper-case)))
  ([markers player1-marker]
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

(defn ^:private ->integer 
  [choice]
  (try
    (Integer. (re-find #"^\d+$" choice))
    (catch Exception e (print-message "Invalid choice!"))))

(defn get-human-move
  "Asks human to pick a cell and will loop if invalid input is received"
  [available-spaces]
  (print-message "Please pick an available cell: ")
  (let [choice (->integer (get-input))]
    (if (and choice (some #(= choice %) available-spaces))
      choice
      (recur available-spaces))))

(defn menu
  "Asks the user for game preferences and returns the prefences chosen"
  [game-types board-sizes markers]
  (print-message "Welcome to Tic Tac Toe!")
  (let [game-choice (pick game-types "Pick a game type: ")
        board-size (pick board-sizes "What size board would you like to play?")
        player-one-marker (pick-marker markers)
        player-two-marker (pick-marker markers player-one-marker)]
    (let [choices {:game (get game-types game-choice)
                   :board (get board-sizes board-size)
                   :player1 player-one-marker
                   :player2 player-two-marker}]
      (chosen-game-options choices)
      choices)))