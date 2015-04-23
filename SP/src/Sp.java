import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Stack;

public class Sp {
	static ArrayList<Node> nodes = new ArrayList<Node>();
	static ArrayList<DirectedEdge> edges = new ArrayList<DirectedEdge>();
	private DirectedEdge[] edgeTo;
	private static float[] distTo;
	private MinPQ<Integer> pq;

	public Sp(EdgeWeightedDigraph G, int s) {
		edgeTo = new DirectedEdge[G.V()];
		distTo = new float[G.V()];
		pq = new MinPQ<Integer>(G.V());
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Float.POSITIVE_INFINITY;
		distTo[s] = 0.0f;
		pq.insert(s);
		while (!pq.isEmpty())
			relax(G, pq.delMin());

	}

	public void relax(EdgeWeightedDigraph G, int v) {
		for (DirectedEdge e : G.adj(v)) {
			int w = e.to();
			if (distTo[w] > distTo[v] + e.weight) {
				distTo[w] = distTo[v] + e.weight;
				edgeTo[w] = e;
				if (!pq.contains(w))
					pq.insert(w);
			}
		}
	}

	public boolean hasPathTo(int v) {
		return distTo[v] < Double.POSITIVE_INFINITY;
	}

	public Iterable<DirectedEdge> pathTo(int v) {
		if (!hasPathTo(v))
			return null;
		Stack<DirectedEdge> path = new Stack<Sp.DirectedEdge>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
			path.push(e);
		return path;
	}

	public float distTo(int v) {
		return distTo[v];
	}

	static class MinPQ<Key extends Comparable<Key>> {
		private Key[] pq;
		private int N = 0;

		public MinPQ(int maxN) {
			pq = (Key[]) new Comparable[maxN + 1];
		}

		public boolean contains(Key k) {
			for (Key key : pq) {
				if (k.equals(key))
					return true;
			}
			return false;
		}

		public boolean isEmpty() {
			return N == 0;
		}

		public int size() {
			return N;
		}

		public void insert(Key v) {
			pq[++N] = v;
			swim(N);
		}

		public void swim(int k) {
			while (k > 1 && bigger(k / 2, k)) {
				exch(k / 2, k);
				k = k / 2;
			}
		}

		public boolean bigger(int i, int j) {
			return distTo[(int) pq[i]] > distTo[(int) pq[j]];
		}

		public void sink(int k) {
			while (2 * k <= N) {
				int j = 2 * k;
				if (j < N && bigger(j, j + 1))
					j++;
				if (!bigger(k, j))
					break;
				exch(k, j);
				k = j;

			}
		}

		public void exch(int i, int j) {
			Key t = pq[i];
			pq[i] = pq[j];
			pq[j] = t;
		}

		public Key delMin() {
			Key min = pq[1];
			exch(1, N--);
			pq[N + 1] = null;
			sink(1);
			return min;
		}
	}

	static class EdgeWeightedDigraph {
		private final int V;
		private int E;
		private ArrayList<DirectedEdge>[] adj;

		public EdgeWeightedDigraph(int V) {
			this.V = V;
			this.E = 0;
			adj = (ArrayList<DirectedEdge>[]) new ArrayList[V];
			for (int v = 0; v < V; v++) {
				adj[v] = new ArrayList<Sp.DirectedEdge>();
			}
		}

		public int V() {
			return V;
		}

		public int E() {
			return E;
		}

		public void addEdge(DirectedEdge e) {
			adj[e.from()].add(e);
			E++;
		}

		public Iterable<DirectedEdge> adj(int v) {
			return adj[v];
		}

		public Iterable<DirectedEdge> edges() {
			ArrayList<DirectedEdge> list = new ArrayList<Sp.DirectedEdge>();
			for (int v = 0; v < V; v++) {
				for (DirectedEdge e : adj[v])
					list.add(e);
			}
			return list;
		}
	}

	static class DirectedEdge {
		int from;
		int to;
		float weight;

		public int from() {
			return from;
		}

		public int to() {
			return to;
		}

		public DirectedEdge(int e, int o, float w) {
			from = e;
			to = o;
			weight = w;
		}

		@Override
		public String toString() {
			return from + " to " + to + "--" + weight;
		}
	}

	static class Node {
		float x;
		float y;

		public boolean equals(Node n) {
			return (this.x == n.x) && (this.y == n.y);
		}

		public Node(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public float getx() {
			return x;
		}

		public float gety() {
			return y;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return x + "," + y;
		}
	}

	public static void readNodes() throws IOException {
		try {
			File file = new File("1.txt");
			if (!file.exists() || file.isDirectory())
				throw new FileNotFoundException();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp;
			while ((temp = br.readLine()) != null) {

				float x1 = Float.parseFloat(temp.split(",")[1]);
				float y1 = Float.parseFloat(temp.split(",")[2]);
				float x2 = Float.parseFloat(temp.split(",")[3]);
				float y2 = Float.parseFloat(temp.split(",")[4]);
				Node a = new Node(x1, y1);
				Node b = new Node(x2, y2);
				boolean aa = true;
				int either = -1, other = -1;
				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i).getx() == x1 && nodes.get(i).gety() == y1) {
						aa = false;
						either = i;
						break;
					}
				}
				if (aa) {
					nodes.add(a);
					either = nodes.size() - 1;
				}

				boolean bb = true;
				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i).getx() == x2 && nodes.get(i).gety() == y2) {
						bb = false;
						other = i;
						break;
					}
				}
				if (bb) {
					nodes.add(b);
					other = nodes.size() - 1;
				}
				edges.add(new DirectedEdge(either, other, Float.parseFloat(temp
						.split(",")[0])));
				edges.add(new DirectedEdge(other, either, Float.parseFloat(temp
						.split(",")[0])));
			}

		} catch (Exception e) {
		}

	}

	public static void writePointToMIF(String filename) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename, true)));
			for (int i = 0; i < nodes.size(); i++) {
				out.write("Text\r\n");
				out.write("    \"" + i + "\"\r\n");
				out.write("    " + nodes.get(i).getx() + " "
						+ nodes.get(i).gety() + " "
						+ (nodes.get(i).getx() + 0.002255f) + " "
						+ (nodes.get(i).gety() - 0.003649f) + "\r\n");
				out.write("    Font (\"Tahoma\",0,0,0)\r\n\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		try {
			readNodes();
			EdgeWeightedDigraph G = new EdgeWeightedDigraph(nodes.size());
			int s = 25;
			for (DirectedEdge edge : edges) {
				G.addEdge(edge);
			}
			int v = 134;

			Sp sp = new Sp(G, s);
			System.out.println("from: " + s + " to: " + v);
			for (DirectedEdge edge : sp.pathTo(v)) {
				System.out.println(edge);
			}
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("五级道路.MIF", true)));
				for (DirectedEdge edge : sp.pathTo(v)) {
					out.write("Line " + nodes.get(edge.from()).getx() + " "
							+ nodes.get(edge.from()).gety() + " "
							+ nodes.get(edge.to()).getx() + " "
							+ nodes.get(edge.to()).gety());
					out.write("\r\n    " + "Pen (5,3,0)\r\n");
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			 writePointToMIF("五级道路.MIF");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
