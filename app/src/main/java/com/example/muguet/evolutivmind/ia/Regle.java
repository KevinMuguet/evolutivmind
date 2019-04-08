package com.example.muguet.evolutivmind.ia;

public class Regle {

    private int idJoueur; // L'id unique du joueur qui va permettre d'identifier les joueurs
    private int idJeu;    // L'id unique du jeu qui va permettre d'identifier les jeux disponibles dans l'application
    private float timeRestant; // Le temps restant au jeu avant la fin de la partie
    private int varianteJeu;
    private boolean resPartiePrecedente; // Victoire ou Défaite du joueur sur la partie précédente afin de réguler au mieux
    private int poids; // Cet attribut permet de mesurer la pertinence de l'association action et situation
    private String action; // La conséquence de l'action faite par l'utilisateur

    // Partie Enum
        // COLORWORDS
        public static String param1 = "Augmentation du temps consacré au timer";
        public static String param2 = "Diminution du temps consacré au timer";
        public static String param3 = "Modification du mot et de la couleur avec laquelle il est écrit en cours de jeu";
        public static String param5 = "Réduire le temps d'exposition du mot";
        public static String param6 = "Augmenter le temps d'exposition du mot";

        // MEMORIZE
        public static String param7 = "Réduire le temps d'exposition de l'image/forme";
        public static String param8 = "Augmenter le temps d'exposition de l'image/forme";
        public static String param9 = "Augmenter le nombre de réponse possible";
        public static String param10 = "Réduire le nombre de réponse possible";
        public static String param11 = "Rajouter l'option Aucune de celle présentées";
}
