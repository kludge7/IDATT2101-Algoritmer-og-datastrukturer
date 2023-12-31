public class ExpressionTree {
    public String inorderTraversalString(TreeNode treeNode, String string) {
        if(treeNode == null) {
            return string;
        }
        if (treeNode.element.equals("+") || treeNode.element.equals("-")) {
            string += "(";
            string = inorderTraversalString(treeNode.left, string);
            string += treeNode.element;
            string = inorderTraversalString(treeNode.right, string);
            string += ")";
        } else {
            string = inorderTraversalString(treeNode.left, string);
            string += treeNode.element;
            string = inorderTraversalString(treeNode.right, string);
        }
        return string;
    }

    public int inorderTraversalCalculation(TreeNode treeNode) {
        if(treeNode.element == "+") {
            return inorderTraversalCalculation(treeNode.left) + inorderTraversalCalculation(treeNode.right);
        } else if (treeNode.element == "-") {
            return inorderTraversalCalculation(treeNode.left) - inorderTraversalCalculation(treeNode.right);
        } else if (treeNode.element == "*") {
            return inorderTraversalCalculation(treeNode.left) * inorderTraversalCalculation(treeNode.right);
        } else if (treeNode.element == "/") {
            return inorderTraversalCalculation(treeNode.left) / inorderTraversalCalculation(treeNode.right);
        } else {
            return (Integer) treeNode.element;
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode("/");
        TreeNode rootLeft = new TreeNode("*");
        TreeNode rootLeftLeft = new TreeNode(3);
        TreeNode rootLeftRight = new TreeNode("+");
        TreeNode rootLeftRightLeft = new TreeNode(2);
        TreeNode rootLeftRightRight = new TreeNode(4);
        TreeNode rootRight = new TreeNode("-");
        TreeNode rootRightLeft = new TreeNode(7);
        TreeNode rootRightRight = new TreeNode("*");
        TreeNode rootRightRightLeft = new TreeNode(2);
        TreeNode rootRightRightRight = new TreeNode(2);
        root.left = rootLeft;
        rootLeft.left = rootLeftLeft;
        rootLeft.right = rootLeftRight;
        rootLeftRight.left = rootLeftRightLeft;
        rootLeftRight.right = rootLeftRightRight;

        root.right = rootRight;
        rootRight.left = rootRightLeft;
        rootRight.right = rootRightRight;
        rootRightRight.left = rootRightRightLeft;
        rootRightRight.right = rootRightRightRight;

        ExpressionTree expressionTree = new ExpressionTree();
        System.out.println("Aritmetisk uttrykk for uttrykkstreet er: " + expressionTree.inorderTraversalString(root, ""));
        System.out.println("Summen av uttrykkstreet er: " + expressionTree.inorderTraversalCalculation(root));

    }
}