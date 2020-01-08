package bgu.spl.net.srv;

public class IntPair {

    private int left;
    private int right;

    public IntPair(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public int getLeft() {
        return left;
    }
}
