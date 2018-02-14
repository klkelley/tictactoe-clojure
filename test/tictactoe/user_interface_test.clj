(ns tictactoe.user-interface-test
  (:require [clojure.test :refer :all]
           [tictactoe.user-interface :refer :all])) 

(deftest print-message-test
   (is (=  "test\n" (with-out-str  (print-message "test")))))

(deftest get-input-test
  (is (= "input received" (with-in-str "input received" (get-input)))))

(deftest test-loop-and-print
	(is (= "game => Human vs Human\n" (with-out-str (loop-and-print {:game "Human vs Human"})))))

(deftest prints-and-loops
	(testing "It loops and prints out key value pairs")
	(with-out-str "test"
		(is (= nil (loop-and-print {:game "Human V Human" :board "3x3"})))))
