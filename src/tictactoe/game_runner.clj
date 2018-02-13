(ns tictactoe.game-runner
  (:require [tictactoe.game-menu :as game-menu]))

(def game-types
  {:1 "1. Human vs. Human" :2 "2. Human vs. Easy Computer"})

(def board-sizes
	{:1 "1. 3x3" :2 "2. 4x4"})


(defn start []
	(let [game-options (game-menu/menu game-types board-sizes)]
	(println game-options)))
 	
  ; / then start the game with stuff ))
