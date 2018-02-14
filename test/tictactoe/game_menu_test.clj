(ns tictactoe.game-menu-test
  (:require [clojure.test :refer :all]
            [tictactoe.game-menu :refer :all]
            [tictactoe.game-messages :as messages]
            [tictactoe.user-interface :as ui]))

(def game-types
  {:1 "Human vs. Human" :2 "Human vs. Easy Computer"})

(def board-sizes
	{:1 "3x3" :2 "4x4"})

(defn make-input [coll]
  (apply str (interleave coll (repeat "\n"))))
           
(deftest show-welcome-message
  (testing "Welcome message is shown"
  (is (= "Welcome to Tic Tac Toe!\n\n" (with-out-str (welcome-message))))))

(deftest get-game-type-from-user 
  (testing "Returns input as keyword" 
  (with-out-str "test" (ui/print-message (messages/game-type))
  (is (= :1 (with-in-str "1" (pick-game game-types)))))))

(deftest get-board-size-from-user
  (testing "Returns input as keyword when valid choice"
  (with-out-str "test" (ui/print-message (messages/board-type))
  (is (= :1 (with-in-str "1" (pick-board-size board-sizes)))))))

(deftest get-4x4-board-size-from-user
  (testing "Get second board option from user; returns choice as keyword"
  (with-out-str "test" (ui/print-message (messages/board-type))
  (is (= :2 (with-in-str "2" (pick-board-size board-sizes)))))))

(deftest get-player-one-marker
  (testing "Get first players marker and return it"
	(with-out-str "test" (ui/print-message (messages/player-one-marker))
	(is (= "X" (with-in-str "X" (pick-player-one-marker)))))))

(deftest get-player-two-marker
  (testing "Get second players marker and return it"
	(with-out-str "test" (ui/print-message (messages/player-two-marker))
	(is (= "O" (with-in-str "O" (pick-player-two-marker "X")))))))

(deftest show-chosen-game-options
  (testing "Prints out game options chosen by user"
	(with-out-str "test" (ui/print-message (messages/game-choices))
	(is (= "You've chosen:\ngame => 1\nboard => 2\nplayer1 => X\nplayer2 => O\n"
       (with-out-str (chosen-game-options {:game 1 :board 2 :player1 "X" :player2"O"})))))))

(deftest show-game-menu
  (testing "Gets all game options from user and returns hash of options"
  (with-out-str "test" (with-in-str (make-input '(1 2 "a" "b")) (menu game-types board-sizes))))
  (is (= {:game :1 :board :2 :player1 "A" :player2 "B"})))
