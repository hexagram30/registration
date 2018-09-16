(ns hxgm30.registration.components.core
  (:require
    [com.stuartsierra.component :as component]
    [hxgm30.registration.components.config :as config]
    [hxgm30.registration.components.logging :as logging]
    [hxgm30.registration.components.registrar :as registrar]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Common Configuration Components   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn cfg
  [cfg-data]
  {:config (config/create-component cfg-data)})

(def log
  {:logging (component/using
             (logging/create-component)
             [:config])})

(def reg
  {:registrar (component/using
               (registrar/create-component)
               [:config :logging])})

(defn basic
  [cfg-data]
  (merge (cfg cfg-data)
         log))

(defn main
  [cfg-data]
  (merge (basic cfg-data)
         reg))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Initializations   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn initialize-bare-bones
  []
  (-> (config/build-config)
      basic
      component/map->SystemMap))

(defn initialize
  []
  (-> (config/build-config)
      main
      component/map->SystemMap))

(def init-lookup
  {:basic #'initialize-bare-bones
   :main #'initialize})

(defn init
  ([]
    (init :main))
  ([mode]
    ((mode init-lookup))))
