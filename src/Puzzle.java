import java.util.*;
import java.lang.Math;

class Puzzle{
  static int inversions(byte[] game){                                           //devolve o numero de 'inversions' de um jogo;
    int sum = 0, count =0;
    for(int i=0; i<16; i++){
      for(int j=i+1; j<16; j++){
        if((game[i] > game[j]) && (game[i] > 0) && (game[j] > 0)){
          sum++;
        }
      }
    }
    return sum;
  }

  static int blank_pos(byte[] game){                                            //devolve a posicao do 0 num jogo;
    int pos = 0;
    for(int i=0; i<game.length; i++){
      if(game[i] == 0) pos = i;
    }
    return pos;
  }

  static boolean blank_odd(byte[] game){                                        //devolve true se 0 está numa posicao par a contar da linha de baixo, false caso contrario;
    int pos = blank_pos(game);
    if(pos>11){
      return true;
    }else if(pos>7){
      return false;
    }else if(pos>3){
      return true;
    }else{
      return false;
    }
  }

  static boolean std_solvable(byte[] game){                                     //devolve true se e possivel chegar ao caso canonico, false caso contrario;
    return ((inversions(game)%2 == 0)) == (blank_odd(game));
  }

  static boolean solvable(byte[] game1, byte[] game2){                          // devolve true se e possivel chegar do caso inicial ao final, false caso contrario;
    return std_solvable(game1) == std_solvable(game2);
  }

  static boolean find_solution(Node n, byte[] fin){                             //devolve true se o jogo do no e igual ao jogo final dado no input;
    return Arrays.equals(n.getGame(), fin);
  }

  static Node getUp(Node pai){                                                  //devolve um no cujo jogo tem o 0 deslocado para cima, path + "U" e depth incrementado em um valor;
    byte[] jogo = pai.getGame().clone();
    int pos = blank_pos(pai.getGame());
    jogo[pos] = jogo[pos-4];
    jogo[pos-4] = 0;
    Node novo = new Node(jogo, pai.getPath() + "U", pai.getDepth() + 1);
    return novo;
  }

  static Node getDown(Node pai){                                                //devolve um no cujo jogo tem o 0 deslocado para baixo, path + "D" e depth incrementado em um valor;
    byte[] jogo = pai.getGame().clone();
    int pos = blank_pos(pai.getGame());
    jogo[pos] = jogo[pos+4];
    jogo[pos+4] = 0;
    Node novo = new Node(jogo, pai.getPath() + "D", pai.getDepth() + 1);
    return novo;
  }

  static Node getLeft(Node pai){                                                //devolve um no cujo jogo tem o 0 deslocado para a esquerda, path + "L" e depth incrementado em um valor;
    byte[] jogo = pai.getGame().clone();
    int pos = blank_pos(pai.getGame());
    jogo[pos] = jogo[pos-1];
    jogo[pos-1] = 0;
    Node novo = new Node(jogo, pai.getPath() + "L", pai.getDepth() + 1);
    return novo;
  }

  static Node getRight(Node pai){                                               //devolve um no cujo jogo tem o 0 deslocado para a direita, path + "R" e depth incrementado em um valor;
    byte[] jogo = pai.getGame().clone();
    int pos = blank_pos(pai.getGame());
    jogo[pos] = jogo[pos+1];
    jogo[pos+1] = 0;
    Node novo = new Node(jogo, pai.getPath() + "R", pai.getDepth() + 1);
    return novo;
  }

  static boolean contem(LinkedList<byte[]> list, byte[] game){                  //verifica se a lista ligada de arrays contem o jogo em questao, retornando true se contem e false caso contrario.
    byte sum = 0;
    for(int i=0; i<list.size(); i++){
      for(int j=0; j<16; j++){
        if(list.get(i)[j] == game[j]) sum++;
      }
      if(sum == 16) return true;
      sum = 0;
    }
    return false;
  }

