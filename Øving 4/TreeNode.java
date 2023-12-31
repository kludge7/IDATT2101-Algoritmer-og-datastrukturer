public class TreeNode {
    Object element;
    TreeNode left;
    TreeNode right;

    public TreeNode(Object element) {
        this.element = element;
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
