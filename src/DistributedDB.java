import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DistributedDB {
    public static final double p = 0.01;
    public static int temps = 10000;
    public static int n = 1;
    ArrayList<Double> arrives = new ArrayList<>();
    ArrayList<Double> venantDesServeurs = new ArrayList<>();
    ArrayList<Double> sorties = new ArrayList<>();
    ArrayList<Double> traitements = new ArrayList<>();
    String saveArrive = "", saveSortie = "";

    public static double findX(double lambda) {
        double y = Math.random();
        return -((Math.log(1 - y) / lambda));
    }

    public static void main(String[] args) {
        DistributedDB rfa = new DistributedDB();
        rfa.simulation(1. / 100., 1. / 100.);
    }

    public void setSaveSortie(String saveSortie) {
        this.saveSortie = saveSortie;
    }

    public String getSaveArrive() {
        return saveArrive;
    }

    public void setSaveArrive(String saveArrive) {
        this.saveArrive = saveArrive;
    }

    public void simulation(double lambda, double mu) {
        Server[] servers = new Server[n];
        for (int k = 0; k < n; k++)
            servers[k] = createServer();
        if (arrives.isEmpty())
            arrives.add(0.0);

        // 1. Est-ce que le système converge
        if (lambda / mu < 1) {
            System.out.println("le système converge");
        } else
            System.out.println("Le système diverge");

        // 2, ... ,8. temps de traitement, nombre moyen de clients, etc ...
        int i = 0;
        double somme = 0;
        while (arrives.get(i) < temps) {
            arrives.add(findX(lambda) + arrives.get(i));
            for (int j = 0; j < n; j++) {
                if (!servers[j].envoyerAuCoordinateur.isEmpty()) {
                    arrives.add(servers[j].envoyerAuCoordinateur.get(0));
                    venantDesServeurs.add(servers[j].envoyerAuCoordinateur.get(0));
                }
                servers[j].envoyerAuCoordinateur.clear();
            }
            traitements.add(i, arrives.get(i));
            double tempsTraitement;
            tempsTraitement = findX(1. / 4);
            sorties.add(i, traitements.get(i) + tempsTraitement);
            if (i > 0) {
                if (sorties.get(i - 1) < arrives.get(i)) {
                    sorties.add(i, arrives.get(i) + tempsTraitement);
                } else {
                    sorties.add(i, sorties.get(i - 1) + tempsTraitement);
                }
            }
            int y = (int) (Math.random() * n);
            if (y % 2 == 0) {
                mu = 1. / 200.;
            } else mu = 1. / 100.;
            servers[y].arrives.add(sorties.get(i));
            servers[y].traitement(mu, p, sorties.get(i));
            somme += tempsTraitement;
            i++;
        }
        int sommeSorties = 0;
        double moyenne = somme / arrives.size();
        int n = arrives.size() - venantDesServeurs.size();
        System.out.println("Nombre de requetes arrivées dans le coordinateur: " + n);
        System.out.println("Nombre de requetes rebouclées: " + venantDesServeurs.size());

        for (int x = 0; x < servers.length; x++) {
            int nb = x + 1;
            System.out.println("Nombre de requetes traitées par le serveur " + nb + " " + servers[x].traitements.size());
            int reste = servers[x].traitements.size() - servers[x].sorties.size();
            System.out.println("Nombre de requetes restées dans le serveur " + nb + " " + reste);
            for (int u = 0; u < servers[x].traitements.size(); u++) {
                setSaveArrive(getSaveArrive() + "\n" + u + "  " + (servers[x].traitements.get(u)));
            }
            sommeSorties += servers[x].sorties.size();
            generateData("serveur" + x + ".dat");
            setSaveArrive("");
            setSaveSortie("");
        }
        System.out.println("le temps moyen de traitement des requetes " + moyenne);
        System.out.println("Nombre de requetes sorties.dat du système pendant la simulation " + sommeSorties);
    }

    public Server createServer() {
        return new Server();
    }

    public void generateData(String arrive) {
        try {
            PrintWriter printWriter = new PrintWriter(arrive, StandardCharsets.UTF_8);
            printWriter.write(getSaveArrive());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