  static int somatorio(byte[] atual, byte[] fin){                               //heuristica do somatorio, calcula quantas "pecas" estao fora do lugar.
    int sum = 0;
    for(int i=0; i<16; i++){
      if(atual[i] != fin[i]){
        sum++;
      }
    }
    return sum;
  }

  static int manhattan(byte[] atual, byte[] fin){                               //heuristica Manhattan Distance, calcula a distancia, para cada peça, entre o lugar atual e o lugar final e faz a soma total.
    int sum = 0;
    for(int i=0; i<16; i++){
      for(int j=0; j<16; j++){
        if(atual[i] == fin[j] && atual[i] != 0 && fin[j] != 0){
          sum += (Math.abs(i/4 - j/4) + Math.abs(i%4 - j%4));
        }
      }
    }
    return sum;
  }

  static void generate_DFS(Stack<Node> pilha, Node n){
    int pos = blank_pos(n.getGame());
    if(pos>3){
      pilha.push(getUp(n));                                                      //adiciona ao topo da pilha um novo no com o 0 em CIMA (se possivel).
    }
    if((pos+1)%4 != 0){
      pilha.push(getRight(n));                                                   //adiciona ao topo da pilha um novo no com o 0 a DIREITA (se possivel).
    }
    if(pos<12){
      pilha.push(getDown(n));                                                    //adiciona ao topo da um novo no com o 0 em BAIXO (se possivel).
    }
    if(pos%4 != 0){
      pilha.push(getLeft(n));                                                    //adiciona ao topo da um novo no com o 0 a ESQUERDA (se possivel).
    }
  }

  static String DFS(byte[] initial, byte[] fin){
    int num = 0;                                                                //faz a busca em profundidade (Depth-first Search).
    Stack<Node> active = new Stack<>();
    LinkedList<byte[]> used   = new LinkedList<>();
    Node root = new Node(initial, "", 0);
    active.push(root);
    while(!active.isEmpty()){
      Node temp = active.pop();
      if(find_solution(temp, fin)){
        return "Path: " + temp.getPath() + " | Depth: ";
      }
      if(!contem(used, temp.getGame())){
        generate_DFS(active, temp);
        used.add(temp.getGame());
      }
    }
    return "Solution not found";
  }

  static void generate_BFS(Queue<Node> list, Node n){
    int pos = blank_pos(n.getGame());
    if(pos>3){
      list.offer(getUp(n));                                                     //adiciona a lista um novo no com o 0 em CIMA (se possivel).
    }
    if((pos+1)%4 != 0){
      list.offer(getRight(n));                                                  //adiciona a lista um novo no com o 0 a DIREITA (se possivel).
    }
    if(pos<12){
      list.offer(getDown(n));                                                   //adiciona a lista um novo no com o 0 em BAIXO (se possivel).
    }
    if(pos%4 != 0){
      list.offer(getLeft(n));                                                   //adiciona a lista um novo no com o 0 a ESQUERDA (se possivel).
    }
  }

  static String BFS (byte[] initial, byte[] fin){                               //faz a busca em largura (Breath-first Search).
    int num = 0;
    Queue<Node> active = new LinkedList<>();
    Node root = new Node(initial, "", 0);
    active.offer(root);
    while(!active.isEmpty()){
      Node temp = active.poll();
      num++;
      if(find_solution(temp, fin)){
        return "Path: " + temp.getPath() + " | Depth: " + temp.getDepth();
      }
      generate_BFS(active, temp);
    }
    return "Solution not found";
  }

  static String IDFS(byte[] initial, byte[] fin){                               //faz a busca em profundidade iterativa (Iterative Depth-first Search).
    Stack<Node> active = new Stack<>();
    for(int i=0; i<=80; i++){
      Node root = new Node(initial, "", 0);
      active.push(root);
      while(!active.isEmpty()){
        Node temp = active.pop();
        if(find_solution(temp, fin)){
          return "Path: " + temp.getPath() + " | Depth: " + temp.getDepth();
        }
        if(temp.getDepth() < i){
          generate_DFS(active, temp);
        }
      }
      active.clear();
    }
    return "Solution not found";
  }

