(ns tictactoe.game-messages-test
  (:require [clojure.test :refer :all]
            [tictactoe.game-messages :refer :all]))

(deftest welcome-message-string 
  (is (= "Welcome to Tic Tac Toe!\n" (welcome))))
 
(deftest game-type-message
 (is (= "Pick a game type: \n" (game-type))))

(deftest board-type-message
  (is (= "What size board would you like to play? \n" (board-type))))

(deftest pick-player-one-marker
  (is (= "Please pick player 1's marker (A-Z)\n" (player-one-marker))))

(deftest pick-player-two-marker
  (is (= "Please pick player 2's marker (A-Z)\n" (player-two-marker))))

(deftest game-choices-message
	(is (= "You've chosen: \n" (game-choices))))
