import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class AcervoFactoryTest {

    @Test
    public void deveCriarItensConcretosPelosFactoryMethods() {
        ItemAcervoFactory livroFactory = new LivroFactory();
        Map<String, Object> dadosLivro = new HashMap<>();
        dadosLivro.put("id", 1);
        dadosLivro.put("titulo", "Engenharia de Software Moderna");
        dadosLivro.put("codigo", "LIV-101");
        dadosLivro.put("autor", "Marco Tulio Valente");
        dadosLivro.put("categorias", new String[]{"Computação", "Engenharia de Software"});
        dadosLivro.put("nPaginas", 400);
        dadosLivro.put("idioma", "Português");

        ItemAcervo livro = livroFactory.criarItemAcervo(dadosLivro);
        assertTrue(livro instanceof Livro);
        assertEquals("Engenharia de Software Moderna", livro.getTitulo());
        assertEquals(400, ((Livro) livro).getNPaginas());

        ItemAcervoFactory midiaFactory = new MidiaFactory();
        Map<String, Object> dadosMidia = new HashMap<>();
        dadosMidia.put("id", 2);
        dadosMidia.put("titulo", "Documentário Turing");
        dadosMidia.put("codigo", "MID-202");
        dadosMidia.put("autor", "BBC");
        dadosMidia.put("duracao", 90);
        dadosMidia.put("idiomas", new String[]{"Inglês", "Português"});

        ItemAcervo midia = midiaFactory.criarItemAcervo(dadosMidia);
        assertTrue(midia instanceof Midia);
        assertEquals(90, ((Midia) midia).getDuracao());

        ItemAcervoFactory periodicoFactory = new PeriodicoFactory();
        Map<String, Object> dadosPeriodico = new HashMap<>();
        dadosPeriodico.put("id", 3);
        dadosPeriodico.put("titulo", "Revista SBC");
        dadosPeriodico.put("codigo", "PER-303");
        dadosPeriodico.put("autor", "SBC");
        dadosPeriodico.put("nPaginas", 60);
        dadosPeriodico.put("nEdicao", 12);
        dadosPeriodico.put("tituloEdicao", "Especial IA");

        ItemAcervo periodico = periodicoFactory.criarItemAcervo(dadosPeriodico);
        assertTrue(periodico instanceof Periodico);
        assertEquals(12, ((Periodico) periodico).getNEdicao());
    }

    @Test
    public void deveBuscarEGerarRelatoriosDoAcervo() {
        Acervo acervo = new Acervo();
        Livro livro = new Livro(1, "Clean Code", "CC-01", "Robert Martin", new String[]{"Programação"}, 464, "Inglês");
        Exemplar ex1 = livro.adicionarExemplar("CC-01-A");
        acervo.registrarItem(livro);

        assertEquals(1, acervo.buscaTitulo("Clean").length);
        assertEquals(livro, acervo.buscaAutor("Robert Martin"));
        assertEquals(livro, acervo.buscaCategoria("Programação"));
        assertEquals(livro, acervo.buscaCodigo("CC-01"));

        Cadastro aluno = new Cadastro(1, "João", new Date(), true, "joao@udesc.br", "hash", "47999999999", 0, new PoliticaAluno());
        Date vencido = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3));
        Emprestimo emp = new Emprestimo(1, new Date(), vencido, aluno, ex1);
        emp.setStatus(StatusEmprestimo.ATRASADO);
        emp.setMulta(new Multa(1, 6.0, "PENDENTE"));

        acervo.registrarEmprestimo(emp);

        assertEquals(1, acervo.relatorioAtrasados().length);
        assertEquals(1, acervo.relatorioItensMaisEmprestados(5).length);
        assertEquals(1, acervo.relatorioMultasPendentes().length);
    }
}