  static void generateGreedySum (PriorityQueue<Node> list, Node n, byte[] fin){
    int pos = blank_pos(n.getGame());
    if(pos>3){
      Node u = getUp(n);
      u.setCost(somatorio(u.getGame(), fin));
      list.add(u);
    }
    if((pos+1)%4 != 0){
      Node r = getRight(n);
      r.setCost(somatorio(r.getGame(), fin));
      list.add(r);
    }
    if(pos<12){
      Node d = getDown(n);
      d.setCost(somatorio(d.getGame(), fin));
      list.add(d);
    }
    if(pos%4 != 0){
      Node l = getLeft(n);
      l.setCost(somatorio(l.getGame(), fin));
      list.add(l);
    }
  }

  static String greedySum (byte[] initial, byte[] fin){                         //faz a busca gulosa com heuristica de somatorio.
    PriorityQueue<Node> active = new PriorityQueue<>();
    LinkedList<byte[]> used   = new LinkedList<>();
    Node root = new Node(initial, "", 0);
    active.add(root);
    while(!active.isEmpty()){
      Node temp = active.poll();
      if(find_solution(temp, fin)){
        return "Path: " + temp.getPath() + " | Depth: " + temp.getDepth();
      }
      if(!contem(used, temp.getGame())){
        generateGreedySum(active, temp, fin);
        used.add(temp.getGame());
      }
    }
    return "Solution not found";
  }

  static void generateGreedyMD(PriorityQueue<Node> list, Node n, byte[] fin){
    int pos = blank_pos(n.getGame());
    if(pos>3){
      Node u = getUp(n);
      u.setCost(manhattan(u.getGame(), fin));
      list.add(u);
    }
    if((pos+1)%4 != 0){
      Node r = getRight(n);
      r.setCost(manhattan(r.getGame(), fin));
      list.add(r);
    }
    if(pos<12){
      Node d = getDown(n);
      d.setCost(manhattan(d.getGame(), fin));
      list.add(d);
    }
    if(pos%4 != 0){
      Node l = getLeft(n);
      l.setCost(manhattan(l.getGame(), fin));
      list.add(l);
    }
  }

  static String greedyMD(byte[] initial, byte[] fin){                           //faz a busca gulosa com heurisica Manhattan Distance;
    PriorityQueue<Node> active = new PriorityQueue<>();
    LinkedList<byte[]> used   = new LinkedList<>();
    Node root = new Node(initial, "", 0);
    active.add(root);
    while(!active.isEmpty()){
      Node temp = active.poll();
      if(find_solution(temp, fin)){
        return "Path: " + temp.getPath() + " | Depth: " + temp.getDepth();
      }
      if(!contem(used, temp.getGame())){
        generateGreedyMD(active, temp, fin);
        used.add(temp.getGame());
      }
    }
    return "Solution not found";
  }

  static void generateAstarSum(PriorityQueue<Node> list, Node n, byte[] fin){
    int pos = blank_pos(n.getGame());
    if(pos>3){
      Node u = getUp(n);
      u.setCost(somatorio(u.getGame(), fin) + n.getDepth());
      list.add(u);
    }
    if((pos+1)%4 != 0){
      Node r = getRight(n);
      r.setCost(somatorio(r.getGame(), fin) + n.getDepth());
      list.add(r);
    }
    if(pos<12){
      Node d = getDown(n);
      d.setCost(somatorio(d.getGame(), fin) + n.getDepth());
      list.add(d);
    }
    if(pos%4 != 0){
      Node l = getLeft(n);
      l.setCost(somatorio(l.getGame(), fin) + n.getDepth());
      list.add(l);
    }
  }

