(ns playground.server
  (:use [clojure.contrib.duck-streams :only (reader writer read-lines spit to-byte-array)]
        [clojure.contrib.str-utils :only (re-split str-join re-gsub)]
				[playground.factorial]
				[playground.memcache :only (increment-and-get)]
				[playground.mysql :only (get-table-count)])
  (:import (java.net InetAddress ServerSocket Socket SocketException URLDecoder)
    			 (java.io InputStreamReader OutputStream OutputStreamWriter PrintWriter)
           (clojure.lang LineNumberingPushbackReader)))


	(defn- run-in-thread [f]
	  (doto (Thread. f) (.start)))

	(defn- handle-request [s fun]
	  (let [ins (.getInputStream s) outs (.getOutputStream s)]
	    (run-in-thread #(do                                     		; create anon func and pass into run-in-thread
	                  (try
	                   (fun ins outs)                       				; the fun must accept the input and output params
	                   (catch SocketException e))
	                  (.close s)))))	
	
	(defn create-server [fun ss]
	    (run-in-thread #(when-not (.isClosed ss)
	                  (try
	                   (handle-request (.accept ss) fun)
	                   (catch SocketException e))
	                  (recur))))
