(ns tictactoe.board-test
  (:require [clojure.test :as test]
            [tictactoe.board :as board]))

(def empty-board [0 1 2 3 4 5 6 7 8])
(def empty-4x4-board [0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15])

(defn lines 
  [& args]
  (apply str (interpose "\n" args)))

(test/deftest test-grid-size
  (test/is (= 3 (board/grid-size empty-board))))

(test/deftest test-4x4-grid-size
  (test/is (= 4 (board/grid-size empty-4x4-board))))

(test/deftest test-winner
  (test/testing "Returns winning marker if a winner exists"
    (test/is (= "X" (board/winner ["X" "X" "X" 3 4 5 6 7 8])))))

(test/deftest test-no-winning-marker
  (test/testing "Returns nil when no winner exists" 
    (test/is (= nil (board/winner empty-board)))))

(test/deftest test-game-is-not-over
  (test/testing "Returns false if game is not won or tied"
    (test/is (= false (board/game-over? empty-board)))))

(test/deftest test-game-is-won
  (test/testing "Returns true if game is won"
    (test/is (= true (board/game-over? ["X" "X" "X" 3 4 5 6 7 8])))))

(test/deftest test-game-is-over-and-tied
  (test/testing "Returns true if game is not won and is tied"
    (test/is (= true (board/game-over? ["X" "X" "O" "O" "O" "X" "X" "X" "O"])))))

(test/deftest test-all-available-spaces
  (test/testing "Returns spaces that haven't been chosen yet"
    (test/is (= [0 1 2 3 4 5] (board/available-spaces [0 1 2 3 4 5 "X" "O" "X"])))))

(test/deftest test-place-move
  (test/testing "Inserts marker on specified cell"
    (test/is (= ["X" 1 2 3 4 5 6 7 8] (board/place-move 0 "X" empty-board)))))

(test/deftest test-make-3x3-board
  (test/is (= empty-board (board/make-board "3x3"))))

(test/deftest test-make-4x4-board
  (test/is (= empty-4x4-board (board/make-board "4x4"))))

(test/deftest test-build-3x3-board
  (test/is (= (lines  
               "   0   |   1   |   2   "
               "------------------------"
               "   3   |   4   |   5   "
               "------------------------"
               "   6   |   7   |   8   \n") 
    (board/build-board empty-board))))

(test/deftest test-build-4x4-board
  (test/is (= (lines  
               "   0   |   1   |   2   |   3   "
               "--------------------------------"
               "   4   |   5   |   6   |   7   "
               "--------------------------------"
               "   8   |   9   |  10   |  11   "
               "--------------------------------"
               "  12   |  13   |  14   |  15   \n")
    (board/build-board empty-4x4-board))))