  static String astarSum(byte[] initial, byte[] fin){                           //faz a busca A* com heuristica somatorio;
    PriorityQueue<Node> active = new PriorityQueue<>();
    LinkedList<byte[]> used = new LinkedList<>();
    Node root = new Node(initial, "", 0);
    active.add(root);
    while(!active.isEmpty()){
      Node temp = active.poll();
      if(find_solution(temp, fin)){
        return "Path: " + temp.getPath() + " | Depth: " + temp.getDepth();
      }
      if(!contem(used, temp.getGame())){
        generateAstarSum(active, temp, fin);
        used.add(temp.getGame());
      }
    }
    return "Solution not found";
  }

  static void generateAstarMD(PriorityQueue<Node> list, Node n, byte[] fin){
    int pos = blank_pos(n.getGame());
    if(pos>3){
      Node u = getUp(n);
      u.setCost(manhattan(u.getGame(), fin) + u.getDepth());
      list.add(u);
    }
    if((pos+1)%4 != 0){
      Node r = getRight(n);
      r.setCost(manhattan(r.getGame(), fin) + r.getDepth());
      list.add(r);
    }
    if(pos<12){
      Node d = getDown(n);
      d.setCost(manhattan(d.getGame(), fin) + d.getDepth());
      list.add(d);
    }
    if(pos%4 != 0){
      Node l = getLeft(n);
      l.setCost(manhattan(l.getGame(), fin) + l.getDepth());
      list.add(l);
    }
  }

  static String astarMD(byte[] initial, byte[] fin){                            //faz a busca A* com heuristica Manhattan Distance;
    PriorityQueue<Node> active = new PriorityQueue<>();
    LinkedList<byte[]> used = new LinkedList<>();
    Node root = new Node(initial, "", 0);
    active.add(root);
    while(!active.isEmpty()){
      Node temp = active.poll();
      if(find_solution(temp, fin)){
        return "Path: " + temp.getPath() + " | Depth: " + temp.getDepth();
      }else{
        if(!contem(used, temp.getGame())){
          generateAstarMD(active, temp, fin);
          used.add(temp.getGame());
        }
      }
    }
    return "Solution not found";
  }

  public static void main(String[] args){
    Scanner in = new Scanner(System.in);
    byte[] initial = new byte[16];
    byte[] fin     = new byte[16];
    System.out.println("Input the initial game state");
    for(int i=0; i<16; i++){
      initial[i] = in.nextByte();
      if(initial[i] < 0 || initial[i] > 15){
        System.out.println("Invalid input.");
        System.exit(0);
      }
    }
    System.out.println("Input the final game state");
    for(int j=0; j<16; j++){
      fin[j] = in.nextByte();
      if(fin[j] < 0 || fin[j] > 15){
        System.out.println("Invalid input.");
        System.exit(0);
      }
    }
    if((!solvable(initial, fin))){
      System.out.println("It is impossible to reach a solution.");
      System.exit(0);
    }
    System.out.println("Choose the search strategy:");
    System.out.println("1) DFS 2) BFS 3) IDFS 4) Greedy 5) A* ");
    byte search = in.nextByte();
    if(search >= 1 && search <= 3){
      if(search == 1) System.out.println(DFS(initial, fin));
      if(search == 2) System.out.println(BFS(initial, fin));
      if(search == 3) System.out.println(IDFS(initial, fin));
    }else if(search >= 4 && search <= 5){
      System.out.println("Choose the heuristic:");
      System.out.println("1) Sum 2) Manhattan Distance");
      byte euristic = in.nextByte();
      if(search == 4){
        if(euristic == 1){
          System.out.println(greedySum(initial, fin));
        }else{
          System.out.println(greedyMD(initial, fin));
        }
      }else{
        if(euristic == 1){
          System.out.println(astarSum(initial, fin));
        }else{
          System.out.println(astarMD(initial, fin));
        }
      }
    }else{
      System.out.println("Invalid input.");
    }
  }
}
