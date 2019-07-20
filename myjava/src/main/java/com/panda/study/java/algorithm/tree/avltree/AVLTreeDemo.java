package com.panda.study.java.algorithm.tree.avltree;

/**
 * Created by Administrator on 2019/7/19.
 */
public class AVLTreeDemo {
    public static void main(String[] args) {
//        int[] arr = {4,3,6,5,7,8};
//        int[] arr = {10,12,8,9,7,6};
        int[] arr = {10,11,7,6,8,9};
        AVLTree avlTree = new AVLTree();
        for (int i = 0; i < arr.length; i++) {
            avlTree.add(new Node(arr[i]));
        }
        //中序遍历
        System.out.println("中序遍历..");
        avlTree.infixOrder();

        System.out.println("在平衡处理后 - - - ");
        System.out.println("树的高度="+avlTree.getRoot().height());//4
        System.out.println("树的左子树高度="+avlTree.getRoot().leftHeight());//1
        System.out.println("树的右子树高度="+avlTree.getRoot().rightHeight());//3
        System.out.println("当前的根节点="+avlTree.getRoot());
        System.out.println("根节点的左子节点="+avlTree.getRoot().left.left);
        System.out.println("根节点的右子节点="+avlTree.getRoot().right.right);
    }
}
class AVLTree{
    private Node root;
    //查找要删除的节点
    public Node search(int value){
        if (root == null){
            return null;
        }else{
            return root.search(value);
        }
    }

    public Node getRoot() {
        return root;
    }

