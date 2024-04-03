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
import java.util.Random;
class IAAleatoire extends IA {
	Random r;
	// Couleurs au format RGB (rouge, vert, bleu, un octet par couleur)
	final static int VERT = 0x00CC00;
	final static int MARRON = 0xBB7755;

	public IAAleatoire() {
		r = new Random();
	}
 
	@Override
    public Sequence<Coup> joue() {
        Sequence<Coup> resultat = Configuration.nouvelleSequence();

        // Trouver la position de la caisse
        int caisseL = -1;
        int caisseC = -1;
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

        // Trouver la position actuelle du joueur
        int joueurL = niveau.lignePousseur();
        int joueurC = niveau.colonnePousseur();
		Graph niv =new Graph();
		niv =Utilitaire.convertToGraph(niveau);
        // Calculer le chemin le plus court entre le joueur et la caisse en utilisant Dijkstra
        List<Node> chemin = Dijkstra.shortestPath(niv, new Node(joueurL, joueurC), new Node(caisseL-1, caisseC-1));
		
        // Déplacer le joueur vers la position adjacente à la caisse selon le chemin calculé
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

        return resultat;
    }

	
	 
 }
 
