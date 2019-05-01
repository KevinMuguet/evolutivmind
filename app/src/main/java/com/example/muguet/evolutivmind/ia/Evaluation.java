package com.example.muguet.evolutivmind.ia;

//C'est  une classe qui permet d'évaluer les règles, avec plusieurs méthodes qui ont pour objectif d'évaluer différents aspects de la règle.
// en fonction du résultat il faudra soit augmenter ou diminuer le poids de celle-ci.
//Quand tu joues l'action d'une régle il faut  voir si elle a été bonne ou pas pour telle situation

import java.util.ArrayList;
import java.util.List;

public class Evaluation{

    public int id;
    public int idRegle; // L'id unique de la regle
    public int idJeu; // L'id du jeu qui va permettre d'identifier les jeux disponibles dans l'application
    public String regleJouee; // La règle à évaluer
    public Boolean gagnee; // résultat de la partie (gagné ou perdu)
    public float timeRestant; // Le temps restant au jeu avant la fin de la partie
    List<String> regleJouees = new ArrayList<String>();//La liste de toutes les règles

    /* evalue la totalité des regles jouées dans la partie en fonction du resultat de la partie : gagné ou perdu*/

    public  void evaluerFin(ArrayList<Regle> reglesJouees, boolean gagnee)
    {

        for (String regle : regleJouees) {
            int influence;
            if(gagnee){
                influence =1;
            }
            else {
                influence = -1;
            }
            // regle.poids += influence; a voir avec philippe comment faire la liaison avec le poids
        }
    }

    /*cette méthode diminue le poids si le joueur fini la partie avant la fin de cette derniere*/


    public void evaluerTemps(){

    }


}