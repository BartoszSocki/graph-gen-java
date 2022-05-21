package algorithms;

import java.util.Comparator;

public class VertexPriorityQueue {

    private int size;
    private int capacity;

    private int[] verticies_indexes;
    private QueuedVertex[] vertices;

    private int getLeftChildIndex(int parent_index) {return 2 * parent_index+1;}
    private int getRightChildIndex(int parent_index) {return 2 * parent_index+2;}
    private int getParentIndex(int child_index) {return (child_index-1)/2;}

    public boolean isInTheQueue(int index){return verticies_indexes[index] != -1;}
    public int getSize() {return size;}

    public QueuedVertex getQueuedVertex(int index) { return vertices[verticies_indexes[index]];}
    private void swapQueuedVertex(int a_index, int b_index)
    {
        verticies_indexes[vertices[a_index].getId()] = b_index;
        verticies_indexes[vertices[b_index].getId()] = a_index;

        QueuedVertex tmp = vertices[a_index];
        vertices[a_index] = vertices[b_index];
        vertices[b_index] = tmp;
    }

    private void heapifyUp(int i)
    {
        int index = i;
        int parent_index = getParentIndex(index);

        while((parent_index>=0) && (vertices[parent_index].compareTo(vertices[index])>0))
        {
            swapQueuedVertex(index, parent_index);

            index = parent_index;
            parent_index = getParentIndex(index);
        }
    }

    private void heapifyDown(int i)
    {
        int index = i;
        int left_child_index = getLeftChildIndex(index);
        int right_child_index = getRightChildIndex(index);

        while(left_child_index < size)
        {
            int smaller_child_index = left_child_index;
            if(right_child_index < size && (vertices[right_child_index].compareTo(vertices[left_child_index]) < 0))
                smaller_child_index = right_child_index;

            if(vertices[index].compareTo(vertices[smaller_child_index]) < 0)
                break;

            swapQueuedVertex(index, smaller_child_index);

            index = smaller_child_index;
            left_child_index = getLeftChildIndex(index);
            right_child_index = getRightChildIndex(index);
        }
    }

    public void add(int index, int dist)
    {
        vertices[size] = new QueuedVertex(index, dist);
        verticies_indexes[index] = size;
        size++;
        heapifyUp(size-1);
    }

    public void add(int index)
    {
        vertices[size] = new QueuedVertex(index, Double.MAX_VALUE);
        verticies_indexes[index] = size;
        size++;
        heapifyUp(size-1);
    }

    public QueuedVertex poll(){
        QueuedVertex item = vertices[0];
        verticies_indexes[item.getId()] = -1;

        vertices[0] = vertices[size-1];
        size--;

        heapifyDown(0);

        return item;
    }

    public void update(int index, double new_dist)
    {
        int index_of_element_to_be_updated = verticies_indexes[index];
        if(new_dist > vertices[index_of_element_to_be_updated].getDist())
        {
            vertices[index_of_element_to_be_updated].setDist(new_dist);
            heapifyDown(index_of_element_to_be_updated);
        }
        else if(new_dist < vertices[index_of_element_to_be_updated].getDist())
        {
            vertices[index_of_element_to_be_updated].setDist(new_dist);
            heapifyUp(index_of_element_to_be_updated);
        }
    }
    public VertexPriorityQueue(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        this.vertices = new QueuedVertex[capacity];
        this.verticies_indexes = new int[capacity];
    }
}
