(ns tictactoe.game-runner-test
  (:require [clojure.test :refer [deftest is testing]]
            [tictactoe.game-runner :refer [start]]
            [clojure.string :as string]))

(defn make-input
  [coll]
  (apply str (interleave coll (repeat "\n"))))

(deftest test-game-play-human-v-human
  (testing "Player one wins the game on a 3x3 board via row"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "X" "O" "0" "5" "1" "4" "2")) 
            (start))) "Player X wins!" )))))

(deftest test-game-human-v-human-player2-wins
  (testing "Player two wins the game on a 3x3 board"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "X" "O" "1" "4" "2" "8" "7" "0")) 
            (start))) "Player O wins!" )))))

(deftest test-4x4-game-human-v-human-player1-wins
  (testing "Player one wins the game on a 4x4 board"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "2" "X" "O" "0" "4" "1" "8" "2" "13" "3")) 
            (start))) "Player X wins!" )))))

(deftest invalid-marker-choice
  (testing "User will be prompted for different marker if choice is invalid"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "x" "5" "p" "0" "4" "1" "3" "2")) 
            (start))) "Invalid. Please pick another marker." )))))

(deftest invalid-menu-choice
  (testing "User must choose an available menu option"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("3" "1" "1" "x" "p" "0" "4" "1" "3" "2"))
            (start))) "Invalid. Please pick an available option." )))))

(deftest play-tie-game
  (testing "Game can be played to a tie"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "x" "p" "0" "4" "1" "2" "6" "3" "5" "7" "8")) 
            (start))) "Its a tie!" )))))

(deftest win-game-diagonally
  (testing "Game can be won via left diagonal"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "X" "O" "0" "2" "4" "1" "8")) 
            (start))) "Player X wins!" )))))

(deftest win-game-diagonally-right
  (testing "Game can be won via right diagonal"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "X" "O" "2" "3" "4" "7" "6")) 
            (start))) "Player X wins!" )))))


(deftest win-game-via-column
  (testing "Game can be won via column"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "X" "O" "2" "3" "5" "7" "8")) 
            (start))) "Player X wins!" )))))

(deftest test-cell-choice-validation
  (testing "User will continue to be prompted for valid input and available spaces"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "x" "p" "0" "0" "f" "4" "1" "2" "6" "3" "5" "7" "8")) 
            (game-runner/start))) "Its a tie!" )))))
