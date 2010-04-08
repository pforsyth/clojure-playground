(ns playground.factserver
	 (:gen-class)
  (:use [clojure.contrib.duck-streams :only (reader writer read-lines spit to-byte-array)]
        [clojure.contrib.str-utils :only (re-split str-join re-gsub)]
				[playground.factorial]
				[playground.server]
				[playground.memcache :only (increment-and-get)]
				[playground.mysql :only (get-table-count)])
	 (:import (java.net ServerSocket)))


	(defn body [num]
		(str "The factorial of " num " is " (factorial num) "\n"))
			;(str "The factorial of " num " is " (factorial num) "\n"
				 ;"The memcache value is " (increment-and-get "paul-test") "\n"
				 ;"The count from message_log is " (get-table-count "message_log" )))
				
	(def headers "HTTP/1.0 200 OK\r\nContent-type: text/plain\r\n\r\n")

	(defn server-responder [in out]
	  (binding [*in* (reader in) *out* (writer out)] ; set stdin/stdout (thread local)
			(print headers)
			(print (body (+ 1000 (rand-int 1000))))
			(flush)))

	(defn start-server [port fun]
	    (create-server fun (ServerSocket. port)))

	(defn -main []
  	(start-server 8080 server-responder))


	;java -cp .:lib/commons-io-1.4.jar:lib/clojure.jar:lib/clojure-contrib.jar::lib/mysql-connector-java-5.1.12-bin.jar:classes playground.factserver
	;java -cp .:lib/commons-io-1.4.jar:lib/clojure.jar:lib/clojure-contrib.jar:lib/mysql-connector-java-5.1.12-bin.jar:classes playground.factserver
	;java -cp .:lib/commons-io-1.4.jar:lib/clojure.jar:lib/clojure-contrib.jar:classes playground.factserver
	;java -cp .:lib/clojure.jar:lib/clojure-contrib.jar:classes playground.factserver