    //查找父结点
    public Node searchParent(int value){
        if (root == null){
            return null;
        }else {
            return root.searchParent(value);
        }

    }
    //编写方法
    //1.返回的是以node为根节点的二叉排序树的最小节点的值
    //2.删除node为根节点的二叉排序树的最小节点
    /**
     * @param node 传入的节点(当做二叉排序树的根节点)
     * @return 返回的是以node为根节点的二叉排序树的最小节点的值
     */
    public int delRightTreeMin(Node node){
        Node target = node;
        //循环的查找左子节点，就会找到最小值
        while (target.left != null){
            target = target.left;
        }
        //这是target就指向了最小节点
        //删除最小节点
        delNode(target.value);
        return target.value;
    }
    //删除节点
    public void delNode(int value){
        if (root == null){
            return;
        }else {
            //1.需求先去找到要删除的结点  targetNode
            Node targetNode = search(value);
            //如果没有找到要删除的结点
            if (targetNode == null){
                return;
            }
            //如果我们发现当前这颗二叉排序树只有一个节点
            if (root.left == null && root.right == null){
                root = null;
                return;
            }
            //去找到targetNode的父结点
            Node parent = searchParent(value);
            //第一种情况：如果要删除的是叶子节点
            if (targetNode.left == null && targetNode.right == null){
                //判断targetNode是父结点的左子节点还是右子节点
                if (parent.left != null && parent.left.value == targetNode.value){
                    parent.left = null;
                }else if (parent.right != null && parent.right.value == targetNode.value){
                    parent.right = null;
                }
                //第三种情况：删除有两颗子树的情况
            }else if (targetNode.left != null && targetNode.right != null){
                int minVal = delRightTreeMin(targetNode.right);
                targetNode.value = minVal;
                //第二种情况：删除只有一颗子树的情况
            }else {
                //如果要删除的结点有左子节点
                if (targetNode.left != null){
                    if (parent != null){
                        //如果targetNode是parent的左子节点
                        if (parent.left.value == value){
                            parent.left = targetNode.left;
                        }else {//targetNode是parent的右子节点
                            parent.right = targetNode.left;
                        }
                    }else {
                        root = targetNode.left;
                    }
                }else {//要删除的结点有右子节点
                    if (parent != null){
                        if (parent.left.value == value){
                            parent.left = targetNode.right;
                        }else {//如果targetNode是parent的右子节点
                            parent.right = targetNode.right;
                        }
                    }else {
                        root = targetNode.right;
                    }
                }
            }
        }
    }
    //添加节点的方法
    public void add(Node node){
        if (root == null){
            root = node;
        }else {
            root.add(node);
        }
    }
    public void infixOrder(){
        if (root != null){
            root.infixOrder();
        }else {
            System.out.println("二叉排序树为空，不能遍历...");
        }
    }
}
class Node{
    int value;
    Node left;
    Node right;

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                '}';
    }
    //返回左子树的高度
    public int leftHeight(){
        if (left == null){
            return 0;
        }
        return left.height();
    }
    //返回右子树的高度
    public int rightHeight(){
        if (right == null){
            return 0;
        }
        return right.height();
    }
    //右旋转
    private void rightRotate(){
        Node newNode = new Node(value);
        newNode.right = right;
        newNode.left = left.right;
        value = left.value;
        left = left.left;
        right = newNode;
    }
    //左旋转方法
    private void leftRotate(){
        //创建新的节点，以当前根节点的值创建
        Node newNode = new Node(value);
        //把新的节点的左子树设置成当前节点的左子树
        newNode.left = this.left;
        //把新的节点的右子树设置成当前节点的右子树的左子树
        newNode.right = this.right.left;
        //把当前节点的值替换成右子的值
        value = right.value;
        //把当前节点的右子树设置当前节点的右子树的右子树
        right = right.right;
        //把当前节点的左子树(左子节点)设置成新的节点
        left = newNode;
    }
    //返回以该节点为根节点的树的高度
    public int height(){
        return Math.max(left == null ? 0 : left.height(),right == null ? 0 : right.height()) + 1;
    }
    //查找要删除的节点
    /**
     * @param value 希望要删除的结点的值
     * @return 如果找到返回该结点，否则返回null
     */
    public Node search(int value){
        if (value == this.value){//找到该结点
            return this;
        }else if (value < this.value){//如果查找的值小于当前结点，向左子树递归查找
            //如果左子节点为空
            if (this.left == null){
                return null;
            }
            return this.left.search(value);
        }else{
            if (this.right == null){
                return null;
            }
            return this.right.search(value);
        }
    }
    //查找要删除结点的父节点
    /**
     *
     * @param value 要找到的节点的值
     * @return 返回的是要删除的节点的父节点，如果没有就返回null
     */
    public Node searchParent(int value){
        if ((this.left != null && this.left.value == value) ||
                (this.right != null && this.right.value == value)){
            return this;
        }else {
            //如果查找的值小于当前结点的值，并且当前结点的左子节点不为空
            if (value < this.value && this.left != null){
                return this.left.searchParent(value);//向左子树递归查找
            }else if (value >= this.value && this.right != null){
                return this.right.searchParent(value);//向右子树递归查找
            }else {
                return null;//没有找到父节点
            }
        }
    }
    public Node(int value) {
        this.value = value;
    }
    //添加节点的方法
    //递归的形式添加节点，注意需要满足二叉排序树的需求
    public void add(Node node){
        if (node == null){
            return;
        }
        //判断传入的节点的值和当前子树的根节点的值的关系
        if (node.value < this.value){
            if (this.left == null){
                this.left = node;
            }else {
                //递归的向左子树添加
                this.left.add(node);
            }
        }else {//添加的节点的值大于当前节点的值
            if (this.right == null){
                this.right = node;
            }else {
                //递归的向右子树添加
                this.right.add(node);
            }
        }
        //当添加完一个节点后，如果:右子树的高度 - 左子树的高度 > 1  左旋转
        if (rightHeight() - leftHeight() > 1){
            //如果它的右子树的左子树的高度大于它的右子树的右子树的高度
            if (right != null && right.leftHeight() > right.rightHeight()){
                //先对右子节点进行右旋转
                rightRotate();
                //然后对当前节点进行左旋转
                leftRotate();
            }else {
                leftRotate();//左旋转
            }
            return;//必须要!!!
        }
        //当添加完一个结点后，如果：左子树的高度 - 右子树的高度 > 1 右旋转
        if (leftHeight() - rightHeight() > 1){
            //如果它的左子树的右子树的高度大于它的左子树的左子树的高度
            if (left != null && left.rightHeight() > left.leftHeight()){
                //先对当前节点的左节点(左子树) -> 左旋转
                left.leftRotate();
                //再对当前节点进行右旋转即可
                rightRotate();
            }else {
                //直接进行右旋转即可
                rightRotate();
            }

        }
    }
    //中序遍历
    public void infixOrder(){
        if (this.left != null){
            this.left.infixOrder();
        }
        System.out.println(this);
        if (this.right != null){
            this.right.infixOrder();
        }
    }
}