
	            _                          _   _                             
	           | |  o                  o  | | | | o                   |      
	 ,_    __  | |      _  _              | | | |     ,   __   __   __|   _  
	/  |  /  \_|/ \_|  / |/ |  |  |  |_|  |/  |/  |  / \_/    /  \_/  |  |/  
	   |_/\__/  \_/ |_/  |  |_/ \/ \/  |_/|__/|__/|_/ \/ \___/\__/ \_/|_/|__/


Title:Featherweights Adjacency Graph
Author: Robin Willis
Modified: 7/30/11
Version: 
--
License: MIT

Description: This application loads a set of spatial descriptions as nodes into an interface which allows a user to graphically compose an adjacency graph for the set of nodes which is then used in determining configurations of each of those spaces.

Libraries:
Processing
ControlP5
Trear Physics
OpenGL
Jexcel
Guava (Google Commons)

Notes:
Currently there is no File IO functionality. Graphs are generated via data is read automatically via excel spreadsheets. One should place close attention to the formatting of these spreadsheets because basically the application just drills down into them for data.

Update there is IO functionality but its a bit funky
can read and write matrix
read typology is probably broken
wow this needs to be updated


Interaction:
Use the mouse to connect nodes and generate an adjacency graph
TODO:Export that graph into an excel spread sheet then load it into the graph viewer

