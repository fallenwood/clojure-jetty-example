(ns app
  (:import
    (org.eclipse.jetty.server Server)
    (org.eclipse.jetty.server.handler AbstractHandler)))

(defn make-default-handler []
  (proxy [AbstractHandler] []
    (handle [target base-request request response]
      (.setContentType response "text/html;charset=utf-8")
      (.setHandled base-request true)
      (.setStatus response 200)
      (.println (.getWriter response) "Hello World"))))

(defn make-server [port]
  (let [server (new Server 8080)]
    (.setHandler
      server
      (make-default-handler))
    server))

(defn main [opts]
  (let [server (make-server 8080)]
    (.start server)
    (.join server)))