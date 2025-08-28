import java.util.*;
import java.io.*;

public class MysticJourneyAlpha {

    static Scanner sc = new Scanner(System.in);
    static Random rnd = new Random();
    static String fileSalvataggio = "salvataggio.txt";
    static boolean inglese = false;

    public static void main(String[] args) {
        while (true) {
            if (!inglese) {
                System.out.println("\nMenu:");
                System.out.println("1. Lingua");
                System.out.println("2. RIPRENDI");
                System.out.println("3. NUOVA PARTITA");
                System.out.println("4. ESCI");
                System.out.print("Scelta: ");
            } else {
                System.out.println("\nMenu:");
                System.out.println("1. Language");
                System.out.println("2. RESUME");
                System.out.println("3. NEW GAME");
                System.out.println("4. EXIT");
                System.out.print("Choice: ");
            }

            String scelta = sc.nextLine().trim();
            switch (scelta) {
                case "1":
                    inglese = !inglese;
                    break;
                case "2":
                    if (new File(fileSalvataggio).exists()) riprendiPartita();
                    else nuovaPartita();
                    break;
                case "3":
                    nuovaPartita();
                    break;
                case "4":
                    return;
                default:
                    System.out.println(inglese ? "Invalid choice." : "Scelta non valida.");
            }
        }
    }

    static void nuovaPartita() {
        int punti = 0;
        String nome;
        List<String> inventario = new ArrayList<>();
        Set<String> scelteChiave = new HashSet<>();

        System.out.print(inglese ? "\nEnter your name: " : "\nInserisci il tuo nome: ");
        nome = sc.nextLine().trim();

        ArrayList<String[]> scelte = creaScelte();
        Collections.shuffle(scelte);

        for (String[] domanda : scelte) {
            punti = sceltaAvanzata(domanda, punti, inventario, nome, scelteChiave);
        }

        finePartita(nome, punti, inventario, scelteChiave);
    }

    static int sceltaAvanzata(String[] domanda, int punti, List<String> inventario, String nome, Set<String> scelteChiave) {
        System.out.println("\n" + (inglese ? domanda[3] : domanda[0]));
        System.out.println(inglese ? "(Press '<' + Enter to save)" : "(Premi '<' + Invio per salvare)");

        String risposta = sc.nextLine().trim().toLowerCase();

        if (risposta.equals("<")) {
            salvaPartita(nome, punti, inventario, scelteChiave);
            return sceltaAvanzata(domanda, punti, inventario, nome, scelteChiave);
        }

        boolean valida = false;
        for (int j = 1; j <= 2; j++) {
            String[] op = domanda[j].split(":");
            if (risposta.startsWith(op[0])) {
                int puntiEffetto = Integer.parseInt(op[1]);
                punti += puntiEffetto;
                valida = true;
                if (op.length > 2 && op[2].equals("chiave")) scelteChiave.add(domanda[0]);
                System.out.println((inglese ? "You " : "Hai ") + (puntiEffetto > 0 ? "gained " : "lost ") + Math.abs(puntiEffetto) + (inglese ? " points: " : " punti per la scelta ") + op[0]);
                break;
            }
        }

        if (!valida) System.out.println(inglese ? "0 points, no effect." : "0 punti, nessun effetto.");

        System.out.println((inglese ? "Current points: " : "Punti attuali: ") + punti);

        if (rnd.nextInt(100) < 25) {
            String[] bonusEventi = inglese ? new String[]{"Magic Gem", "Strength Potion", "Arcane Shield"} 
                                           : new String[]{"Gemma Magica", "Pozione della Forza", "Scudo Arcano"};
            String bonus = bonusEventi[rnd.nextInt(bonusEventi.length)];
            inventario.add(bonus);
            int puntiBonus = 5 + rnd.nextInt(6);
            punti += puntiBonus;
            System.out.println((inglese ? "You found a bonus: " : "Hai trovato un bonus: ") + bonus + " (+ " + puntiBonus + ")");
            System.out.println((inglese ? "Current points: " : "Punti attuali: ") + punti);
        }

        return punti;
    }

