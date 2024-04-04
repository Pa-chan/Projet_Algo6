package Modele;

public class Utilitaire {

    // Fonction pour convertir un niveau de Sokoban en un graphe
    public static Graph convertToGraph(Niveau niveau) {
        Graph graph = new Graph();

        // Parcours de toutes les cases du niveau
        for (int i = 0; i < niveau.lignes(); i++) {
            for (int j = 0; j < niveau.colonnes(); j++) {
                if (!niveau.aMur(i, j) && !niveau.aCaisse(i,j)) {
                    Node node = new Node(i, j);
                    graph.addNode(node);

                    // Vérification des déplacements possibles (haut, bas, gauche, droite)
                    int[] dLig = {-1, 1, 0, 0};
                    int[] dCol = {0, 0, -1, 1};
                    for (int k = 0; k < 4; k++) {
                        int newL = i + dLig[k];
                        int newC = j + dCol[k];
                        if (newL >= 0 && newL < niveau.lignes() && newC >= 0 && newC < niveau.colonnes()) {
                            if (niveau.estVide(newL, newC) || niveau.aBut(newL, newC)) {
                                Node neighborNode = new Node(newL, newC);
                                // Ajout d'une arête entre le nœud actuel et son voisin s'il est vide ou un but
                                graph.addEdge(node, neighborNode, 1); // Poids de l'arête mis à 1
                            }
                        }
                    }
                }
            }
        }

        return graph;
    }

    public static Graph convertToGraphCaisse(Niveau niveau) {
        Graph graph = new Graph();
    
        // Parcours de toutes les cases du niveau
        for (int i = 0; i < niveau.lignes(); i++) {
            for (int j = 0; j < niveau.colonnes(); j++) {
                if (!niveau.aMur(i, j)) {
                    Node node = new Node(i, j);
                    graph.addNode(node);
    
                    // Vérification des déplacements possibles (haut, bas, gauche, droite)
                    int[] dLig = {-1, 1, 0, 0};
                    int[] dCol = {0, 0, -1, 1};
                    for (int k = 0; k < 4; k++) {
                        int newL = i + dLig[k];
                        int newC = j + dCol[k];
                        if (newL >= 0 && newL < niveau.lignes() && newC >= 0 && newC < niveau.colonnes()) {
                            if (niveau.estVide(newL, newC) || niveau.aBut(newL, newC) || niveau.aCaisse(newL, newC)) {
                                Node neighborNode = new Node(newL, newC);
                                // Ajout d'une arête entre le nœud actuel et son voisin s'il est vide, un but ou une caisse
                                graph.addEdge(node, neighborNode, 1); // Poids de l'arête mis à 1
                            }
                        }
                    }
                }
            }
        }
    
        return graph;
    }
}