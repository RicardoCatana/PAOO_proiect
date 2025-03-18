package ai;
import Main.GamePanel;
import entity.Entity;
import java.util.ArrayList;

public class PathFinder {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp){
        this.gp = gp;
        instantiateNodes();
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Inițializarea matricei de noduri
    public void instantiateNodes(){
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];
        int col=0;
        int row=0;

        while(col < gp.maxWorldCol && row < gp.maxWorldCol){
            node[col][row] = new Node(col,row);
            col++;
            if(col == gp.maxWorldCol){
                col=0;
                row++;
            }
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Resetează starea nodurilor și a altor setări
    public void resetNodes(){
        int col=0;
        int row=0;

        while(col < gp.maxWorldCol && row < gp.maxWorldCol) {
            //RESET OPEN, CHECKED AND SOLID STATE;
            node[col][row].open=false;
            node[col][row].checked=false;
            node[col][row].solid=false;

            col++;
            if(col == gp.maxWorldCol){
                col=0;
                row++;
            }
        }

        //RESET OTHER SETTINGS
        openList.clear();
        pathList.clear();
        goalReached=false;
        step=0;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Setarea nodurilor de start și destinație și marcarea nodurilor solide
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity){
        resetNodes();

        //SET START AND GOAL NODE
        startNode=node[startCol][startRow];
        currentNode=startNode;
        goalNode=node[goalCol][goalRow];
        openList.add(currentNode);
        int col=0;
        int row=0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow){
            //SET SOLID NODE | CHECK TILES
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if(gp.tileM.tile[tileNum].collision == true){
                node[col][row].solid=true;
            }
            //SET COST
            getCost(node[col][row]);
            col++;
            if(col == gp.maxWorldCol){
                col=0;
                row++;
            }
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Calcularea costurilor G, H și F
    public void getCost(Node node){
        //G COST
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost=xDistance+yDistance;

        //H COST
        xDistance=Math.abs(node.col - goalNode.col);
        yDistance=Math.abs(node.row - goalNode.row);
        node.hCost=xDistance+yDistance;

        //F COST
        node.fCost=node.gCost+node.hCost;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă pentru căutarea căii folosind algoritmul A*. Aceasta explorează nodurile vecine pentru a găsi cel mai bun nod
    până când nodul destinație este atins sau este atins un număr de pași
     */
    public boolean search(){
        while(goalReached == false && step < 500){
            int col= currentNode.col;
            int row=currentNode.row;

            //CHECK THE CURRENT NODE
            currentNode.checked=true;
            openList.remove(currentNode);

            //OPEN THE UP NODE
            if(row - 1 >= 0){
                openNode(node[col][row-1]);
            }
            //OPEN THE LEFT NODE
            if(col - 1 >=0){
                openNode(node[col-1][row]);
            }
            //OPEN THE DOWN NODE
            if(row + 1 < gp.maxWorldRow){
                openNode(node[col][row+1]);
            }
            //OPEN THE RIGHT NODE
            if(col+1<gp.maxWorldCol){
                openNode(node[col+1][row]);
            }

            //FIND THE BEST NODE
            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            for(int i = 0;i < openList.size(); i++) {
                //CHECK IF THIS NODE'S F COST IS BETTER
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //IF F COST IS EQUAL, CHECK THE G COST
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost)
                        bestNodeIndex = i;
                }
            }
            if(openList.size()==0){
                break;
            }

            currentNode=openList.get(bestNodeIndex);
            if(currentNode == goalNode){
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Metodă pentru deschiderea unui nod
    public void openNode(Node node){
        if(node.open == false && node.checked == false && node.solid == false){
            node.open=true;
            node.parent=currentNode;
            openList.add(node);
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Metodă pentru urmărirea căii de la nodul destinație înapoi la nodul de start și construirea listei de noduri care
    // formează calea
    public void trackThePath(){
        Node current = goalNode;
        while(current != startNode){
            pathList.add(0,current);
            current=current.parent;
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
