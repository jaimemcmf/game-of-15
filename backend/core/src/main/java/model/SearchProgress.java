package model;

public class SearchProgress {
    private volatile int expandedNodes;

    public void setExpandedNodes(int expandedNodes) {
        this.expandedNodes = expandedNodes;
    }

    public int getExpandedNodes() {
        return expandedNodes;
    }
}