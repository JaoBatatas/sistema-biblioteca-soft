public interface PoliticaEmprestimo {
    double calcularMulta(int diasAtraso);
    int obterLimiteEmprestimo();
    int obterMaxRenovacoes();
    int obterPrazoMaximo();
}