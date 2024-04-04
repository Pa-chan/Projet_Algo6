package Modele;
/*
 * Sokoban - Encore une nouvelle version (à but pédagogique) du célèbre jeu
 * Copyright (C) 2018 Guillaume Huard
 *
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 *
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 *
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */



import Global.Configuration;
import Structures.Sequence;
//import Modele.Graph;
//import Modele.Dijkstra;
import java.util.List;
class IAAleatoire extends IA {
	List<Node> cheminCaisse;
    int avance;
    int row;
    int col;
    int caisseL;
    int caisseC;
    int ButC;
    int ButL;
    boolean bool;
    Niveau niveau;
    int ArriveL;
    int ArriveC;

    
	public IAAleatoire(Niveau niv) {
        caisseL = -1;
        caisseC = -1;
        niveau=niv;
        for (int i = 0; i < niveau.lignes(); i++) {
            for (int j = 0; j < niveau.colonnes(); j++) {
                if (niveau.aCaisse(i, j)) {
                    caisseL = i;
                    caisseC = j;
                    break;
                }
            }
            if (caisseL != -1) {
                break;
            }
        }
        ButL = -1;
        ButC = -1;
        for (int i = 0; i < niveau.lignes(); i++) {
            for (int j = 0; j < niveau.colonnes(); j++) {
                if (niveau.aBut(i, j)) {
                    ButL = i;
                    ButC = j;
                    break;
                }
            }
            if (ButL != -1) {
                break;
            }
        }
		Graph niv1 =new Graph();
	    niv1 =Utilitaire.convertToGraphCaisse(niveau);
        cheminCaisse = Dijkstra.shortestPath(niv1, new Node(caisseL, caisseC), new Node(ButL, ButC));
        avance=1;
        bool=true;
        ArriveC=-1;
        ArriveL=-1;
	}
 
	@Override
    public Sequence<Coup> joue() {
        Sequence<Coup> resultat = Configuration.nouvelleSequence();
        
        if(niveau.pousseurC==ArriveC && niveau.pousseurL==ArriveL){
            bool=true;
            avance++;
            Coup coup=null;
            
            if(caisseL>row){
                coup = niveau.deplace(-1, 0);
                caisseL=caisseL-1;
            }
            else if (caisseL<row){
                coup = niveau.deplace(1,0 );
                caisseL=caisseL+1;
            }
            else if (caisseC>col){
                coup = niveau.deplace(0, -1);
                caisseC=caisseC-1;
            }
            else if (caisseC<col){
                coup = niveau.deplace(0, 1);
                caisseC=caisseC+1;
            }
            if (coup != null) {
                resultat.insereQueue(coup);
            }

        }
        if(bool){
            Node nextNode = cheminCaisse.get(avance);
            row=nextNode.row;
            col=nextNode.col;
            bool=false;
            if(caisseL>row){
                ArriveC=caisseC;
                ArriveL=caisseL+1;
            }
            else if (caisseL<row){
                ArriveC=caisseC;
                ArriveL=caisseL-1;
            }
            else if (caisseC>col){
                ArriveC=caisseC+1;
                ArriveL=caisseL;
            }
            else if (caisseC<col){
                ArriveC=caisseC-1;
                ArriveL=caisseL;
            }
        }
        if(!(niveau.pousseurC==ArriveC && niveau.pousseurL==ArriveL)){
        if(caisseL>row){
            DeplaceJoueur(niveau,caisseL+1, caisseC,resultat);
            
        }
        else if (caisseL<row){
            DeplaceJoueur(niveau,caisseL-1, caisseC,resultat);
            
        }
        else if (caisseC>col){
            DeplaceJoueur(niveau,caisseL, caisseC+1,resultat);
           
        }
        else if (caisseC<col){
            DeplaceJoueur(niveau,caisseL, caisseC-1,resultat);
            
        }
    }
    if(avance==cheminCaisse.size()-1){
        Coup coup=null;
        if(caisseL>row){
            coup = niveau.deplace(-1, 0);
        
        }
        else if (caisseL<row){
            coup = niveau.deplace(1,0 );
            
        }
        else if (caisseC>col){
            coup = niveau.deplace(0, -1);
            
        }
        else if (caisseC<col){
            coup = niveau.deplace(0, 1);
            
        }
        if (coup != null) {
            resultat.insereQueue(coup);
        }
    }
        return resultat;
} 

private void DeplaceJoueur(Niveau niveau,int destL, int destC, Sequence<Coup> resultat){
    int joueurL = niveau.lignePousseur();
    int joueurC = niveau.colonnePousseur();
	Graph niv =new Graph();
	niv =Utilitaire.convertToGraph(niveau);
    List<Node> chemin = Dijkstra.shortestPath(niv, new Node(joueurL, joueurC), new Node(destL, destC));
    if (chemin.size() > 1) { // Assurez-vous qu'il y a un chemin valide trouvé
            // Trouver le prochain nœud dans le chemin (la position adjacente à la caisse)
            Node nextNode = chemin.get(1);
            int dLig = nextNode.row - joueurL;
            int dCol = nextNode.col - joueurC;

            // Déplacer le joueur
            Coup coup = niveau.deplace(dLig, dCol);

            // Ajouter le coup à la séquence de coups résultat
            if (coup != null) {
                resultat.insereQueue(coup);
            }
    }
}
}
 
