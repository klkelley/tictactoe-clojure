(ns tictactoe.game-runner-test
  (:require [clojure.test :refer :all]
            [tictactoe.game-runner :refer :all]
            [tictactoe.game-menu :as game-menu]
            [tictactoe.game-messages :as messages]
            [tictactoe.user-interface :as ui]))

(defn make-input [coll]
  (apply str (interleave coll (repeat "\n"))))

(deftest test-start 
  (with-out-str "test" (with-in-str (make-input '("1" "2" "a" "b")) (start))))
    (is (= {:game :1 :board :2 :player1 "A" :player2 "B"}))