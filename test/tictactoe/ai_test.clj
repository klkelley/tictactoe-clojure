(ns tictactoe.ai-test
  (:require [clojure.test :as test]
            [tictactoe.ai :as ai]))

(test/deftest impossible-computer
  (test/testing "Computer chooses middle cell when available"
    (test/is (= 4 (ai/get-computer-move ["X" 1 2 3 4 5 6 7 8] "O" "X"))))

  (test/testing "Computer chooses winning cell 7 when human chooses cells 0, 2, 6"
    (test/is (= 7 (ai/get-computer-move ["X" "O" "X" 3 "O" 5 "X" 7 8] "O" "X"))))

  (test/testing "Computer chooses winning cell 5 when human chooses cells 0, 2, 6, 7"
    (test/is (= 5 (ai/get-computer-move ["X" "O" "X" "O" "O" 5 "X" "X" 8] "O" "X"))))

  (test/testing "Computer blocks when human has two in a row"
    (test/is (= 5 (ai/get-computer-move ["O" 1 2 "X" "X" 5 "O" 7 8] "O" "X")))))