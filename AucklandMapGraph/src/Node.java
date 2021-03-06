
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node implements Comparable<Node>{

    private int id;  
    private Location location;
    private List<Segment> outNeighbours = new ArrayList<Segment>(2);
    private List<Segment> inNeighbours = new ArrayList<Segment>(2);
    
    private int depth;
    private int reachBack;
	private boolean visited;
	private Node previousNode;
	private double costSoFar;
	private double heuristic;


    public Node(int id, Location location){
	this.id = id;
	this.location = location;
	this.depth = Integer.MAX_VALUE;
	this.reachBack = Integer.MAX_VALUE;
    }
    
    public Node(String line){
	String[] values = line.split("\t");
	id = Integer.parseInt(values[0]);
	double lat = Double.parseDouble(values[1]);
	double lon = Double.parseDouble(values[2]);
	location = Location.newFromLatLon(lat, lon);
    }
    
	public int getID(){
		return id;
    }
    public Location getLocation(){
	return this.location;
    }

    public void addInSegment(Segment segment){
	inNeighbours.add(segment);
    }	
    public void addOutSegment(Segment segment){
	outNeighbours.add(segment);
    }
    public List<Segment> getOutNeighbours(){
	return outNeighbours;
    }	 
    public List<Segment> getInNeighbours(){
	return inNeighbours;
    }

    public void draw(Graphics g, Location origin, double scale, boolean b){
	Point point = location.getPoint(origin, scale);
	if(b==true)g.fillRect(point.x, point.y, 3, 3);
	else g.fillRect(point.x, point.y, 2, 2);
	}
    
	@Override
	public int hashCode() {
		int hash = 1277;
		hash = hash * ((int) Math.random()+ (int)System.currentTimeMillis());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}

    public double distanceTo(Location place){
	return location.distanceTo(place);
    }

	public int getDepth() {
		return depth;
	}

	public void setDepth(int d) {
		this.depth = d;
	}

	public int getReachBack() {
		return reachBack;
	}

	public void setReachBack(int reachBack) {
		this.reachBack = reachBack;
	}

	public double getCostSoFar() {
		return costSoFar;
	}

	public void setCostSoFar(double costSoFar) {
		this.costSoFar = costSoFar;
	}

	public Node getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}


	public boolean closeTo(Location place, double dist) {
		return location.closeTo(place, dist);
	}

	public void heuristic(Node goal) {

		double newX = this.getLocation().x * 111.0;
		double newY = this.getLocation().y * 88.649;
		double goalX = goal.getLocation().x * 111.0;
		double goalY = goal.getLocation().y * 88.649;
		double fin = Math.sqrt(Math.hypot(newY - goalY, newX - goalX));
		setHeuristic(fin + getCostSoFar());
	}

	private void setHeuristic(double estimate) {
		this.heuristic = estimate;
	}

	private double getEstimate() {
		return heuristic;
	}

	public String toString() {
		StringBuilder b = new StringBuilder(String.format(
				"Intersection %d: at %s; Roads:  ", id, location));
		Set<String> roadNames = new HashSet<String>();
		for (Segment neigh : inNeighbours) {
			roadNames.add(neigh.getRoad().getName());
		}
		for (Segment neigh : outNeighbours) {
			roadNames.add(neigh.getRoad().getName());
		}
		for (String name : roadNames) {
			b.append(name).append(", ");
		}
		return b.toString();
	}

	public int compareTo(Node arg0) {
		if (getEstimate() > arg0.getEstimate()) {
			return 1;
		} else if (getEstimate() < arg0.getEstimate()) {
			return -1;
		}
		return 0;
	}

}

