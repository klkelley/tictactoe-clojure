(ns tictactoe.game-runner-test
  (:require [clojure.test :as test]
            [tictactoe.game-runner :as game-runner]
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

(test/deftest test-welcoming-message
  (test/testing "Game contains a welcome message"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "a" "B" "0" "5" "1" "4" "2")) 
          (binding [game-runner/*sleep-time* 0]
            (game-runner/start)))) "Welcome to Tic Tac Toe!\n" )))))

(test/deftest test-game-menu-summary
  (test/testing "A summary is displayed after user chooses game options"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "C" "d" "0" "5" "1" "4" "2")) 
          (binding [game-runner/*sleep-time* 0]
            (game-runner/start)))) "You've chosen: \ngame => Human vs. Human\nboard => 3x3\nplayer1 => C\nplayer2 => D\n" )))))

(test/deftest test-top-row-winner
  (test/testing "Player one wins the game on a 3x3 board via top row"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "E" "f" "0" "5" "1" "4" "2")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player E wins!\n" )))))

(test/deftest test-middle-row-winner
  (test/testing "Player one wins the game on a 3x3 board via middle row"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "G" "h" "3" "2" "4" "7" "5")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player G wins!\n" )))))

(test/deftest test-last-row-winner
  (test/testing "Player one wins the game on a 3x3 board via last row"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "i" "J" "6" "2" "7" "1" "8")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player I wins!\n" )))))

(test/deftest test-first-column-winner
  (test/testing "Player one wins the game on a 3x3 board via first column"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "k" "l" "0" "2" "3" "1" "6")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player K wins!\n" )))))

(test/deftest test-middle-column-winner
  (test/testing "Player one wins the game on a 3x3 board via first column"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "m" "n" "1" "0" "4" "2" "7")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player M wins!\n" )))))

(test/deftest test-last-column-winner
  (test/testing "Player one wins the game on a 3x3 board via first column"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "O" "P" "2" "0" "5" "4" "8")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player O wins!\n" )))))

(test/deftest test-game-human-v-human-player2-wins
  (test/testing "Player two wins the game on a 3x3 board"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "q" "r" "1" "4" "2" "8" "7" "0")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player R wins!\n" )))))

(test/deftest test-4x4-game-human-v-human-player1-wins
  (test/testing "Player one wins the game on a 4x4 board"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "2" "s" "t" "0" "4" "1" "8" "2" "13" "3")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player S wins!\n" )))))

(test/deftest invalid-marker-choice
  (test/testing "User will be prompted for different marker if choice is invalid"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "u" "5" "v" "0" "4" "1" "3" "2")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Invalid. Please pick another marker.\n" )))))

(test/deftest invalid-menu-choice
  (test/testing "User must choose an available menu option"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("6" "1" "1" "w" "x" "0" "4" "1" "3" "2"))
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Invalid. Please pick an available option.\n" )))))

(test/deftest play-tie-game
  (test/testing "Game can be played to a tie"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "x" "r" "0" "4" "1" "2" "6" "3" "5" "7" "8")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Its a tie!\n" )))))

(test/deftest win-game-diagonally-left
  (test/testing "Game can be won via left diagonal"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O" "0" "2" "4" "1" "8")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player X wins!\n" )))))

(test/deftest win-game-diagonally-right
  (test/testing "Game can be won via right diagonal"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O" "2" "3" "4" "7" "6")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Player X wins!\n" )))))

(test/deftest test-cell-choice-validation
  (test/testing "User will continue to be prompted for valid input and available spaces"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "x" "p" "0" "0" "f" "4" "1" "2" "6" "3" "5" "7" "8")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Its a tie!\n" )))))

(test/deftest test-user-is-prompted-to-choose-space
  (test/testing "User is asked to enter a number for an available cell"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O" "2" "3" "4" "7" "6")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Please pick an available cell: \n" )))))

(test/deftest test-invalid-space-choice
  (test/testing "An exception is caught if user enters invalid cell choice"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("1" "1" "X" "O" "2" "3" "4" "7" "f" "6")) 
          (binding [game-runner/*sleep-time* 0]
            (timeout 1000 #(game-runner/start))))) "Invalid choice!\n" )))))

(test/deftest test-all-available-markers 
  (test/testing "Available markers for game play are a-z"
    (doseq [[x y] [["a" "b"] ["c" "d"] ["c" "d"] ["e" "f"] ["g" "h"] ["i" "j"] ["k" "l"] ["m" "n"]
                  ["o" "p"] ["q" "r"] ["s" "t"] ["u" "v"] ["w" "x"] ["y" "z"]]]
      (test/is (= true (string/includes?
        (with-out-str ""
          (with-in-str (make-input ["1" "1" x y "2" "3" "4" "7" "6"]) 
            (binding [game-runner/*sleep-time* 0]
              (timeout 2000 #(game-runner/start))))) (str "Player " (string/upper-case x) " wins!\n" )))))))

(test/deftest impossible-computer-ties-game 
  (test/testing "Computer blocks any attempts at opponent winning resulting in tie game"
    (doseq [[spot1 spot2 spot3 spot4 spot5] [["0" "2" "7" "6" "5"] ["0" "3" "2" "7" "5"] 
                                            ["8" "5" "6" "1" "0"] ["8" "5" "6" "1" "0"] 
                                            [ "4" "6" "5" "1" "0"] ["6" "3" "8" "1" "2"]]]
      (test/is (= true (string/includes?
        (with-out-str ""
          (with-in-str (make-input ["2" "1" "x" "o" spot1 spot2 spot3 spot4 spot5]) 
            (binding [game-runner/*sleep-time* 0]
              (timeout 2000 #(game-runner/start))))) "Its a tie!\n" ))))))

(test/deftest test-computer-always-wins 
  (test/testing "Computer always wins when opponent doesn't make a blocking move"
    (doseq [[spot1 spot2 spot3] [["2" "8" "1"] ["0" "1" "3"] 
                                ["5" "2" "4"] ["6" "1" "2"]]]
      (test/is (= true (string/includes?
        (with-out-str ""
          (with-in-str (make-input ["2" "1" "x" "o" spot1 spot2 spot3]) 
            (binding [game-runner/*sleep-time* 0]
              (timeout 2000 #(game-runner/start))))) "Player O wins!\n" ))))))

(test/deftest test-stupid-computer
  (test/testing "Stupid computer picks moves at random"
    (test/is (= true (string/includes?
      (with-out-str ""
        (with-in-str (make-input '("3" "1" "x" "o" "4" "2" "6" "7" "8" "3" "1"))
          (binding [game-runner/*sleep-time* 0]
            (timeout 2000 #(game-runner/start))))) "Player X wins!\n" )))))
