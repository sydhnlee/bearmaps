# Bear Maps
CS 61B. Data Structures

Bear Maps is a clone of Google Maps that spans the surrounding area of UC Berkeley. Following a successful implementation, the web mapping application should be capable of performing the "smart" features commonly associated with Google Maps. Such features include dragging/zooming, auto-complete search suggestions, map rasterization, and pathing generated by an A* search algorithm.

<img src="images/demo.gif" alt="demo_gif" width="100%"/>

Implementation | Description
------- | -------
[RasterAPIHandler](https://github.com/sydhnlee/BearMaps/blob/main/bearmaps/server/handler/impl/RasterAPIHandler.java) | Renders map images given a user's display area and level of zoom.
[AugmentedStreetMapGraph](https://github.com/sydhnlee/BearMaps/blob/main/bearmaps/AugmentedStreetMapGraph.java) | Graph representation of the contents of Berkeley Open Street Map data.
[AStarSolver](https://github.com/sydhnlee/BearMaps/blob/main/bearmaps/utils/graph/AStarSolver.java) | The A* search algorithm finds the shortest path between two points in Berkeley.
[MyTrieSet](https://github.com/sydhnlee/BearMaps/blob/main/bearmaps/MyTrieSet.java) | TrieSet backs the autocomplete search feature, matching a prefix to valid location names in Θ(k) time, where k in the number of words sharing the prefix.
[KDTree](https://github.com/sydhnlee/BearMaps/blob/main/bearmaps/utils/ps/KDTree.java) | K-Dimensional tree backs the A* search algorithm, allowing efficient nearest neighbor lookup averaging O(log(n)) time.
[MinHeapPQ](https://github.com/sydhnlee/BearMaps/blob/main/bearmaps/utils/pq/MinHeapPQ.java) | Min-heap priority queue backs the A* search algorithm.
