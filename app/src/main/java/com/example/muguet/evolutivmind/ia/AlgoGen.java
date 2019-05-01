package com.example.muguet.evolutivmind.ia;

import java.util.Random;

public class AlgoGen {

    public static String mutationAction(String action){
        String newAction = "";
        Random random = new Random();
        int res = random.nextInt(3);
        if(res == 0){
            newAction = "Augmentation du temps consacré au timer";
        }
        if (res == 1){
            newAction = "Diminution du temps consacré au timer";
        }
        if (res == 2){
            newAction = "Modification du mot et de la couleur avec laquelle il est écrit en cours de jeu";
        }
        if (res == 3){
            newAction = "Réduire le temps d'exposition du mot";
        }
        return newAction;
    }

    public static Regle mutationRegle(Regle regle){
        Random random = new Random();
        int res = random.nextInt(4);
        if(res == 0){ //userId
        }
        if (res == 1){ //idJeu
        }
        if (res == 2){ //tempsRestant

        }
        if (res == 3){ //varianteJeu
            if(regle.getVarianteJeu() == 1){
                regle.setVarianteJeu(2);
            }else{
                regle.setVarianteJeu(1);
            }
        }
        if (res == 4){ //resPartiePrecedente
            if(regle.isResPartiePrecedente()) {
                regle.setResPartiePrecedente(false);
            }else{
                regle.setResPartiePrecedente(true);
            }
        }
        return regle;
    }

}
