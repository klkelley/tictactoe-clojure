(ns tictactoe.game-runner-test
  (:require [clojure.test :refer [deftest is testing]]
            [tictactoe.game-runner :refer [start *sleep-time*]]
            [clojure.string :as string]))

(defn make-input
  [coll]
  (apply str (interleave coll (repeat "\n"))))

(defn lines 
  [& args]
  (apply str (interpose "\n" args)))

(defn timeout 
  [timeout-ms callback]
  (let [future-thread (future (callback))
        timeout-val (deref future-thread timeout-ms ::timed-out)]
  (when (= timeout-val ::timed-out)
    (future-cancel future-thread)
    (throw (Exception. "Timed out!")))
      timeout-val))

(deftest test-welcoming-message
  (testing "Game contains a welcome message"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "a" "B" "0" "5" "1" "4" "2")) 
          (binding [*sleep-time* 0]
            (start)))) "Welcome to Tic Tac Toe!\n" )))))

(deftest test-game-menu-summary
  (testing "A summary is displayed after user chooses game options"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "C" "d" "0" "5" "1" "4" "2")) 
          (binding [*sleep-time* 0]
            (start)))) "You've chosen: \ngame => Human vs. Human\nboard => 3x3\nplayer1 => C\nplayer2 => D\n" )))))

(deftest test-top-row-winner
  (testing "Player one wins the game on a 3x3 board via top row"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "E" "f" "0" "5" "1" "4" "2")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player E wins!\n" )))))

(deftest test-middle-row-winner
  (testing "Player one wins the game on a 3x3 board via middle row"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "G" "h" "3" "2" "4" "7" "5")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player G wins!\n" )))))

(deftest test-last-row-winner
  (testing "Player one wins the game on a 3x3 board via last row"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "i" "J" "6" "2" "7" "1" "8")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player I wins!\n" )))))

(deftest test-first-column-winner
  (testing "Player one wins the game on a 3x3 board via first column"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "k" "l" "0" "2" "3" "1" "6")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player K wins!\n" )))))

(deftest test-middle-column-winner
  (testing "Player one wins the game on a 3x3 board via first column"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "m" "n" "1" "0" "4" "2" "7")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player M wins!\n" )))))

(deftest test-last-column-winner
  (testing "Player one wins the game on a 3x3 board via first column"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "O" "P" "2" "0" "5" "4" "8")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player O wins!\n" )))))

(deftest test-game-human-v-human-player2-wins
  (testing "Player two wins the game on a 3x3 board"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "q" "r" "1" "4" "2" "8" "7" "0")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player R wins!\n" )))))

(deftest test-4x4-game-human-v-human-player1-wins
  (testing "Player one wins the game on a 4x4 board"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "2" "s" "t" "0" "4" "1" "8" "2" "13" "3")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player S wins!\n" )))))

(deftest invalid-marker-choice
  (testing "User will be prompted for different marker if choice is invalid"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "u" "5" "v" "0" "4" "1" "3" "2")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Invalid. Please pick another marker.\n" )))))

(deftest invalid-menu-choice
  (testing "User must choose an available menu option"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("3" "1" "1" "w" "x" "0" "4" "1" "3" "2"))
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Invalid. Please pick an available option.\n" )))))

(deftest play-tie-game
  (testing "Game can be played to a tie"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "x" "r" "0" "4" "1" "2" "6" "3" "5" "7" "8")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Its a tie!\n" )))))

(deftest win-game-diagonally-left
  (testing "Game can be won via left diagonal"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O" "0" "2" "4" "1" "8")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player X wins!\n" )))))

(deftest win-game-diagonally-right
  (testing "Game can be won via right diagonal"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O" "2" "3" "4" "7" "6")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Player X wins!\n" )))))

(deftest test-cell-choice-validation
  (testing "User will continue to be prompted for valid input and available spaces"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "x" "p" "0" "0" "f" "4" "1" "2" "6" "3" "5" "7" "8")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Its a tie!\n" )))))

(deftest test-user-is-prompted-to-choose-space
  (testing "User is asked to enter a number for an available cell"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O" "2" "3" "4" "7" "6")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Please pick an available cell: \n" )))))

(deftest test-invalid-space-choice
  (testing "An exception is caught if user enters invalid cell choice"
    (is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O" "2" "3" "4" "7" "f" "6")) 
          (binding [*sleep-time* 0]
            (timeout 1000 #(start))))) "Invalid choice!\n" )))))

(deftest test-all-available-markers 
  (testing "Available markers for game play are a-z"
    (doseq [[x y] [["a" "b"] ["c" "d"] ["c" "d"] ["e" "f"] ["g" "h"] ["i" "j"] ["k" "l"] ["m" "n"]
                  ["o" "p"] ["q" "r"] ["s" "t"] ["u" "v"] ["w" "x"] ["y" "z"]]]
      (is (= true (string/includes?
        (with-out-str ""
          (with-in-str (make-input ["1" "1" x y "2" "3" "4" "7" "6"]) 
            (binding [*sleep-time* 0]
              (timeout 2000 #(start))))) (str "Player " (string/upper-case x) " wins!\n" )))))))

(deftest impossible-computer-ties-game 
  (testing "Computer blocks any attempts at opponent winning resulting in tie game"
    (doseq [[spot1 spot2 spot3 spot4 spot5] [["0" "2" "7" "6" "5"] ["0" "3" "2" "7" "5"] 
                                            ["8" "5" "6" "1" "0"] ["8" "5" "6" "1" "0"] 
                                            [ "4" "6" "5" "1" "0"] ["6" "3" "8" "1" "2"]]]
      (is (= true (string/includes?
        (with-out-str ""
          (with-in-str (make-input ["2" "1" "x" "o" spot1 spot2 spot3 spot4 spot5]) 
            (binding [*sleep-time* 0]
              (timeout 4000 #(start))))) "Its a tie!\n" ))))))

(deftest test-computer-always-wins 
  (testing "Computer always wins when opponent doesn't make a blocking move"
    (doseq [[spot1 spot2 spot3] [["2" "8" "1"] ["0" "1" "3"] 
                                ["5" "2" "4"] ["6" "1" "2"]]]
      (is (= true (string/includes?
        (with-out-str ""
          (with-in-str (make-input ["2" "1" "x" "o" spot1 spot2 spot3]) 
            (binding [*sleep-time* 0]
              (timeout 7000 #(start))))) "Player O wins!\n" ))))))
