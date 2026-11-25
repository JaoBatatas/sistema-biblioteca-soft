import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cadastro implements Observador {
    private int id;
    private String nome;
    private Date nascimento;
    private boolean ativo;
    private String email;
    private String senhaHash;
    private String numeroTelefone;
    private int papel;
    private PoliticaEmprestimo politica;

    private List<String> notificacoesRecebidas = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();

    public Cadastro(int id, String nome, Date nascimento, boolean ativo, String email,
                    String senhaHash, String numeroTelefone, int papel, PoliticaEmprestimo politica) {
        this.id = id;
        this.nome = nome;
        this.nascimento = nascimento;
        this.ativo = ativo;
        this.email = email;
        this.senhaHash = senhaHash;
        this.numeroTelefone = numeroTelefone;
        this.papel = papel;
        this.politica = politica;
    }

    @Override
    public void atualizar(String mensagem) {
        notificacoesRecebidas.add(mensagem);
    }

    public boolean possuiPendencia() {
        for (Emprestimo emp : emprestimos) {
            if (emp.getStatus() != StatusEmprestimo.DEVOLVIDO && (emp.getStatus() == StatusEmprestimo.ATRASADO || emp.calcularAtraso() > 0)) {
                return true;
            }
            if (emp.getMulta() != null && "PENDENTE".equalsIgnoreCase(emp.getMulta().getStatus())) {
                return true;
            }
        }
        return false;
    }

    public void adicionarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo != null) {
            emprestimos.add(emprestimo);
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public int getPapel() {
        return papel;
    }

    public PoliticaEmprestimo getPolitica() {
        return politica;
    }

    public void setPolitica(PoliticaEmprestimo politica) {
        this.politica = politica;
    }

    public List<String> getNotificacoesRecebidas() {
        return notificacoesRecebidas;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
}