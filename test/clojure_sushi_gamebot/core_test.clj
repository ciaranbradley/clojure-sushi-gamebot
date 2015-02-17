(ns clojure-sushi-gamebot.core-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [clojure-sushi-gamebot.core :refer :all]))


(facts "get-coord"
       (fact "get-coord returns correct map with single keyword"
             (get-coord co-ords :phone) => {:x 654 :y 385})
       (fact "get-coord returns correct map with two keywords"
             (get-coord co-ords :food :rice) => {:x 98 :y 337}))

(facts "screen-pos"
       (fact "screen-pos returns correct position offset map with keyword"
             (screen-pos :phone) => (merge-with + screen-offset
                                                {:x 654 :y 385}))
       (fact "screen-pos returns correct positions offset map with two keywords"
             (screen-pos :food :rice) => (merge-with + screen-offset
                                                     {:x 98 :y 337})))



