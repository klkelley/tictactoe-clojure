(ns tictactoe.user-interface-test
  (:require [clojure.test :refer :all]
           [tictactoe.user-interface :refer :all])) 

(deftest print-message-test
   (is (=  "test\n" (with-out-str  (print-message "test")))))