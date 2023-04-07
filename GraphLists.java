// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;

class GraphQueue {
    private int[] q;
    private int capacity;
    private int size;
    private int first;
    private int last;

    public GraphQueue(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.first = 0;
        this.last = capacity - 1;
        q = new int[this.capacity];
    }

    public void printQueue() {
        for (int i = first; i <= last; i++) {
            System.out.print(q[i] + " ");
        }
        System.out.println();
    }

    public void enqueue(int v) {
        if (isFull()) {
            System.out.println("Queue is full");
            return;
        }
        last = (last + 1) % capacity;
        q[last] = v;
        size++;
    }

    public int dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return -1;
        }
        int v = q[first];
        first = (first + 1) % capacity;
        size--;
        return v;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public boolean isFull() {
        return (size == capacity);
    }
}

class Heap
{
    private int[] a;       // heap array
    private int[] hPos;    // hPos[a[k]] == k
    private int[] dist;    // dist[v] = priority of v
    private int MAX;

    private int N;         // heap size

    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        MAX = maxSize;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = a[k];

        a[0] = 0;
        dist[0] = Integer.MIN_VALUE;

        //vertex using dist moved up heap
        while(dist[v] < dist[a[k/2]]){


            a[k] = a[k/2]; //parent vertex is assigned pos of child vertex

            hPos[a[k]] = k;//hpos modified for siftup

            k = k/2;// index of child assigned last parent to continue siftup
        }

        a[k] = v;//resting pos of vertex assigned to heap

        hPos[v] = k;//index of resting pos of vertex updated in hpos