    static ArrayList<String[]> creaScelte() {
        ArrayList<String[]> scelte = new ArrayList<>();
        scelte.add(new String[]{"Sei davanti a due porte: rossa o blu?", "r:+10:chiave", "b:-5", "You face two doors: red or blue?"});
        scelte.add(new String[]{"Trovi un drago addormentato: lo aggiri o svegli?", "a:+5", "s:-7", "You find a sleeping dragon: avoid or wake?"});
        scelte.add(new String[]{"Un mercante ti offre un oggetto magico: accetti o rifiuti?", "a:+8", "r:0", "A merchant offers a magical item: accept or refuse?"});
        scelte.add(new String[]{"Un ponte scricchiola: attraversi o torni indietro?", "a:+4", "t:-3", "A bridge creaks: cross or turn back?"});
        scelte.add(new String[]{"Vedi un lago incantato: lo attraversi o torni indietro?", "a:+6", "t:-2", "You see an enchanted lake: cross or turn back?"});
        scelte.add(new String[]{"Incontra un fantasma: parli o corri?", "p:+5", "c:-4", "You meet a ghost: talk or run?"});
        scelte.add(new String[]{"Un troll ti sfida a un indovinello: rispondi o scappi?", "r:+7", "s:-3", "A troll challenges you with a riddle: answer or flee?"});
        scelte.add(new String[]{"Trovi un forziere chiuso: lo apri o lo lasci?", "a:+8", "l:0", "You find a locked chest: open or leave?"});
        scelte.add(new String[]{"Un cavallo selvaggio ti passa davanti: lo cavalchi o lo lasci?", "c:+6", "l:0", "A wild horse passes by: ride or leave?"});
        scelte.add(new String[]{"Un drago vola sopra di te: combatti o fuggi?", "c:+7", "f:-3", "A dragon flies over you: fight or flee?"});
        scelte.add(new String[]{"Un arcobaleno appare: lo segui o ignori?", "s:+4", "i:0", "A rainbow appears: follow or ignore?"});
        scelte.add(new String[]{"Vedi una grotta misteriosa: entri o passi oltre?", "e:+6", "p:0", "You see a mysterious cave: enter or pass?"});
        scelte.add(new String[]{"Un vecchio ti offre un tesoro: accetti o rifiuti?", "a:+10", "r:0", "An old man offers you a treasure: accept or refuse?"});
        scelte.add(new String[]{"Un ponte sospeso scricchiola: attraversi o torni indietro?", "a:+5", "t:-2", "A hanging bridge creaks: cross or turn back?"});
        scelte.add(new String[]{"Un mercante ti sfida a comprare un oggetto raro: compri o rifiuti?", "c:+8", "r:0", "A merchant challenges you to buy a rare item: buy or refuse?"});
        scelte.add(new String[]{"Un elfo ti invita a seguirlo: segui o ignori?", "s:+5", "i:0", "An elf invites you: follow or ignore?"});
        scelte.add(new String[]{"Una pozione appare davanti a te: bevi o ignori?", "b:+7", "i:0", "A potion appears: drink or ignore?"});
        scelte.add(new String[]{"Un fantasma ti propone un enigma: rispondi o fuggi?", "r:+6", "f:-3", "A ghost gives you a puzzle: answer or flee?"});
        scelte.add(new String[]{"Vedi un frutto magico: lo mangi o ignori?", "m:+3", "i:0", "You see a magical fruit: eat or ignore?"});
        scelte.add(new String[]{"Un drago addormentato ti blocca: aggiri o svegli?", "a:+5", "s:-5", "A sleeping dragon blocks you: avoid or wake?"});
        return scelte;
    }

    static void salvaPartita(String nome, int punti, List<String> inventario, Set<String> scelteChiave) {
        try (PrintWriter pw = new PrintWriter(fileSalvataggio)) {
            pw.println(nome);
            pw.println(punti);
            pw.println(String.join(",", inventario));
            pw.println(String.join(";", scelteChiave));
        } catch (IOException e) {}
    }

    static void riprendiPartita() {
        try (Scanner file = new Scanner(new File(fileSalvataggio))) {
            String nome = file.nextLine();
            int punti = Integer.parseInt(file.nextLine());
            List<String> inventario = new ArrayList<>();
            Set<String> scelteChiave = new HashSet<>();
            if (file.hasNextLine()) inventario.addAll(Arrays.asList(file.nextLine().split(",")));
            if (file.hasNextLine()) scelteChiave.addAll(Arrays.asList(file.nextLine().split(";")));

            ArrayList<String[]> scelte = creaScelte();
            Collections.shuffle(scelte);

            for (String[] domanda : scelte) {
                punti = sceltaAvanzata(domanda, punti, inventario, nome, scelteChiave);
            }

            finePartita(nome, punti, inventario, scelteChiave);

        } catch (IOException e) {}
    }

    static void finePartita(String nome, int punti, List<String> inventario, Set<String> scelteChiave) {
        if (punti >= 80) System.out.println(inglese ? "Hero Ending!" : "Finale Eroe!");
        else if (inventario.contains(inglese ? "Magic Gem" : "Gemma Magica")) System.out.println(inglese ? "Arcane Ending!" : "Finale Arcano!");
        else if (punti < 30) System.out.println(inglese ? "Unlucky Ending!" : "Finale Sfortunato!");
        else System.out.println(inglese ? "Normal Ending!" : "Finale Normale!");

        System.out.println((inglese ? "Total points: " : "Punteggio totale: ") + punti);
        System.out.println((inglese ? "Final inventory: " : "Inventario finale: ") + inventario);
        System.out.println((inglese ? "Key choices made: " : "Scelte chiave effettuate: ") + scelteChiave);

        salvaPartita(nome, punti, inventario, scelteChiave);
    }
}
