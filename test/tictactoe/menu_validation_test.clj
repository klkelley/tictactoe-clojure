(ns tictactoe.menu-validation-test
	(:require [clojure.test :refer :all]
			 			[tictactoe.menu-validation :refer :all]))

(def game-types
  {:1 "Human vs. Human" :2 "Human vs. Easy Computer"})

(deftest test-valid-game-choice
  (testing "Test that available option was chosen"
    (is (= true (valid-choice? game-types :1)))))

(deftest test-invalid-game-choice
  (testing "Returns false when invalid input is received"
    (is (= false (valid-choice? game-types :4)))))

(deftest test-no-errors
  (testing "No error message is returned when input is valid"
    (let [error (errors game-types :1)]
      (is (= nil (:error error))))))

(deftest test-errors 
  (testing "Error messages is returned when input is invalid"
    (let [error (errors game-types :4)]
      (is (= "Invalid. Please pick an available option." (:error error))))))

(deftest loops-when-invalid-choice
  (testing "Loops until valid input is received"
    (is (= "Invalid. Please pick an available option.\n"
      (with-out-str "Invalid. Please pick an available option."
        (with-in-str "1" (input-validation-loop :3 game-types)))))))

(deftest input-validation-doesnt-loop-when-valid
  (testing "It does not loop when valid input is given"
    (is (= :1 (with-in-str "1" (input-validation-loop :1 game-types))))))
