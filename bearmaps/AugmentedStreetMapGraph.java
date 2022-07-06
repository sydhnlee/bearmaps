package bearmaps;

import bearmaps.utils.Constants;
import bearmaps.utils.graph.streetmap.StreetMapGraph;
import bearmaps.utils.graph.streetmap.Node;
import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.Point;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, Sydney Lee
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private Map<Point, Node> pointTonode = new HashMap<>();
    private KDTree kdtree;
    private MyTrieSet trie = new MyTrieSet();
    private Map<String, Node> cleanTonode = new HashMap<>();

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getAllNodes();
        List<Point> point_list = new ArrayList<>();
        for (Node node : nodes) {
            double x = projectToX(node.lon(), node.lat());
            double y = projectToY(node.lon(), node.lat());
            Point point = new Point(x, y);
            pointTonode.put(point, node);
            if (neighbors(node.id()).size() != 0) {
                point_list.add(point);
            }
            if (node.name() != null) {
                String cleanName = cleanString(node.name());
                int i = 1;
                if (!cleanTonode.containsKey(cleanName + i)) {
                    cleanTonode.put(cleanName + i, node);
                } else {
                    i++;
                    while (true) {
                        if (!cleanTonode.containsKey(cleanName + i)) {
                            cleanTonode.put(cleanName + i, node);
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                trie.add(cleanName);
            }
        }
        kdtree = new KDTree(point_list);
    }


    /**
     * For Project Part III
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        double x = projectToX(lon, lat);
        double y = projectToY(lon, lat);
        Point nearestPoint = kdtree.nearest(x, y);
        return pointTonode.get(nearestPoint).id();
    }

    /**
     * Return the Euclidean x-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean x-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToX(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double b = Math.sin(dlon) * Math.cos(phi);
        return (K0 / 2) * Math.log((1 + b) / (1 - b));
    }

    /**
     * Return the Euclidean y-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean y-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToY(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double con = Math.atan(Math.tan(phi) / Math.cos(dlon));
        return K0 * (con - Math.toRadians(ROOT_LAT));
    }


    /**
     * For Project Part IV (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> string_with_prefix = new ArrayList<>();
        String cleanPrefix = cleanString(prefix);
        List<String> clean_with_prefix = trie.keysWithPrefix(cleanPrefix);
        for (String s : clean_with_prefix) {
            int i = 2;
            if (!cleanTonode.containsKey(s + i)) {
                string_with_prefix.add(cleanTonode.get(s + 1).name());
            } else {
                i = 1;
                while(true) {
                    if (!cleanTonode.containsKey(s + i)) {
                        break;
                    } else {
                        string_with_prefix.add(cleanTonode.get(s + i).name());
                        i++;
                    }
                }
            }
        }
        return string_with_prefix;
    }

    /**
     * For Project Part IV (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleanName = cleanString(locationName);
        List<Map<String, Object>> locations = new ArrayList<>();
        if (trie.contains(cleanName)) {
            int i = 2;
            if (!cleanTonode.containsKey(cleanName + i)) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", cleanTonode.get(cleanName + 1).name());
                map.put("lon", cleanTonode.get(cleanName + 1).lon());
                map.put("id", cleanTonode.get(cleanName + 1).id());
                map.put("lat", cleanTonode.get(cleanName + 1).lat());
                locations.add(map);
            } else {
                i = 1;
                while(true) {
                    if (!cleanTonode.containsKey(cleanName + i)) {
                        break;
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", cleanTonode.get(cleanName + i).name());
                        map.put("lon", cleanTonode.get(cleanName + i).lon());
                        map.put("id", cleanTonode.get(cleanName + i).id());
                        map.put("lat", cleanTonode.get(cleanName + i).lat());
                        locations.add(map);
                        i++;
                    }
                }
            }
        }
        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }


    /**
     * Scale factor at the natural origin, Berkeley. Prefer to use 1 instead of 0.9996 as in UTM.
     * @source https://gis.stackexchange.com/a/7298
     */
    private static final double K0 = 1.0;
    /** Latitude centered on Berkeley. */
    private static final double ROOT_LAT = (Constants.ROOT_ULLAT + Constants.ROOT_LRLAT) / 2;
    /** Longitude centered on Berkeley. */
    private static final double ROOT_LON = (Constants.ROOT_ULLON + Constants.ROOT_LRLON) / 2;

}
