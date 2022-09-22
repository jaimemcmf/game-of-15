import java.util.Arrays;

class Node implements Comparable<Node>{
  private byte[] jogo;
  private String path;
  private int depth;
  private int cost;

  Node(byte[] j, String s, int d){
    jogo = j;
    path = s;
    depth = d;
    cost = 0;
  }

  Node(byte[] j, String s, int d, int c){
    jogo = j;
    path = s;
    depth = d;
    cost = c;
  }

  public int compareTo(Node n){
    if(this.cost < n.cost){
      return -1;
    }else if(this.cost > n.cost){
      return 1;
    }else{
      return 0;
    }
  }

  public void setCost(int c){
    cost = c;
  }

  public int getCost(){
    return cost;
  }

  public String getPath(){
    return path;
  }

  public byte[] getGame(){
    return jogo;
  }

  public int getDepth(){
    return depth;
  }

  public String printGame(){
    return Arrays.toString(jogo);
  }
}
