(ns clojure-sushi-gamebot.core
  (:require [clojure-sushi-gamebot.robot :as r])
  (:gen-class))


(def screen {:width 640 :height 480})
(def screen-offset {:x 9 :y 80})

(def bot (r/create-robot))

;; The plates flash on screen when a
;; seat is vacated
(def plates [{:x 87 :y 210}
             {:x 191 :y 210}
             {:x 290 :y 210}
             {:x 392 :y 210}
             {:x 494 :y 210}
             {:x 595 :y 210}])

(def stores {:shrimp 5
             :rice 10
             :nori 10
             :roe 10
             :salmon 5
             :unagi 5})

(def recipies {:caliroll [:rice :nori :roe]
               :onigiri [:rice :rice :nori]
               :gunkan [:rice :nori :roe :roe]})


(def co-ords {:food {:shrimp  {:x 41 :y 335}
                     :rice    {:x 98 :y 337}
                     :nori    {:x 35 :y 389}
                     :roe     {:x 96 :y 400}
                     :salmon  {:x 36 :y 448}
                     :unagi   {:x 95 :y 445}}
              :phone {:x 654 :y 385}
              :mat   {:x 133 :y 406}
              :menu {:toppings {:x 540 :y 271}
                     :rice     {:x 540 :y 292}
                     :sake     {:x 540 :y 314}}
              :exit {:x 585 :y 345}
              :buy {:rice {:x 540 :y 292}
                    :sake {:x 540 :y 288}}
              :toppings {:shrimp {:x 460 :y 227}
                         :nori   {:x 460 :y 291}
                         :roe    {:x 543 :y 291}
                         :salmon {:x 460 :y 345}
                         :unagi  {:x 543 :y 227}}
              :delivery {:norm {:x 484 :y 299}
                         :express {:x 575 :y 300}}})

(defn get-coord
  ([map key]
   (get map key))
  ([map key & keys])
  (recur (get map key) (first keys) (rest keys)))

(defn fold-mat
  []
  (r/mouse-move (get-coord co-ords :mat))
  (r/click r/left-button))

(defn clear-tables
  []
  (map (fn [plate]
         (r/with-robot bot
           (r/mouse-move table)
           (r/click r/left-button)
           (r/pause 500))) plates))

(select-food
 "Takes a food keyword eg :rice, and makes the appropriate clicks
 to select it"
 [food])

(defn make-food
  "Takes a recipe and makes up"
  [recipe]
  (map select-food (recipe recipies))
  (fold-mat))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
