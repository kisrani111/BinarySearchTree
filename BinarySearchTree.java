import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BinarySearchTree<T extends Comparable<T>> implements Iterable<BinarySearchTree.Node<T>> {

    public static class Node<T> {
        public T data;
        public Node<T> left;
        public Node<T> right;
        public Node<T> parent;

        public Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;

        }
        @Override
        public String toString() {
            return data.toString();
        }
    }
    public Node<T> root;
    public String name;

    public BinarySearchTree(String name) {
        this.root = null;
        this.name = name;
    }

    public void insert(T newData) {
        this.root = insert(root, newData);
    }

    public Node<T> insert(Node<T> root, T newData) {
        if (root == null) {
            root = new Node<T>(newData);
            return root;
        }
        else if (root.data.compareTo(newData) >= 0) {
            root.left = insert(root.left, newData);
            root.left.parent = root;
        } else {
            root.right = insert(root.right, newData);
            root.right.parent = root;
        }
        return root;
    }
    public Node<T> getRoot(){
        return root;
    }
    @Override
    public Iterator<Node<T>> iterator() {
        return new myIterator<>(this);
    }

    @Override
    public String toString() {
        return "[" + this.name +"]" + "  " + root.toString() + printing(root);
    }

    private String printing(Node<T> current) {
        String x = "";
        if(current.left != null) {
            x = x + " L:(" + current.left.toString() + printing(current.left) + ")";
        }
        if(current.right != null) {
            x = x + " R:(" + current.right.toString() + printing(current.right) + ")";
        }
        return x;
    }

    public static class myIterator<T extends Comparable<T>> implements Iterator<Node<T>>{
        Node<T> current;
        public myIterator(BinarySearchTree<T> list)
        {
            current = list.getRoot();
            while (current.left != null)
                current = current.left;
        }
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Node<T> next() {
            if(!hasNext()) throw new NoSuchElementException();
            Node<T> r = current;
            if(current.right != null) {
                current = current.right;
                while (current.left != null)
                    current = current.left;
                return r;
            }
            while(true) {
                if(current.parent == null) {
                    current = null;
                    return r;
                }
                if(current.parent.left == current) {
                    current = current.parent;
                    return r;
                }
                current = current.parent;
            }
        }
    }
    public void addAll(List<T> asList) {
        asList.forEach(this::insert);
    }
    private ArrayList<T> getList(){
        ArrayList<T> tree = new ArrayList<T>();
        this.iterator().forEachRemaining(a -> tree.add(a.data));
        return tree;
    }
    public static <T extends Comparable<T>> List<T> merge(List<BinarySearchTree<T>> bstlist){
        final ArrayList<T>[] master = new ArrayList[]{new ArrayList<>()};
        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < bstlist.size(); i++) {
            int finalI = i;
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<T> tree = bstlist.get(finalI).getList();
                    synchronized(master[0]) {
                        master[0] = merging(tree, master[0]);
                    }
                }
                private <T extends Comparable<T>> ArrayList<T> merging(ArrayList<T> tree, ArrayList<T> master) {
                    ArrayList<T> temp = new ArrayList<>();
                    int i = 0;
                    int j = 0;
                    while (i < master.size() && j < tree.size()) {
                        if (master.get(i).compareTo(tree.get(j)) < 0) {
                            temp.add(master.get(i));
                            i++;
                        } else {
                            temp.add(tree.get(j));
                            j++;
                        }
                    }
                        if (i == master.size()) {
                            while (j < tree.size()) {
                                temp.add(tree.get(j));
                                j++;
                            }
                        }
                        if (j == tree.size()) {
                            while (i < master.size()) {
                                temp.add(master.get(i));
                                i++;
                            }
                        }
                    return temp;
                }
            });
        threads.add(t1);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return master[0];
    }
    }

