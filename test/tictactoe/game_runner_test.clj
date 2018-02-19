(ns tictactoe.game-runner-test
  (:require [clojure.test :refer [deftest is testing]]
            [tictactoe.game-runner :as game-runner]
            [clojure.string :as string]))

(defn make-input
  [coll]
  (apply str (interleave coll (repeat "\n"))))

(defn lines
  [& args]
  (apply str (interpose "\n" args)))

(deftest test-game-play-human-v-human
  (testing "Player one wins the game"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "X" "O" "0" "5" "1" "4" "2")) (game-runner/start))) "Player X wins!" )))))

(deftest test-game-human-v-human-player2-wins
  (testing "Player two wins the game"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "X" "O" "1" "4" "2" "8" "7" "0")) (game-runner/start))) "Player O wins!" )))))

(deftest test-4x4-game-human-v-human-player1-wins
  (testing "Player two wins the game"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "2" "X" "O" "0" "4" "1" "8" "2" "13" "3")) (game-runner/start))) "Player X wins!" )))))

(deftest invalid-marker-choice
  (testing "User will be prompted for different input if input is invalid"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "x" "5" "p" "0" "4" "1" "3" "2")) (game-runner/start))) "Invalid. Please pick another marker." )))))


(deftest invalid-menu-choice
  (testing "User must choose an available menu option"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("3" "1" "1" "x" "p" "0" "4" "1" "3" "2")) (game-runner/start))) "Invalid. Please pick an available option." )))))

(deftest play-tie-game
  (testing "Game can be played to a tie"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "x" "p" "0" "4" "1" "2" "6" "3" "5" "7" "8")) (game-runner/start))) "Its a tie!" )))))

(deftest test-cell-choice-validation
  (testing "User will continue to be prompted for valid input and available spaces"
  (is (= true (string/includes?
         (with-out-str ""
           (with-in-str (make-input '("1" "1" "x" "p" "0" "0" "f" "4" "1" "2" "6" "3" "5" "7" "8")) (game-runner/start))) "Its a tie!" )))))