        //display hpos array
       /* System.out.println("\nThe following is the hpos array after siftup: \n");

        for(int i = 0; i < MAX; i ++){

            System.out.println("%d", hPos[i]);
        }

        System.out.println("\n Following is heap array after siftup: \n");

        for (int i = 0; i < MAX; i ++ ){

            System.out.println("%d" , a[i]);

        }*/
    }


    //removing the vertex at top of heap
    //passed the index of the smallest value in heap
    //siftdown resizes and resorts heap

    public void siftDown( int k) 
    {
        int v, j;

        v = a[k];  

        while(k <= N/2){

            j = 2 * k;

            if(j < N && dist[a[j]] > dist[a[j + 1]]) ++j; //if node is > left increment j child

            if(dist[v] <= dist[a[j]]) break;//if sizeof parent vertex is less than child stop.

            a[k] = a[j];//if parent is greater than child then child assigned parent pos

            hPos[a[k]] = k;//update new pos of last child

            k = j;//assign vertex new pos
        }
        a[k] = v;//assign rest place of vertex to heap
        hPos[v] = k;//update pos of the vertex in hpos array
    }


    public void insert( int x) 
    {
        a[++N] = x;//assign new vertex to end of heap
        siftUp( N);//pass index at end of heap to siftup
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot

        a[1] = a[N--];//last node of heap moved to top
        siftDown(1);//pass index at top to siftdown

        return v;//return vertex at top of heap
    }

}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;

        public Node() {
            this.vert = 0;
            this.wgt = 0;
            this.next = null;
        }

        public Node(int v, int w, Node n) {
            this.vert = v;
            this.wgt = w;
            this.next = n;
        }
    }

    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst, spt;

    // used for traversing graph
    private int[] visited;
    private int id;

    // default constructor
    public Graph(String graphFile) throws IOException {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        String splits = " "; // multiple whitespace as delimiter
        String line = reader.readLine();
        System.out.println(line);
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);

        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        // create sentinel node
        z = new Node();
        z.next = z;

        // create adjacency lists, initialised to sentinel node z
        adj = new Node[V + 1];
        for (v = 1; v <= V; ++v) {
            adj[v] = z;
        }

        // read the edges
        System.out.println("Reading edges from text file");
        for (e = 1; e <= E; ++e) {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]);
            wgt = Integer.parseInt(parts[2]);

            System.out.println("" + toChar(u) + "-(" + wgt + ")-" + toChar(v));

            // write code to put edge into adjacency matrix
            t = new Node(v, wgt, adj[u]);
            adj[u] = t;

            t = new Node(u, wgt, adj[v]);
            adj[v] = t;
            



        }
    }

    // convert vertex into char for pretty printing
    private char toChar(int u) {
        return (char) (u + 64);
    }

    // method to display the graph representation
    public void display() {
        int v;
        Node n;

        for (v = 1; v <= V; ++v) {
            System.out.print("\nadj[" + toChar(v) + "] ->");
            for (n = adj[v]; n != z; n = n.next)
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");
        }
        System.out.println("");
    }

    public void printArray(int[] a) {
        for (int i = 1; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    public void MST_Prim(int s) {
        int v, u;
        int wgt, wgt_sum = 0;
        int[] dist, parent, hPos;
        Node t;

        // code here
        dist = new int[V + 1];
        hPos = new int[V + 1];
        parent = new int[V + 1];

        for (v = 0; v <= V; v++) {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0;

        Heap h = new Heap(V, dist, hPos);
        h.insert(s);

        while (!h.isEmpty()) {

            v = h.remove();
            wgt_sum += dist[v];
            dist[v] = -dist[v];

            for (t = adj[v]; t != z; t = t.next) {
                u = t.vert;
                wgt = t.wgt;
                if (wgt < dist[u]) {
                    dist[u] = wgt;
                    parent[u] = v;
                    if (hPos[u] == 0) {
                        h.insert(u);
                    } else {
                        h.siftUp(hPos[u]);
                    }
                }

            }
            printArray(dist);
            printArray(parent);
        }

        for (v = 0; v <= V; v++) {
            System.out.println("v = " + v + " dist = " + dist[v] + " parent = " + parent[v]);
        }
        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");

        mst = parent;
    }

    public void showMST() {
        System.out.print("\n\nMinimum Spanning tree parent array is:\n");
        for (int v = 1; v <= V; ++v)
            System.out.println(toChar(v) + " -> " + toChar(mst[v]));
        System.out.println("");
    }

    public void SPT_Dijkstra(int s) {
        int v, u;
        int wgt;
        int[] dist, parent, hPos;
        Node t;
        dist = new int[V + 1];
        hPos = new int[V + 1];
        parent = new int[V + 1];

        for (v = 1; v <= V; v++) {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        

        dist[s] = 0;
        v = s;

        Heap h = new Heap(V, dist, hPos);
        h.insert(s);
        while (!h.isEmpty()){
            for(t = adj[v]; t != z; t = t.next){
                u = t.vert;
                System.out.println("u = " + u + " v = " + v);
                wgt = t.wgt;

                if (dist[u] > dist[v] + wgt){

                    dist[u] = dist[v] + wgt;
                    parent[u] = v;
                    
                    if (hPos[u] == 0){
                        h.insert(u);
                    } else {
                        System.out.println("test sift up U: " + u + " hPos[u]: " + hPos[u]);

                        h.siftUp(hPos[u]);
                    }
                    
                }
            }
            v = h.remove();
        }
        
        for (v = 0; v <= V; v++) {
            System.out.println("v = " + toChar(v) + " parent = " + toChar(parent[v]) + " dist = " + dist[v]);

        }
        spt = parent;
    }

    public void DF(int s) {
        int v;
        visited = new int[V + 1];
        for (v = 1; v <= V; ++v)
            visited[v] = 0;

        DFTraverse(s);
    }

    public void DFTraverse(int v) {
        Node t;
        visited[v] = 1;
        //Goes through the neighbors of the vertex and if not visited it will call the method again
        System.out.print(toChar(v) + " ");
        for (t = adj[v]; t != z; t = t.next) {
            if (visited[t.vert] == 0) {
                DFTraverse(t.vert);
            }
        }
    }

    public void breadthFirst(int v) {
        int u;
        Node t;
        GraphQueue q = new GraphQueue(V);
        visited = new int[V + 1];

        //Sets all the vertices to not visited
        for (u = 1; u <= V; ++u) {
            visited[u] = 0;
        }

        visited[v] = 1;
        System.out.print(toChar(v) + " ");

        q.enqueue(v);

        while (!q.isEmpty()) {
            //Goes through the neighbors of the dequeued vertex and if not visted it will enqueue it
            v = q.dequeue();
            for (t = adj[v]; t != z; t = t.next) {
                u = t.vert;
                if (visited[u] == 0) {
                    visited[u] = 1;
                    System.out.print(toChar(u) + " ");
                    q.enqueue(u);
                }
            }
        }
    }

}

public class GraphLists {
    public static void main(String[] args) throws IOException {
        int s = 12;
        String fname = "wGraph1.txt";

        Graph g = new Graph(fname);

        g.display();

        //g.DF(s);
        System.out.println();
        //g.breadthFirst(s);
        System.out.println();
        //g.MST_Prim(s);
        g.SPT_Dijkstra(s);
    }
}
