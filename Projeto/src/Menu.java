import java.io.IOException;
import java.util.*;


public class Menu {

    public interface Handler {
        void execute() throws IOException;
    }
    public interface PreCondition {
        boolean validate();
    }

    private static Scanner is = new Scanner(System.in);


    private String titulo;                  // Titulo do menu (opcional)
    private List<String> opcoes;            // Lista de opções
    private List<PreCondition> disponivel;  // Lista de pré-condições
    private List<Handler> handlers;         // Lista de handlers


    public Menu() {
        this.titulo = "Menu";
        this.opcoes = new ArrayList();
        this.disponivel = new ArrayList();
        this.handlers = new ArrayList();
    }


    public Menu(String titulo, List<String> opcoes) {
        this.titulo = titulo;
        this.opcoes = new ArrayList(opcoes);
        this.disponivel = new ArrayList();
        this.handlers = new ArrayList();
        this.opcoes.forEach(s-> {
            this.disponivel.add(()->true);
            this.handlers.add(()->System.out.println("\nATENÇÃO: Opção não implementada!"));
        });
    }

    public Menu(List<String> opcoes) { this("Menu", opcoes); }

    public Menu(String titulo, String[] opcoes) {
        this(titulo, Arrays.asList(opcoes));
    }


    public Menu(String[] opcoes) {
        this(Arrays.asList(opcoes));
    }

    public void option(String name, PreCondition p, Handler h) {
        this.opcoes.add(name);
        this.disponivel.add(p);
        this.handlers.add(h);
    }


    public void run() throws IOException {
        int op;
        do {
            show();
            op = readOption();
            // testar pré-condição
            if (op>0 && !this.disponivel.get(op-1).validate()) {
                System.out.println("Opção indisponível! Tente novamente.");
            } else if (op>0) {
                // executar handler
                this.handlers.get(op-1).execute();
            }
        } while (op != 0);
    }


    public void setPreCondition(int i, PreCondition b) {
        this.disponivel.set(i-1,b);
    }


    public void setHandler(int i, Handler h) {
        this.handlers.set(i-1, h);
    }


    private void show() {
        System.out.println("\n *** "+this.titulo+" *** ");
        for (int i=0; i<this.opcoes.size(); i++) {
            System.out.print(i+1);
            System.out.print(" - ");
            System.out.println(this.disponivel.get(i).validate()?this.opcoes.get(i):"---");
        }
        System.out.println("0 - Sair");
    }


    private int readOption() {
        int op;

        System.out.print("Opção: ");
        try {
            String line = is.nextLine();
            op = Integer.parseInt(line);
        }
        catch (NumberFormatException e) { // Não foi inscrito um int
            op = -1;
        }
        if (op<0 || op>this.opcoes.size()) {
            System.out.println("Opção Inválida!!!");
            op = -1;
        }
        return op;
    }
}
