# Empire Builder
## Resources :
- Stone
- Wheat
- Gold
- Wood
## Sites
- Forest
- Stone quary
  - Impure
  - Normal
  - Pure
- Gold quary
  - Impure
  - Normal
  - Pure
## Buildings :
- Woodcutter
  - Produce wood
  - Place on forest
- Field
    - Produce wheat
    - Place everywhere
- Mine
    - Produce stone
    - Place on stone quary
- Gold mine
    - Produce gold
    - Place on gold quary
- House
  - Place everywhere
- Storage
  - Place everywhere
  - Increase the stock capacity
## Code hints
- The game is structured as MVC (Model / View / Controller)
### Model
- GameWorld's class is the main model
- GameWorld's class has a list of WorldChunk, it's all the chunks load
- GameWorld's class has a number for the map seed
- GameWorld's class has a Player, which contains the position of the player
- WorldChunk's class contains a lif of Placeables, it's the placeables the chunk contains
- WorldState's class contains a list of Placeables created by the player (e.g. Buildings...)
- WorldState's class contains a list of Sites used by the player (not the unused)
- WorldState's class contains a list of GridPositions. It's the positions of all the destroyed placeables. (e.g. Decorations, Sites...)
- ResourceStock's class contains the number of each resource (e.g. Gold, Rock, Wheat...)
- GameTime's class is the class that contains the elapsed time and the number of the current day
### Controller
- The main controller is the class Game
- The BuildingController's class controls the Building actions (e.g. Improve or destroy the building)
- The ConstructionUiController's class controls the ui's buttons to create building
### View
- The view contains the renderer and the ui
- The renderer is the game sprite
- The ui is the game overlay