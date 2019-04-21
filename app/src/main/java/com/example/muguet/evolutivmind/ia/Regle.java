package com.example.muguet.evolutivmind.ia;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Random;

@Entity
public class Regle {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int idJoueur; // L'id unique du joueur qui va permettre d'identifier les joueurs
    public int idJeu;    // L'id unique du jeu qui va permettre d'identifier les jeux disponibles dans l'application
    public float timeRestant; // Le temps restant au jeu avant la fin de la partie
    public int varianteJeu;
    public boolean resPartiePrecedente; // Victoire ou Défaite du joueur sur la partie précédente afin de réguler au mieux
    public int poids; // Cet attribut permet de mesurer la pertinence de l'association action et situation

    public String getAction() {
        return action;
    }

    private String action; // La conséquence de l'action faite par l'utilisateur

    public Regle(int idJoueur, int idJeu, float timeRestant, int varianteJeu, boolean resPartiePrecedente, int poids, String action){
        this.idJoueur = idJoueur;
        this.idJeu = idJeu;
        this.timeRestant = timeRestant;
        this.varianteJeu = varianteJeu;
        this.resPartiePrecedente = resPartiePrecedente;
        this.poids = poids;
        this.action = action;
    }
        // COLORWORDS
        public static String param1 = "Augmentation du temps consacré au timer";
        public static String param2 = "Diminution du temps consacré au timer";
        public static String param3 = "Modification du mot et de la couleur avec laquelle il est écrit en cours de jeu";
        public static String param5 = "Réduire le temps d'exposition du mot";

        // MEMORIZE
        public static String param7 = "Réduire le temps d'exposition de l'image/forme";
        public static String param8 = "Augmenter le temps d'exposition de l'image/forme";
        public static String param9 = "Augmenter le nombre de réponse possible";
        public static String param10 = "Réduire le nombre de réponse possible";
        public static String param11 = "Rajouter l'option Aucune de celle présentées";

}
