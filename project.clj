(defproject clojure-rest-example "1.0.0-SNAPSHOT"
  :description "Clojure-rest-example service"

  :dependencies [[ch.qos.logback/logback-classic "1.1.3"]
                 [cheshire "4.1.0"]
                 [clj-http "1.1.2"]
                 [clj-time "0.9.0"]
                 [compojure "1.3.4"]
                 [environ "1.0.0"]
                 [com.codahale.metrics/metrics-logback "3.0.2"]
                 [mixradio/graphite-filter "1.0.0"]
                 [mixradio/instrumented-ring-jetty-adapter "1.0.4"]
                 [mixradio/radix "1.0.10"]
                 [net.logstash.logback/logstash-logback-encoder "4.3"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ring/ring-json "0.3.1"]
                 [ring-middleware-format "0.5.0"]]

  :exclusions [commons-logging
               log4j
               org.clojure/clojure]

  :plugins [[lein-environ "1.0.0"]
            [lein-release "1.0.5"]
            [lein-ring "0.8.12"]]
  
  :env {:auto-reload "true"
        :environment-name "poke"
        :graphite-enabled "false"
        :graphite-host ""
        :graphite-port "2003"
        :graphite-post-interval-seconds "60"
        :logging-consolethreshold "info"
        :logging-filethreshold "info"
        :logging-level "info"
        :logging-path "/tmp"
        :logging-stashthreshold "off"
        :production "false"
        :requestlog-enabled "false"
        :requestlog-retainhours "24"
        :restdriver-port "8081"
        :service-name "clojure-rest-example"
        :service-port "8080"
        :service-url "http://localhost:%s"
        :shutdown-timeout-millis "5000"
        :start-timeout-seconds "120"
        :threads "254"}
  
  :lein-release {:deploy-via :shell
                 :shell ["lein" "do" "clean," "uberjar," "pom," "rpm"]}

  :ring {:handler clojure-rest-example.web/app
         :main clojure-rest-example.setup
         :port ~(Integer/valueOf (get (System/getenv) "SERVICE_PORT" "8080"))
         :init clojure-rest-example.setup/setup
         :browser-uri "/healthcheck"
         :nrepl {:start? true}}

  :uberjar-name "clojure-rest-example.jar"
  
  :profiles {:dev {:dependencies [[com.github.rest-driver/rest-client-driver "1.1.42"
                                   :exclusions [org.slf4j/slf4j-nop
                                                javax.servlet/servlet-api
                                                org.eclipse.jetty.orbit/javax.servlet]]
                                  [junit "4.12"]
                                  [midje "1.6.3"]
                                  [rest-cljer "0.1.20"]]
                   
                   :plugins [[lein-kibit "0.0.8"]
                             [lein-midje "3.1.3"]
                             [lein-rpm "0.0.5"]]}}
  
  :rpm {:name "clojure-rest-example"
        :summary "RPM for Clojure-rest-example service"
        :copyright "MixRadio 2017"
        :preinstall {:scriptFile "scripts/rpm/preinstall.sh"}
        :postinstall {:scriptFile "scripts/rpm/postinstall.sh"}
        :preremove {:scriptFile "scripts/rpm/preremove.sh"}
        :postremove {:scriptFile "scripts/rpm/postremove.sh"}
        :requires ["jdk >= 2000:1.7.0_55-fcs"]
        :mappings [{:directory "/usr/local/clojure-rest-example"
                    :filemode "444"
                    :username "clojure-rest-example"
                    :groupname "clojure-rest-example"
                    :sources {:source [{:location "target/clojure-rest-example.jar"}]}}
                   {:directory "/usr/local/clojure-rest-example/bin"
                    :filemode "744"
                    :username "clojure-rest-example"
                    :groupname "clojure-rest-example"
                    :sources {:source [{:location "scripts/bin"}]}}
                   {:directory "/etc/rc.d/init.d"
                    :filemode "755"
                    :sources {:source [{:location "scripts/service/clojure-rest-example"
                                        :destination "clojure-rest-example"}]}}]}


  :aot [clojure-rest-example.setup]

  :main clojure-rest-example.setup)
