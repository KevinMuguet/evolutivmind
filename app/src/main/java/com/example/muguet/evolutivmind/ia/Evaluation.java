package com.example.muguet.evolutivmind.ia;

public class Evaluation{

    /**
     * Évaluation d'une règle de Colorwords et mise à jour du poids
     * @param regle
     * @param victoire
     * @param variante
     * @param tempsRestant
     */
    public static void evaluerColorwords(Regle regle, boolean victoire, int variante, long tempsRestant)
    {
        if(victoire == true){
            if(variante == 2){
                if(regle.getAction() == "Augmentation du temps consacré au timer"){
                    if(tempsRestant < 5){
                        regle.setPoids(regle.getPoids()-1);
                    }else{
                        regle.setPoids(regle.getPoids()-2);
                    }
                }
                if(regle.getAction() == "Diminution du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()+1);
                }
                if(regle.getAction() == "Réduire le temps d'exposition du mot"){
                    regle.setPoids(regle.getPoids()+1);
                }
            }else{
                if(regle.getAction() == "Augmentation du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()-1);
                }
                if(regle.getAction() == "Diminution du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()+2);
                }
                if(regle.getAction() == "Réduire le temps d'exposition du mot"){
                    regle.setPoids(regle.getPoids()+2);
                }
            }
        }else{
            if(variante == 2){
                if(regle.getAction() == "Augmentation du temps consacré au timer"){
                    if(tempsRestant < 5){
                        regle.setPoids(regle.getPoids()+2);
                    }else{
                        regle.setPoids(regle.getPoids()+1);
                    }
                }
                if(regle.getAction() == "Diminution du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()-1);
                }
                if(regle.getAction() == "Réduire le temps d'exposition du mot"){
                    regle.setPoids(regle.getPoids()-1);
                }
            }else{
                if(regle.getAction() == "Augmentation du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()+1);
                }
                if(regle.getAction() == "Diminution du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()-2);
                }
                if(regle.getAction() == "Réduire le temps d'exposition du mot"){
                    regle.setPoids(regle.getPoids()-2);
                }
            }
        }
    }

    /**
     * Évaluation d'une règle de Memorize et mise à jour du poids
     * @param regle
     * @param victoire
     * @param variante
     * @param tempsRestant
     */
    public static void evaluerMemorize(Regle regle, boolean victoire, int variante, long tempsRestant)
    {
        if(victoire == true){
            if(variante == 2){
                if(regle.getAction() == "Augmentation du temps consacré au timer"){
                    if(tempsRestant < 5){
                        regle.setPoids(regle.getPoids()-1);
                    }else{
                        regle.setPoids(regle.getPoids()-2);
                    }
                }
                if(regle.getAction() == "Diminution du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()+1);
                }
                if(regle.getAction() == "Réduire le temps d'exposition de la forme"){
                    regle.setPoids(regle.getPoids()+1);
                }
            }else{
                if(regle.getAction() == "Augmentation du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()-1);
                }
                if(regle.getAction() == "Diminution du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()+2);
                }
                if(regle.getAction() == "Réduire le temps d'exposition de la forme"){
                    regle.setPoids(regle.getPoids()+2);
                }
            }
        }else{
            if(variante == 2){
                if(regle.getAction() == "Augmentation du temps consacré au timer"){
                    if(tempsRestant < 5){
                        regle.setPoids(regle.getPoids()+2);
                    }else{
                        regle.setPoids(regle.getPoids()+1);
                    }
                }
                if(regle.getAction() == "Diminution du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()-1);
                }
                if(regle.getAction() == "Réduire le temps d'exposition de la forme"){
                    regle.setPoids(regle.getPoids()-1);
                }
            }else{
                if(regle.getAction() == "Augmentation du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()+1);
                }
                if(regle.getAction() == "Diminution du temps consacré au timer"){
                    regle.setPoids(regle.getPoids()-2);
                }
                if(regle.getAction() == "Réduire le temps d'exposition de la forme"){
                    regle.setPoids(regle.getPoids()-2);
                }
            }
        }
    }
}