public class DoublyLinkedList {

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void addWrapper(Node data) {
        synchronized(this) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
            addLast(data);
        }
    }

    public void addLast(Node data) {
        if (isEmpty()) {
            data.setPrev(null);
            data.setNext(null);
            head = tail = data;
        } else {
            data.setPrev(tail);
            data.setNext(null);
            tail.setNext(data);
            tail = tail.getNext();
        }
        size++;
    }

    public void addFirst(Node data) {
        if (isEmpty()) {
            data.setPrev(null);
            data.setNext(null);
            head = tail = data;
        } else {
            data.setNext(head);
            head.setPrev(data);
            head = head.getPrev();
        }
        size++;
    }

    public Node removeFirstWrapper() {
        synchronized(this) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
            return removeFirst();
        }
    }

    public Node removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("ERROR: Doubly Linked List is Empty!");
        }
        Node temp = head;
        head = head.getNext();
        size--;
        if (isEmpty()) {
            tail = null;
        } else {
            head.setPrev(null);
        }
        return temp;
    }

    public Node removeLast() {
        if (isEmpty()) {
            throw new RuntimeException("ERROR: Doubly Linked List is Empty!");
        }
        Node temp = tail;
        tail = tail.getPrev();
        size--;
        if (isEmpty()) {
            head = null;
        } else {
            tail.setNext(null);
        }
        return temp;
    }

    public Node removeHighestPRWrapper() {
        synchronized(this) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
            return removeHighestPR();
        }
    }

    public Node removeMinCPUBurstWrapper() {
        synchronized(this) { /* Also synchronized(obj){ } structure is OK to use for protecting critical sections in your methods).*/
            return removeMinCPUBurst();
        }
    }

    public Node removeMinCPUBurst() {
        if (isEmpty()) {
            throw new RuntimeException("ERROR: Doubly Linked List is Empty!");
        }
        Node toRemove = head;
        Node temp = head;
        int min = temp.getCpuBurst()[temp.getCpuIndex()];
        while (temp != null) {
            if (min > temp.getCpuBurst()[temp.getCpuIndex()]) {
                min = temp.getCpuBurst()[temp.getCpuIndex()];
                toRemove = temp;
            }
            temp = temp.getNext();
        }
        return removeSpecificNode(toRemove);
    }

    public Node removeHighestPR() {
        if (isEmpty()) {
            throw new RuntimeException("ERROR: Doubly Linked List is Empty!");
        }
        Node toRemove = head;
        Node temp = head;
        int max = temp.getPr();
        while (temp != null) {
            if (max < temp.getPr()) {
                max = temp.getPr();
                toRemove = temp;
            }
            temp = temp.getNext();
        }
        return removeSpecificNode(toRemove);
    }

    public Node removeSpecificNode(Node data) {
        if (data.getPrev() == null) return removeFirst();
        if (data.getNext() == null) return removeLast();
        data.getNext().setPrev(data.getPrev());
        data.getPrev().setNext(data.getNext());
        data.setPrev(null);
        data.setNext(null);
        size--;
        return data;
    }
}
