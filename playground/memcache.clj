(ns playground.memcache
	[:use clojure.memcached])

	(def memcache-servers ["192.168.100.122:11211", "192.168.100.123:11211"])

	(defn increment [sockets key]
		(let [cnt (.toString (inc (Integer/parseInt (get-val sockets key))))]
				(set-val sockets key cnt)))

	(defn increment-and-get [key]
		(let [sockets (setup-sockets memcache-servers)]
			(increment sockets key)
  		(let [peek (get-val sockets key)]
				(close-sockets sockets)
				(str peek))))

