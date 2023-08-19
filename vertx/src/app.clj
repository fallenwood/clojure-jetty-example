(ns app
  (:import
   (io.vertx.core
    Vertx
    VertxOptions
    DeploymentOptions
    AbstractVerticle
    Handler)
   (io.vertx.ext.web
    Router)))

(defn main-vertice [port]
  (proxy [AbstractVerticle] []
    (start [startPromise]
      (println "Hello from Vert.x!")
      (println (format "You can open http://localhost:%d in a web browser" port))
      (let
       [router (Router/router (.getVertx this))
        server (.createHttpServer (.getVertx this))]
        (->
         router
         (.route "/")
         (.handler
          (reify Handler
            (handle [_ ctx]
              (let [response (.response ctx)]
                (.putHeader response "content-type" "text/plain")
                (.end response "Hello World!"))))))

        (->
         server
         (.requestHandler router)
         (.listen
          port
          (reify Handler
            (handle [_ http]
              (if
               (.succeeded http)
                (do
                  (println "Server is now listening!")
                  (.complete startPromise))
                (.fail startPromise (.cause http)))))))))))

(defn main [opts]
  (let [options (->
                 (VertxOptions.)
                 (.setWorkerPoolSize 40))
        vertice (main-vertice 8080)
        deployment-options (.setWorker (DeploymentOptions.) true)]
    (->
     (Vertx/vertx options)
     (.deployVerticle vertice deployment-options))))