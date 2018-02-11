(ns tictactoe.game-menu-test
  (:require [clojure.test :refer :all]
            [tictactoe.game-menu :refer :all]))
           
(deftest show-welcome-message
  (is (= "Welcome to TTT!\n" (with-out-str (welcome-message)))))
