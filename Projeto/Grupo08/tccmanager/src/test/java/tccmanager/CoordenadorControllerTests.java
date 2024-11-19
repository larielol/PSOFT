package tccmanager.tccmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tccmanager.tccmanager.Dto.*;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Repository.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@DisplayName("Classe de testes de coordenador")
public class CoordenadorControllerTests {

    final String URI_COORDENADOR = "/coordenador";
    final String URI_ALUNO = "/alunos";
    final String URI_AREAESTUDO = "/areasEstudo";
    final String URI_PROFESSOR = "/professores";

    @Autowired
    MockMvc driver;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
        temaTCCRepository.deleteAllInBatch();
        alunoRepository.deleteAllInBatch();
        professorRepository.deleteAllInBatch();
        areaEstudoRepository.deleteAllInBatch();
        solicitacaoRepository.deleteAllInBatch();
    }

    //Teste bastante extenso, vão existir três alunos no sistema,
    //um que tem professor orientador, um que tem tema
    //mas não tem professor orientador e
    //o último sem tema. Todos devem aparecer no relatório.
    @Test
    @DisplayName("Teste para gerar um relatório completo")
    void testGerandoRelatorioCompleto() throws Exception {
        Session session = entityManager.unwrap(Session.class);
        session.clear();

        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(1)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)));

        AreaEstudoPostPutDto areaEstudoPostPutDto = AreaEstudoPostPutDto.builder()
                .nomeAreaEstudo("Inteligencia Artificial")
                .temasTCC(null).build();

        driver.perform(post(URI_AREAESTUDO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(areaEstudoPostPutDto)));

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Sabrina")
                .matricula("121210110")
                .email("sabrina@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        Optional<AreaEstudo> areaEstudoOptional = areaEstudoRepository.findById(1L);
        AreaEstudo areaEstudo = areaEstudoOptional.get();
        areasInteresse.add(areaEstudo);

        TemaTCCPostPutDto temaTCCPostPutDto = TemaTCCPostPutDto.builder()
                .titulo("TCC")
                .descricao("Padrao")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(null).build();

        driver.perform(post(URI_ALUNO + "/121210110/temas-tcc")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(temaTCCPostPutDto)));

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/" + alunoPostPutDto.getMatricula() + "/solicitar-temaProprio?idProfessor=1&idTema=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)));

        driver.perform(put("/professores/1/solicitacoes-temas-tcc-aluno-aprovar" +
                "?matricula=121210110&idSolicitacao=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        TemaTCCPostPutDto temaTCCPostPutDto2 = TemaTCCPostPutDto.builder()
                .titulo("TCC2")
                .descricao("Padrao")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(null).build();

        AlunoPostPutDto alunoPostPutDto2 = AlunoPostPutDto.builder()
                .nome("Ariel")
                .matricula("121210111")
                .email("ariel@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto2)));

        driver.perform(post(URI_ALUNO + "/121210111/temas-tcc")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(temaTCCPostPutDto2)));

        AlunoPostPutDto alunoPostPutDto3 = AlunoPostPutDto.builder()
                .nome("Ana")
                .matricula("121210112")
                .email("ana@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto3)));

        String responseJSONString = driver.perform(get(URI_COORDENADOR + "/relatorio")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseJSONString.contains("Sabrina"));
        assertTrue(responseJSONString.contains("Ariel"));
        assertTrue(responseJSONString.contains("Ana"));
        assertTrue(responseJSONString.contains("Fabio"));
        assertTrue(responseJSONString.contains("TCC"));
        assertTrue(responseJSONString.contains("Inteligencia Artificial"));

        solicitacaoRepository.deleteById(1L);
    }

    @Test
    @DisplayName("Teste para gerar relatório do sistema sem dados de aluno")
    void testGerandoRelatorioSemDadosDeAlunos() throws Exception {
        String responseJSONString = driver.perform(get(URI_COORDENADOR + "/relatorio")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseJSONString.contains("Sem dados de alunos no sistema."));
    }
}
