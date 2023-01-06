class Node:
    def __init__(self, data=None):
        self.data = data
        self.left = None
        self.right = None

    def __str__(self):
        return str(self.data)

    def __iter__(self):
        return self.inorder(self)

    def inorder(self, node):
        if node is None or node.data is None:
            return
        if node.left is not None:
            for x in self.inorder(node.left):
                yield x
        yield node
        if self.right is not None:
            for x in self.inorder(node.right):
                yield x
        pass


class BinarySearchTree:

    def __init__(self, name, root):
        self.name = name
        self.root = root

    def insert(self, root, newdata):

        if root is None or root.data is None:
            root = Node(newdata)
            return root
        elif root.data > newdata:
            root.left = self.insert(root.left, newdata)
        else:
            root.right = self.insert(root.right, newdata)
        return root

    def inserting(self, newdata):
        self.root = self.insert(self.root, newdata)

    def add_all(self, *list):
        for i in list:
            self.inserting(i)

    def __iter__(self):
        return self.inorder(self.root)

    def inorder(self, node):
        if node is None:
            return
        if node.left is not None:
            for x in self.inorder(node.left):
                yield x
        yield node
        if node.right is not None:
            for x in self.inorder(node.right):
                yield x

    def __str__(self):
        return "[" + self.name + "] " + self.root.__str__() + (self.printing(self.root)).__str__()

    def printing(self, current):
        x = ""
        if current.left is not None:
            x = x + " L:(" + current.left.__str__() + self.printing(current.left) + ")"
        if current.right is not None:
            x = x + " R:(" + current.right.__str__() + self.printing(current.right) + ")"
        return x
