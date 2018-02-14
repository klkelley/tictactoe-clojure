(ns tictactoe.marker-validation-test
	(:require [clojure.test :refer :all]
            [tictactoe.marker-validation :refer :all]))

(deftest valid-marker
  (testing "Marker is a valid choice" 
    (is (= "X" (valid-marker-choice? "X")))))

(deftest both-valid-markers
  (testing "Both markers are valid choices"
    (is (= true (valid-marker-choice? "x" "O")))))

(deftest invalid-already-chosen
  (testing "Marker was already chosen by player one" 
    (is (= false (valid-marker-choice? "X" "X")))))

(deftest invalid-marker-for-player2
  (testing "Player two chose an invalid marker"
    (is (= nil (valid-marker-choice? "4" "x")))))

(deftest invalid-marker-not-letter
  (testing "Player one chose an invalid marker"
    (is (= nil (with-in-str "4" (valid-marker-choice? "4"))))))

(deftest test-no-errors
  (testing "No errors due to valid marker choice"
    (let [error (errors "X")]
      (is (= nil (:error error))))))

(deftest test-errors
  (testing "Error message returned due to invalid marker choice"  
    (let [error (errors "X" "X")]
      (is (= "Invalid. Please pick another marker." (:error error))))))

(deftest test-no-errors-both-markers
  (testing "No errors returned after both markers chosen"
    (let [error (errors "X" "O")]
      (is (= nil (:error error))))))

(deftest loops-when-invalid-marker
  (testing "When invalid input it loops until valid input is received"
    (is (= "Invalid. Please pick another marker.\n"
      (with-out-str "Invalid. Please pick another marker."
        (with-in-str "X" (marker-validation-loop "4")))))))

(deftest loops-when-marker-taken
  (testing "When user tries to use marker already taken it loops until valid input is received"
    (is (= "Invalid. Please pick another marker.\n"
      (with-out-str "Invalid. Please pick another marker."
        (with-in-str "O" (marker-validation-loop "X" "X")))))))

(deftest validation-does-not-loop-when-valid
  (testing "It does not loop when there is valid input received"
    (is (= "X" (with-in-str "X" (marker-validation-loop "X"))))))
