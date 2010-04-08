(ns playground.factorial)

(defn factorial [n]
  (reduce * (range 1 (inc n))))
