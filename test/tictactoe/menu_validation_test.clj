(ns tictactoe.menu-validation-test
	(require [clojure.test :refer :all]
			 [tictactoe.menu-validation :refer :all]))

(def game-types
  {:1 "1. Human vs. Human" :2 "2. Human vs. Easy Computer"})

(deftest test-valid-game-choice
	(is (= true (valid-choice game-types :1))))

(deftest test-invalid-game-choice
	(is (= false (valid-choice game-types :4))))

(deftest test-no-errors
	(let [error (errors game-types :1)]
	(is (= nil (:error error)))))

(deftest test-errors 
	(let [error (errors game-types :4)]
	(is (= "Invalid. Please pick an available option." (:error error)))))

(deftest loops-when-invalid-choice
	(is (= "Invalid. Please pick an available option.\n"
		 (with-out-str "Invalid. Please pick an available option."
			(with-in-str "1" (input-validation-loop :3 game-types))))))

(deftest input-validation-doesnt-loop-when-valid
		(is (= :1 (with-in-str "1" (input-validation-loop :1 game-types)))))
