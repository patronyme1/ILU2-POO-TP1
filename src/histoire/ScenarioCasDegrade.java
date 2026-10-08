package histoire;

import personnages.Gaulois;
import villagegaulois.Etal;
import villagegaulois.VillageSansChefException;

public class ScenarioCasDegrade {

    public static void main(String[] args) {
        Etal etal = new Etal();
        Gaulois obelix = new Gaulois("Obélix", 25);

        etal.libererEtal();

        etal.occuperEtal(obelix, "menhirs", 5);
        System.out.println(etal.acheterProduit(2, null));

        try {
            etal.acheterProduit(-1, obelix);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Etal etalVide = new Etal();
        try {
            etalVide.acheterProduit(2, obelix);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        
     

        System.out.println("Fin du test");
    }
}