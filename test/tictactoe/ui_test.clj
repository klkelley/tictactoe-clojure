(ns tictactoe.ui-test
  (:require [clojure.test :refer [deftest is testing]]
            [tictactoe.ui :as ui]
            [clojure.string :as string]))

(def ^:private game-types
  {"1" "Human vs. Human" "2" "Human vs. Impossible Computer"})

(def ^:private board-sizes
  {"1" "3x3" "2" "4x4"})

(def ^:private markers 
  (set (string/split "abcdefghijklmnopqrstuvwxyz" #"")))

(defn lines 
  [& args]
  (apply str (interpose "\n" args)))

(defn make-input
  [coll]
  (apply str (interleave coll (repeat "\n"))))

(deftest test-print-message
  (testing "prints message as string if one parameter is received"
    (is (= "TicTacToe!\n"
      (with-out-str "" (ui/print-message "TicTacToe!"))))))

(deftest test-print-message-with-supplementing-info
  (testing "prints message with arrow if supplementing info is received"
    (is (= "Winner => Player X\n"
      (with-out-str "" (ui/print-message "Winner" "Player X"))))))

(deftest test-get-input
  (testing "Reads input entered by user"
    (is (= "X" (with-in-str "X" (ui/get-input))))))

(deftest test-menu 
  (testing "Menu is displayed to user and summary is shown after user picks choices"
    (is (= (lines "Welcome to Tic Tac Toe!"
                  "Pick a game type: "
                  "1 => Human vs. Human"
                  "2 => Human vs. Impossible Computer"
                  "What size board would you like to play?"
                  "1 => 3x3"
                  "2 => 4x4"
                  "Please pick player 1's marker (A-Z):"
                  "Please pick player 2's marker (A-Z):"
                  "You've chosen: "
                  "game => Human vs. Human"
                  "board => 3x3"
                  "player1 => X"
                  "player2 => O\n")
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O"))
          (ui/menu game-types board-sizes markers)))))))
