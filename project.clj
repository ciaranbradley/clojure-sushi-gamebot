(defproject clojure-sushi-gamebot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://www.ciaranbradley.com/sushibot"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [midje "1.6.3"]]
  :plugins [[lein-midje "3.1.3"]]
  :main ^:skip-aot clojure-sushi-gamebot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
