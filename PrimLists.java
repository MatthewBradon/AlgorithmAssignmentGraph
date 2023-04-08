import java.io.*;

class Heap
{
    private int[] h;       // heap array
    private int[] hPos;    // hPos[h[k]] == k
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
        h = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = h[k];

        h[0] = 0;
        dist[0] = Integer.MIN_VALUE;

        //vertex using dist moved up heap
        while(dist[v] < dist[h[k/2]]){


            h[k] = h[k/2]; //parent vertex is assigned pos of child vertex

            hPos[h[k]] = k;//hpos modified for siftup

            k = k/2;// index of child assigned last parent to continue siftup
        }

        h[k] = v;//resting pos of vertex assigned to heap

        hPos[v] = k;//index of resting pos of vertex updated in hpos

        //display hpos array
       /* System.out.println("\nThe following is the hpos array after siftup: \n");

        for(int i = 0; i < MAX; i ++){

            System.out.println("%d", hPos[i]);
        }

        System.out.println("\n Following is heap array after siftup: \n");

        for (int i = 0; i < MAX; i ++ ){

            System.out.println("%d" , h[i]);

        }*/
    }


    //removing the vertex at top of heap
    //passed the index of the smallest value in heap
    //siftdown resizes and resorts heap

    public void siftDown( int k) 
    {
        int v, j;

        v = h[k];  

        while(k <= N/2){

            j = 2 * k;

            if(j < N && dist[h[j]] > dist[h[j + 1]]) ++j; //if node is > left increment j child

            if(dist[v] <= dist[h[j]]) break;//if sizeof parent vertex is less than child stop.

            h[k] = h[j];//if parent is greater than child then child assigned parent pos

            hPos[h[k]] = k;//update new pos of last child

            k = j;//assign vertex new pos
        }
        h[k] = v;//assign rest place of vertex to heap
        hPos[v] = k;//update pos of the vertex in hpos array
    }


    public void insert( int x) 
    {
        h[++N] = x;//assign new vertex to end of heap
        siftUp( N);//pass index at end of heap to siftup
    }


    public int remove() 
    {   
        int v = h[1];
        hPos[v] = 0; // v is no longer in heap
        h[N+1] = 0;  // put null node into empty spot

        h[1] = h[N--];//last node of heap moved to top
        siftDown(1);//pass index at top to siftdown

        return v;//return vertex at top of heap
    }

}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }

    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;

    // used for traversing graph
    private int[] visited;
    private int id;


    // default constructor
    public Graph(String graphFile) throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        String splits = " ";  // multiple whitespace as delimiter
        String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);

        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        // create sentinel node
        z = new Node(); 
        z.next = z;

        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;               

       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);

            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));    

            // write code to put edge into adjacency matrix 
            t = new Node(); t.vert = v; t.wgt = wgt; t.next = adj[u]; adj[u] = t;

            t = new Node(); t.vert = u; t.wgt = wgt; t.next = adj[v]; adj[v] = t;    

        }          
    }

    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    // method to display the graph representation
    public void display() {
        int v;
        Node n;

        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }


    //use the breath first approach to add verts from the adj list to heap
    //uses 3 arrays where array = # of verts in graph
    //parent array to keep track of parent verts
    // a dist matrix to keep track of dist between it and parent
    //hpos array to track pos of vert in the heap

    public void MST_Prim(int s)
    {
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        //declare 3 arrays
        dist = new int[V + 1];
        parent = new int[V + 1];
        hPos = new int[V +1];

        //initialise arrays
        for(v = 0; v <= V; ++v){

            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0;

        //d.dequeue is pq.remove

        Heap pq =  new Heap(V, dist, hPos);
        pq.insert(s);

        while (! pq.isEmpty())  
        {
            // most of alg here
            v = pq.remove();
            wgt_sum += dist[v];//add the dist/wgt of vert removed to mean spanning tree

            //System.out.println("\nAdding to MST edge {0} -- ({1}) -- {2}", toChar(parent[v]), dist[v], toChar[v]);

            dist[v] = -dist[v];//mark it as done by making it negative

            for(t = adj[v]; t != z; t = t.next){

                u = t.vert;
                wgt = t.wgt;

                if(wgt < dist[u]){ //weight less than current value

                    dist[u] = wgt;
                    parent[u] = v;

                    if(hPos[u] == 0)// not in heap insert
                        pq.insert(u);
                    else
                        pq.siftUp(hPos[u]);//if already in heap siftup the modified heap node
                }
            }

        }
        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");

        //display hPos array
        /*System.out.println("\nhPos array after siftUp: \n");

        for(int i = 0; i < V; i ++){

            System.out.println("%d", hPos[i]);
        }*/

        mst = parent;                           
    }

    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println("");
    }

}

public class PrimLists{
    public static void main(String[] args) throws IOException{
        int s = 2;
        String fname = "wGraph1.txt";               

        Graph g = new Graph(fname) ;


        g.MST_Prim(s);


    }


}