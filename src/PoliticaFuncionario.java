public class PoliticaFuncionario implements PoliticaEmprestimo {
    private double valorMultaPorDia;

    public PoliticaFuncionario() {
        this.valorMultaPorDia = 2.00;
    }

    public PoliticaFuncionario(double valorMultaPorDia) {
        this.valorMultaPorDia = valorMultaPorDia;
    }

    @Override
    public double calcularMulta(int diasAtraso) {
        return diasAtraso > 0 ? diasAtraso * valorMultaPorDia : 0.0;
    }

    @Override
    public int obterLimiteEmprestimo() {
        return 5;
    }

    @Override
    public int obterMaxRenovacoes() {
        return 3;
    }

    @Override
    public int obterPrazoMaximo() {
        return 14;
    }
}