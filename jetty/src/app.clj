(ns app
  (:import
   (org.eclipse.jetty.server Server)
   (org.eclipse.jetty.server Handler)
   (org.eclipse.jetty.server ServerConnector)
   (org.eclipse.jetty.server Handler$Abstract)
   (org.eclipse.jetty.server Handler$Abstract$NonBlocking)
   (org.eclipse.jetty.io Content$Sink)
   (org.eclipse.jetty.http HttpHeader)))

(defn make-default-handler []
  (proxy [Handler$Abstract$NonBlocking] []
    (handle
     [request response callback]
     (doto (.getHeaders response)
       (.put HttpHeader/SERVER "text/html;charset=utf-8")
       (.put "Server" "Clojure/Jetty 12.0"))
     (.setStatus response 200)
     (Content$Sink/write response true "Hello World" callback)
     true)))

(defn make-server [port]
  (let [server (Server.)
        connector (ServerConnector. server)]
      (.setPort connector port)
      (.addConnector server connector)
      (.setHandler
       server
       (make-default-handler))
      server))

(defn main [opts]
  (let [server (make-server 8080)]
    (.start server)))