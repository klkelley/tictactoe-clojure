(defproject tictactoe "game"
  :description "TicTacToe in Clojure"
  :url "https://github.com/klkelley/tictactoe-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :main ^:skip-aot tictactoe.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
