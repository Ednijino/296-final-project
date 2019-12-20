(ns adventure.core)
  (require '[clojure.string :as str])
  (require '[clojure.core.match :refer [match]])

(def init-map
      {
       :lobby {:desc "You find yourself standing in the lobby of a special hotel and the only thing you still remember is that you won a free-night ticket from a resort. Apparently, you are the only guest here, there’s only one staff standing in front of an elevator. You walk towards the elevator for more information. The staff said nothing but opened the elevator for you and handed you an IDwatch, there is only one button in the elevator that says up"
               :title "In the lobby"
               :dir {:up :video-games-floor}
               :contents #{:IDwatch}}
       :video-games-floor {:desc "You noticed a big poster at the entrance of a room full of computers, it lists out different kinds of games and the prices for each of them. You suddenly realize that the number on your watch is the money that u will be able to use in this weird hotel. At the bottom of the poster, it says new customers can have a trial for 2 hours by just paying 10 points when it normally costs more than 100. It seems to be a great deal, you decided to pay for that. There i s also a sign shows that ‘east’ room is for PCs and ‘west’ room is for PS4s."
                          :title "by the gaming floor entrance"
                          :dir {
      		                      :down :lobby
                                :up :escape-floor
      		                        :east :pc-room
      		                          :west :ps4-room
                                }
                         :contents #{:game-ticket}
      }
      :escape-floor {
        :desc "You arrived the room for escape activities, there are different kinds of games with different difficulties that you can challenge, each will take up to 2 hours, and you will be able to have prizes by just participating. Head ‘south’ for joining the game or ‘up’ to the next floor or ‘down’ to the previous floor."
        :title "in the escape floor"
        :dir {
		          :up :pool-floor
              :down :video-games-floor
		          :south :escape-game-room
            }
        :contents #{}}
      :pool-floor {:desc "There is even a hot tub and an outdoor pool in this hotel, you can but tickets for entrance"
                    :title "by the swimming pool"
                    :dir { :up :movie-floor
	                         :down :escape-floor
	                        }
                    :contents #{}}
      :movie-floor {
              :desc "A movie theatre is essential for a holiday resort, but you have never imagined one like this, you can enjoy any movie you want while lying on a sofa bed. And the price is much cheaper than you imagined. The personal theatre is on your ‘north’ side"
              :title "in movie floor"
              :dir { :north :personal-theatre
              	     :up    :hotel-rooms-floor
              	      :down :pool-floor
              	}
              :contents #{}}
      :hotel-rooms-floor {
              :desc "You arrive at the floor that contains the guest rooms, there are various types of rooms provided for different sizes of guest groups. There are queen/king/full size beds in each room for friends and suites for families."
              :title "in the guest room"
              :dir  {
              	     :east :guest-room
                    :up :rooftop
              	     :down :movie-floor
              }
              :contents #{}}
      :pc-room { :desc "You entered a room full of PCs and played fortnite for 2 hours"
                :title "In pc game room"
                :dir { :west :video-games-floor
                        :up :escape-floor
                        :down :lobby
                      }
                :contents #{}}
      :ps4-room {:desc "You entered a room full of PS4s and played fortnite for 2 hours"
                  :title "in pc game room"
                  :dir { :east :video-games-floor
	                         :up :escape-floor
	                          :down :lobby
	                         }
                :contents #{:teddybear}}
      :escape-game-room {
                :desc-- "You successfully escaped and win the prizes!"
                :title "In the escape game room"
                :dir { }
                :contents #{:metal-prize}}
      :personal-theatre {:desc "You stayed and watched your favorite movie! You can go ‘south’ to select another movie or go up to enjoy the hotel rooms or go ‘down’ to the pool area"
                          :title "In the private theatre"
                          :dir {:south :movie-floor
          	                     :up :hotel-rooms-floor
          	                      :down :pool-floor
          	                     }
                          :contents #{}}
      :guest-room {
                    :desc "You happily chose a comfortable room with a king-size bed and rest for a few hours. Before you leave, u saw there is a teddy bear at the corner with a card says it is a gift from the hotel that you can take away. Now you can go ‘up’ to the rooftop or go ‘down’ to the movie floor or go ‘west’ to select another room"
                    :title "In the guest room"
                    :dir {:west :guest-rooms-floor
                    	    :up :rooftop
                    	   :down :movie-floor
                    	}
                    :contents #{}}
      :rooftop {
             :desc "You arrive at the rooftop of this hotel, there is a door that shows ‘exit’, and another door that says lobby"
             :title "at the rooftop"
             :dir {:down :hotel-rooms-floor
             	   :door :lobby
             	}
             :contents #{}}
       })

  (def init-items
        {
        :raw-egg {:desc "This is a raw egg.  You probably want to cook it before eating it."
                  :name "a raw egg" }
         :IDwatch {
                   :desc "You put on the watch and your name and a number shows up on the screen."
                   :name " an IDwatch"
                   }
          :game-ticket {
                   :desc "You bought a 2-hour video game ticket that you can use to enter the pc-room/ps4-room"
                   	:name " a game ticket"
                   }
          :metal-prize {
                   	:desc "A prize for escape game winner"
                   	:name "a metal-prize"
                   	}
          :teddybear {
                   	:desc "A teddy bear from the hotel as a gift"
                   	:name " a teddy bear"
                  }})


  (def init-adventurer
                     { :location :lobby
                       :luggage #{}
                       :tick 0
                       :hp 10
                       :seen #{}
})

(defn status [state]
  (let [location (get-in state [:adventurer :location])
        the-map (:map state)]
    (when-not ((get-in state [:adventurer :seen]) location)
      (print (-> the-map location :desc)))
    (print (str "\nYou are " (-> the-map location :title) ". "))
    (update-in state [:adventurer :seen] #(conj % location))))

(defn inventory [state]
        (let [inventory (get-in state [:adventurer :luggage] )]
        (if (empty? inventory)
        (do (println "You have nothing in inventory now") state)
        (do (println (str "You have " inventory ". ")) state)
        )

  )
  )


  (defn go [state dir]
    (let [location (get-in state [:adventurer :location])
          dest ((get-in state [:map location :dir]) dir)]
      (if (nil? dest)
        (do (println "You can't go that way.")
            state)
            (assoc-in (update-in state [:adventurer :hp] #(dec %)) [:adventurer :location] dest))))

  (defn quit [state]
(System/exit 0))


(defn takee [state item]
    (print (str "\nYou've picked" item ". "))
    (update-in state [:adventurer :luggage] #(conj % item ))
    )

(defn dropp [state item]
    (print (str "\nYou've droped" item ". "))
    (update-in state [:adventurer :luggage] #(disj % item ))
  )

(defn look [state item]
        (if (empty? item)
        (do (println "There is nothing to pick now") state)
        (do (println (str "You can pick " item". ")) state)
        ))

(defn respond [state command]
  (match command
      [:north] (go  state :north )
      [:n] (go state :north )
      [:gonorth] (go state :north )
      [:w] (go  state :west )
      [:west] (go  state :west )
      [:gowest] (go  state :west )
      [:e] (go  state :east )
      [:east] (go  state :east )
      [:goeast] (go  state :east )
      [:south] (go  state :south )
      [:s] (go  state :south )
      [:gosouth] (go  state :south )

      [:examine] (status state)
      [:i] (inventory state)
      [:inventory] (inventory state)
      [:up] (go state :up)
      [:down] (go state :down)
      [:quit] (quit state)
      [:look] (let [location (get-in state [:adventurer :location])
            the-map (:map state)
            item (-> the-map location :contents)]
            (look state item))
      [:take] (let [location (get-in state [:adventurer :location])
            the-map (:map state)
            item (-> the-map location :contents)]
            (takee state item))
      [:drop] (let [location (get-in state [:adventurer :location])
            the-map (:map state)
            item (-> the-map location :contents)]
            (dropp state item))

      _ (do (println "I don't understand.")
              state)
            ))


  (defn canonicalize
  "Given an input string, strip out whitespaces, lowercase all words, and convert to a vector of keywords."
  [input]
  (mapv keyword (str/split input #"[.,?! ]+")))
(def intro "You are in fornt of a big building and have 10 tickets, going into any room will consume a ticket. You will be kicked off when out of tikets. Arrive the rooftop with teddybear considering win! ")
(defn -main
  "Initialize the adventure"
  [& args]
  (println intro)
  (println "**************************************")
  (println "look: to look what item you can pick in the current room")
  (println "take: pick the item in your luggage")
  (println "drop: drop the item in your luggage")
  (println "examine: see what room you are in")
  (println "*************************************")
  (loop [local-state {:map init-map :adventurer init-adventurer :items init-items}]
    (let [pl (status local-state)
          _  (println "What do you want to do?")
          command (read-line)]
      (if (= (get-in pl [:adventurer :hp]) 0)
      (quit pl)
      (recur (respond (update-in pl [:adventurer :tick] #(inc %)) (canonicalize command)))))))
