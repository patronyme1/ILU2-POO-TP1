package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() throws VillageSansChefException {
		if (chef == null) {
			throw new VillageSansChefException("Le village n'a pas de chef.");
		}
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder sb = new StringBuilder();
		sb.append(vendeur.getNom()).append(" cherche un endroit pour vendre ")
		  .append(nbProduit).append(" ").append(produit).append(".\n");
		int indice = marche.trouverEtalLibre();
		if (indice == -1) {
			sb.append("Il n'y a plus d'étal disponible au marché.\n");
		}
		else {
			marche.utiliserEtal(indice, vendeur, produit, nbProduit);
			sb.append("Le vendeur ").append(vendeur.getNom()).append(" vend des ")
				.append(produit).append(" à l'étal n°").append(indice+1).append(".\n");
		}
		return sb.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		Etal[] etals = marche.trouverEtals(produit);
		StringBuilder sb = new StringBuilder();

		if (etals.length == 0) {
			sb.append("Il n'y a pas de vendeur qui propose des ")
			  .append(produit).append(" au marché.\n");
		} else if (etals.length == 1) {
			sb.append("Seul le vendeur ")
			  .append(etals[0].getVendeur().getNom())
			  .append(" propose des ").append(produit).append(" au marché.\n");
		} else {
			sb.append("Les vendeurs qui proposent des ").append(produit)
			  .append(" sont : \n");
			for (Etal etal : etals) {
				sb.append("- ").append(etal.getVendeur().getNom()).append("\n");
			}
		}
		return sb.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}

	public String partirVendeur(Gaulois vendeur) {
		Etal etal = marche.trouverVendeur(vendeur);
		if (etal == null) {
			return vendeur.getNom() + " n'est pas installé à un étal.\n";
		}
		return etal.libererEtal();
	}
	
	public String afficherMarche() {
		return "Le marché du village \"" + nom + "\" possède plusieurs étals :\n" 
				+ marche.afficherMarche();
	}
	
	private class Marche{
		private Etal[] etals;
		
		public Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i< nbEtals; i++) {
				etals[i] = new Etal();
			}
		}
		
		public void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		
		public int trouverEtalLibre() {
			for (int i =0; i < etals.length; i++) {
				if (!etals[i].isEtalOccupe()){
					return i;
				}
			}
			return -1;
		}
		
		public Etal[] trouverEtals(String produit) {
			int compteur = 0;
			for (int i =0; i < etals.length; i++) {
				if (etals[i].contientProduit(produit)) {
					compteur++;
				}
			}
			
			Etal[] fin = new Etal[compteur];
			int index = 0;
			for (int i=0; i< etals.length; i++) {
				if (etals[i].contientProduit(produit)) {
					fin[index] = etals[i];
					index++;
				}
			}
			return fin;
		}
		
		public Etal trouverVendeur(Gaulois gaulois) {
			for (int i=0; i< etals.length; i++) {
				if (etals[i].isEtalOccupe() && etals[i].getVendeur() == gaulois) {
					return etals[i];
				}
			}
			return null;
		}
		
		
		public String afficherMarche() {
			StringBuilder sb = new StringBuilder();
			int nbEtalVide = 0;
			for (int i =0 ; i< etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					sb.append(etals[i].afficherEtal());
				}
				else {
					nbEtalVide++;
				}
			}
			if (nbEtalVide > 0) {
				sb.append("Il reste ").append(nbEtalVide).append(" étals non utilisés dans le marché.\n");
			}
			return sb.toString();
		}
	}
}