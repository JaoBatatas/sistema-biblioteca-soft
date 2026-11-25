public class PoliticaAluno implements PoliticaEmprestimo {
    private double valorMultaPorDia;

    public PoliticaAluno() {
        this.valorMultaPorDia = 2.00;
    }

    public PoliticaAluno(double valorMultaPorDia) {
        this.valorMultaPorDia = valorMultaPorDia;
    }

    @Override
    public double calcularMulta(int diasAtraso) {
        return diasAtraso > 0 ? diasAtraso * valorMultaPorDia : 0.0;
    }

    @Override
    public int obterLimiteEmprestimo() {
        return 3;
    }

    @Override
    public int obterMaxRenovacoes() {
        return 2;
    }

    @Override
    public int obterPrazoMaximo() {
        return 7;
    }
}