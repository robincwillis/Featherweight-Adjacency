Title:Featherweights Adjacency Graph  
Author: Robin Willis  
Modified: 7/30/11  
Version:  1 
[Homepage](http://code.robincwillis.com)
___
**License:** MIT
___
**Description:**

This application loads a set of spatial descriptions as nodes into an interface which allows a user to graphically compose an adjacency graph for the set of nodes which is then used in determining configurations of each of those spaces.
___
**Required Libraries:**
- Processing
- ControlP5
- Trear Physics
- OpenGL
- Jexcel
- Guava (Google Commons)

___
**Notes:**

Currently there is no File IO functionality. Graphs are generated via data is read automatically via excel spreadsheets. One should place close attention to the formatting of these spreadsheets because basically the application just drills down into them for data.

- Update there is IO functionality but its a bit funky
- can read and write matrix
- read typology is probably broken
- wow this needs to be updated

___
**Interaction:**

Use the mouse to connect nodes and generate an adjacency graph

TODO:Export that graph into an excel spread sheet then load it into the graph viewer

