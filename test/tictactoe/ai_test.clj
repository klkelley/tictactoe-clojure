(ns tictactoe.ai-test
  (:require [clojure.test :refer [deftest is testing]]
            [tictactoe.ai :refer [get-computer-move]]))

(deftest impossible-computer
  (testing "Computer chooses middle cell when available"
    (is (= 4 (get-computer-move ["X" 1 2 3 4 5 6 7 8] "O" "X"))))

  (testing "Computer chooses winning cell 7 when human chooses cells 0, 2, 6"
    (is (= 7 (get-computer-move ["X" "O" "X" 3 "O" 5 "X" 7 8] "O" "X"))))

  (testing "Computer chooses winning cell 5 when human chooses cells 0, 2, 6, 7"
    (is (= 5 (get-computer-move ["X" "O" "X" "O" "O" 5 "X" "X" 8] "O" "X"))))

  (testing "Computer blocks when human has two in a row"
    (is (= 5 (get-computer-move ["O" 1 2 "X" "X" 5 "O" 7 8] "O" "X")))))