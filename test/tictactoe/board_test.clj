(ns tictactoe.board-test
  (:require [clojure.test :refer [deftest is testing]]
            [tictactoe.board :as board]))

(def empty-board [0 1 2 3 4 5 6 7 8])
(def empty-4x4-board [0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15])

(defn lines 
  [& args]
  (apply str (interpose "\n" args)))

(deftest test-grid-size
  (is (= 3 (board/grid-size empty-board))))

(deftest test-4x4-grid-size
  (is (= 4 (board/grid-size empty-4x4-board))))

(deftest test-winner
  (testing "Returns winning marker if a winner exists"
    (is (= "X" (board/winner ["X" "X" "X" 3 4 5 6 7 8])))))

(deftest test-no-winning-marker
  (testing "Returns nil when no winner exists" 
    (= nil (board/winner empty-board))))

(deftest test-game-is-not-over
  (testing "Returns false if game is not won or tied"
    (is (= false (board/game-over? empty-board)))))

(deftest test-game-is-won
  (testing "Returns true if game is won"
    (is (= true (board/game-over? ["X" "X" "X" 3 4 5 6 7 8])))))

(deftest test-game-is-over-and-tied
  (testing "Returns true if game is not won and is tied"
    (is (= true (board/game-over? ["X" "X" "O" "O" "O" "X" "X" "X" "O"])))))

(deftest test-all-available-spaces
  (testing "Returns spaces that haven't been chosen yet"
    (is (= [0 1 2 3 4 5] (board/available-spaces [0 1 2 3 4 5 "X" "O" "X"])))))

(deftest test-place-move
  (testing "Inserts marker on specified cell"
    (is (= ["X" 1 2 3 4 5 6 7 8] (board/place-move 0 "X" empty-board)))))

(deftest test-make-3x3-board
  (is (= empty-board (board/make-board "3x3"))))

(deftest test-make-4x4-board
  (is (= empty-4x4-board (board/make-board "4x4"))))

(deftest test-build-3x3-board
  (is (= (lines  
               "   0   |   1   |   2   "
               "------------------------"
               "   3   |   4   |   5   "
               "------------------------"
               "   6   |   7   |   8   \n") 
    (board/build-board empty-board))))

(deftest test-build-4x4-board
  (is (= (lines  
               "   0   |   1   |   2   |   3   "
               "--------------------------------"
               "   4   |   5   |   6   |   7   "
               "--------------------------------"
               "   8   |   9   |  10   |  11   "
               "--------------------------------"
               "  12   |  13   |  14   |  15   \n")
    (board/build-board empty-4x4-board))))