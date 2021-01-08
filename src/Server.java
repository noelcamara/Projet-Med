import java.util.ArrayList;
import java.util.Random;

public class Server {
    ArrayList<Double> arrives = new ArrayList<>();
    ArrayList<Double> sorties = new ArrayList<>();
    ArrayList<Double> traitements = new ArrayList<>();
    ArrayList<Double> envoyerAuCoordinateur = new ArrayList<>();
    int i = 0;

    public void traitement(double mu, double p, double valeur) {
        Random r = new Random();
        double tempsTraitement = DistributedDB.findX(mu);
        arrives.add(valeur);
        traitements.add(arrives.get(i) + tempsTraitement);
        if (p == 0.0) {
            sorties.add(arrives.get(i) + tempsTraitement);
        } else if (p == 1.0) {
            envoyerAuCoordinateur.add(arrives.get(i) + tempsTraitement);
        } else {
            if (r.nextDouble() <= p)
                envoyerAuCoordinateur.add(arrives.get(i) + tempsTraitement);
            else
                sorties.add(arrives.get(i) + tempsTraitement);
        }
        i++;
    }
}
