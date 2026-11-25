public class PoliticaProfessor implements PoliticaEmprestimo {
    private double valorMultaPorDia;

    public PoliticaProfessor() {
        this.valorMultaPorDia = 1.50;
    }

    public PoliticaProfessor(double valorMultaPorDia) {
        this.valorMultaPorDia = valorMultaPorDia;
    }

    @Override
    public double calcularMulta(int diasAtraso) {
        return diasAtraso > 0 ? diasAtraso * valorMultaPorDia : 0.0;
    }

    @Override
    public int obterLimiteEmprestimo() {
        return 10;
    }

    @Override
    public int obterMaxRenovacoes() {
        return 5;
    }

    @Override
    public int obterPrazoMaximo() {
        return 30;
    }
}