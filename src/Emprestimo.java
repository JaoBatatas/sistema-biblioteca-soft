import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Emprestimo {
    private int id;
    private Date dataEmprestimo;
    private Date dataVencimento;
    private Date dataDevolucao;
    private StatusEmprestimo status;
    private int quantidadeRenovacoes;

    private Cadastro cadastro;
    private Exemplar exemplar;
    private Multa multa;

    public Emprestimo(int id, Date dataEmprestimo, Date dataVencimento, Cadastro cadastro, Exemplar exemplar) {
        this.id = id;
        this.dataEmprestimo = dataEmprestimo;
        this.dataVencimento = dataVencimento;
        this.cadastro = cadastro;
        this.exemplar = exemplar;
        this.status = StatusEmprestimo.ATIVO;
        this.quantidadeRenovacoes = 0;
    }

    public int calcularAtraso() {
        if (status == StatusEmprestimo.DEVOLVIDO && dataDevolucao == null) {
            return 0;
        }
        Date referencia = (dataDevolucao != null) ? dataDevolucao : new Date();
        if (referencia.after(dataVencimento)) {
            long diffMillis = referencia.getTime() - dataVencimento.getTime();
            return (int) TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
        }
        return 0;
    }

    public boolean renovar(PoliticaEmprestimo politica) {
        if (politica == null || status != StatusEmprestimo.ATIVO) {
            return false;
        }
        if (calcularAtraso() > 0 || quantidadeRenovacoes >= politica.obterMaxRenovacoes()) {
            return false;
        }
        long prazoMillis = TimeUnit.DAYS.toMillis(politica.obterPrazoMaximo());
        this.dataVencimento = new Date(this.dataVencimento.getTime() + prazoMillis);
        this.quantidadeRenovacoes++;
        return true;
    }

    public int getId() {
        return id;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    public int getQuantidadeRenovacoes() {
        return quantidadeRenovacoes;
    }

    public Cadastro getCadastro() {
        return cadastro;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public Multa getMulta() {
        return multa;
    }

    public void setMulta(Multa multa) {
        this.multa = multa;
    }
}