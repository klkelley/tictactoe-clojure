(ns tictactoe.game-runner-test
  (:require [clojure.test :refer [deftest is testing]]
            [tictactoe.game-runner :as game-runner]))

(defn make-input
  [coll]
  (apply str (interleave coll (repeat "\n"))))

(defn lines
  [& args]
  (apply str (interpose "\n" args)))

(deftest test-start-with-all-valid-input
  (testing "Loops through all game options without errors when valid input is received"
    (is (= (lines "Welcome to Tic Tac Toe!"
                  ""
                  "Pick a game type:"
                  "1 => Human vs. Human\n2 => Human vs. Easy Computer"
                  "What size board would you like to play?"
                  "1 => 3x3"
                  "2 => 4x4"
                  "Please pick player 1's marker (A-Z)"
                  "Please pick player 2's marker (A-Z)"
                  "You've chosen:"
                  "game => Human vs. Human"
                  "board => 4x4"
                  "player1 => A"
                  "player2 => B\n")
           (with-out-str ""
             (with-in-str (make-input '("1" "2" "a" "b")) (game-runner/start)))))))


(deftest test-invalid-game-choice
  (testing "Error message is displayed when invalid input for game type is received"
  (is (= (lines "Welcome to Tic Tac Toe!"
                ""
                "Pick a game type:"
                "1 => Human vs. Human\n2 => Human vs. Easy Computer"
                "Invalid. Please pick an available option."
                "What size board would you like to play?"
                "1 => 3x3"
                "2 => 4x4"
                "Please pick player 1's marker (A-Z)"
                "Please pick player 2's marker (A-Z)"
                "You've chosen:"
                "game => Human vs. Human"
                "board => 4x4"
                "player1 => A"
                "player2 => B\n")
         (with-out-str ""
           (with-in-str (make-input '("4" "1" "2" "a" "b")) (game-runner/start)))))))



(deftest test-invalid-marker-choice
  (testing "Error message is displayed when invalid marker choice is received"
    (is (= (lines "Welcome to Tic Tac Toe!"
                  ""
                  "Pick a game type:"
                  "1 => Human vs. Human\n2 => Human vs. Easy Computer"
                  "What size board would you like to play?"
                  "1 => 3x3"
                  "2 => 4x4"
                  "Please pick player 1's marker (A-Z)"
                  "Please pick player 2's marker (A-Z)"
                  "Invalid. Please pick another marker."
                  "You've chosen:"
                  "game => Human vs. Human"
                  "board => 4x4"
                  "player1 => A"
                  "player2 => B\n")
           (with-out-str ""
             (with-in-str (make-input '("1" "2" "a" "a" "b")) (game-runner/start)))))))