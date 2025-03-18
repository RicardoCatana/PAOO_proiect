package ai;

public class Node {
    Node parent; // Referință către nodul părinte
    public int col; // Coloana nodului
    public int row; // Rândul nodului
    int gCost; // Costul de la nodul de început până la acest nod
    int hCost; // Costul estimat de la acest nod până la destinație
    int fCost; // Suma dintre gCost si hCost
    boolean solid; // Indică dacă nodul este solid (obstacol)
    boolean open; // Indică daca nodul este în lista de noduri deschise
    boolean checked; // Indică daca nodul a fost deja evaluat

    /*
    Inițializează un nod cu poziția sa (rândul și coloana)
     */
    public Node(int col, int row){
        this.col = col;
        this.row = row;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
