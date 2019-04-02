package com.example.muguet.evolutivmind.ia;

public class Regle {

    private int idJoueur; // L'id unique du joueur qui va permettre d'identifier les joueurs
    private int idJeu;    // L'id unique du jeu qui va permettre d'identifier les jeux disponibles dans l'application
    private float timeRestant; // Le temps restant au jeu avant la fin de la partie
    private int varianteJeu;
    private boolean resPartiePrecedente; // Victoire ou Défaite du joueur sur la partie précédente afin de réguler au mieux
    private int poids; // Cet attribut permet de mesurer la pertinence de l'association action et situation
    private String action; // La conséquence de l'action faite par l'utilisateur

}
