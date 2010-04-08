(ns playground.mysql
	[:use clojure.contrib.sql])
 
	(def db-host "localhost")
	(def db-port 3306)
	(def db-name "test")
	(def db-user "test")
	(def db-pass "test")
 
  (def db {:classname "com.mysql.jdbc.Driver"
           :subprotocol "mysql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           :user db-user
           :password db-pass})


  (defn print-column [table column]
		"warning: does not prevent sql injection!"
		(with-connection db 
		   (with-query-results rs [(str "select " column " as name from " table)] 
		     (dorun (map #(println (:name %)) rs)))))			
		
	(defn get-table-count [table]
		"warning: does not prevent sql injection!"
		(with-connection db 
		   (with-query-results rs [(str "select count(id) as cnt from " table)] 
		     (str (:cnt (first rs))))))			

