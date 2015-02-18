(ns clojure-sushi-gamebot.core
  (:require [clojure-sushi-gamebot.robot :as r])
  (:import [javax.imageio.ImageIO])
  (:gen-class))


(def screen {:width 640 :height 480})
;;(def screen-offset {:x 158 :y 315})
(def screen-offset {:x 10 :y 86})

(def bot (r/create-robot))

;;;
;;; IItems that are interactable while
;;; the game is running
;;;

(def start-seq [{:x 312 :y 206}
                {:x 320 :y 390}
                {:x 580 :y 455}
                {:x 320 :y 390}])

;; The plates flash on screen when a seat is
;; vacated. They must be clicked to clear
(def plates [{:x 87 :y 210}
             {:x 191 :y 210}
             {:x 290 :y 210}
             {:x 392 :y 210}
             {:x 494 :y 210}
             {:x 595 :y 210}])

;;
;; Game starts out with ingredients in store
;;
(def stores {:shrimp 5
             :rice 10
             :nori 10
             :roe 10
             :salmon 5
             :unagi 5})

;;
;; Recipies for the sushi
;;
(def recipies {:caliroll [:rice :nori :roe]
               :onigiri [:rice :rice :nori]
               :gunkan [:rice :nori :roe :roe]})

;;
;; Co-ordinate map of all the clickables
;;
(def co-ords {:food {:shrimp  {:x 41 :y 335}
                     :rice    {:x 98 :y 337}
                     :nori    {:x 35 :y 389}
                     :roe     {:x 96 :y 400}
                     :salmon  {:x 36 :y 448}
                     :unagi   {:x 95 :y 445}}
              :phone {:x 570 :y 390}
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

;;
;; Robot actions are side effect
;; so need to be run from one place
;;
(defn remote-control
  "Takes a vector of positions and clicks on them"
  [click-sequence]
  (map (fn [position]
         (r/with-robot bot
           (r/mouse-move position)
           (r/click r/left-button)
           (r/pause 500))) click-sequence))

;;
;; When viewing a co-ordinate, apply the offset
;;
(defn offset
  "Takes a cord map {:x 5 :y 10} and adds it to screen
  offset, then returns the result"
  [cord-map]
  (merge-with + screen-offset cord-map))

;;
;; Looks in a nested map for co-ords
;;
(defn get-coord
  ([map key]
   (get map key))
   ([map key & keys]
    (get (get map key) (first keys))))

;;
;; Wrap all those to give an offset of the co-ords
;; (screen-pos :toppings :shrimp)
;;
(defn screen-pos
  [& args]
  (offset (apply get-coord co-ords args)))

;;
;; Commands
;;
(defn clear-tables
  []
  (remote-control (into [] (map #(offset %) plates))))

(defn make-food
  "Takes a recipe and makes up"
  [recipe]
  ;;Make a vector of the positions for the recipe
  (let [co-ords (into [] (map #(offset (get (:food co-ords) %))
                               (recipe recipies)))]
    ;;add the mat roll, and send to robot
    (remote-control (conj co-ords (screen-pos :mat)))))


(defn build-order
  "Takes a possibly embedded vector to build a vector of pointmaps"
  [keywordvect]
  (into [] (map (fn [option]
                  (if (vector? option)
                    (offset (get (get co-ords (first option)) (last option)))
                    (offset (get co-ords option))))
                keywordvect)))


(defn order
  [food]
  (if (= food :rice)
    (remote-control
     (build-order [:phone
                   [:menu :rice]
                   [:buy :rice]
                   [:delivery :norm]]))
    (remote-control
     (build-order [:phone
                   [:menu :toppings]
                   [:toppings food]
                   [:delivery :norm]]))))

(defn check-store
  "Checks if stores are running low, orders food if they are"
  [])

(defn start-game
  []
  (remote-control (into [] (map #(offset %) start-seq))))

(defn screen-capture
  []
  (let [image (r/screen-capture bot
                                (:x screen-offset)
                                (:y screen-offset)
                                640
                                480)
        file (clojure.java.io/file 'test.png')]
    (javax.imageio.ImageIO/write image 'PNG' file)))

(defn get-mouse
  []
  (let [point (.. java.awt.MouseInfo getPointerInfo getLocation)]
    [(- (.getX point) (:x screen-offset))(- (.getY point) (:y screen-offset))]))

(defn mouse-find
 []
 (while true
   (Thread/sleep 1000)
   (println (get-mouse))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
