import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PoliticaEmprestimoTest {

    @Test
    public void deveAplicarRegrasDaPoliticaAluno() {
        PoliticaEmprestimo politica = new PoliticaAluno();

        assertEquals(3, politica.obterLimiteEmprestimo());
        assertEquals(2, politica.obterMaxRenovacoes());
        assertEquals(7, politica.obterPrazoMaximo());
        assertEquals(0.0, politica.calcularMulta(0));
        assertEquals(0.0, politica.calcularMulta(-2));
        assertEquals(10.0, politica.calcularMulta(5));
    }

    @Test
    public void deveAplicarRegrasDaPoliticaProfessor() {
        PoliticaEmprestimo politica = new PoliticaProfessor();

        assertEquals(10, politica.obterLimiteEmprestimo());
        assertEquals(5, politica.obterMaxRenovacoes());
        assertEquals(30, politica.obterPrazoMaximo());
        assertEquals(6.0, politica.calcularMulta(4));
    }

    @Test
    public void deveAplicarRegrasDaPoliticaFuncionario() {
        PoliticaEmprestimo politica = new PoliticaFuncionario();

        assertEquals(5, politica.obterLimiteEmprestimo());
        assertEquals(3, politica.obterMaxRenovacoes());
        assertEquals(14, politica.obterPrazoMaximo());
        assertEquals(6.0, politica.calcularMulta(3));
    }
}