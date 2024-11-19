package tccmanager.tccmanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tccmanager.tccmanager.Dto.*;
import tccmanager.tccmanager.Enum.StatusSolicitacao;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Exception.AreaEstudoException.AreaEstudoInexistenteException;
import tccmanager.tccmanager.Exception.EmailDuplicadoException;
import tccmanager.tccmanager.Exception.AlunoException.MatriculaDuplicadaException;
import tccmanager.tccmanager.Exception.AlunoException.AlunoInexistenteException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorSemTemasCadastradosException;
import tccmanager.tccmanager.Exception.TemaTCCException.TituloDuplicadoException;
import tccmanager.tccmanager.Model.*;
import tccmanager.tccmanager.Repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Classe de testes de aluno")
public class AlunoControllerTests {
    final String URI_ALUNO = "/alunos";
    final String URI_AREAESTUDO = "/areasEstudo";
    final String URI_PROFESSOR = "/professores";

    @Autowired
    MockMvc driver;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

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

    @Test
    @DisplayName("Teste para o cadastro de um aluno válido")
    void testCadastrandoAlunoValido() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Sabrina")
                .matricula("121210110")
                .email("sabrina@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um aluno com um nome vazio")
    void testCadastrandoAlunoNomeVazio() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("")
                .matricula("121210110")
                .email("ariel@ccc.ufcg.edu.br")
                .periodoConclusao("2021.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um aluno com uma matrícula vazia")
    void testCadastrandoAlunoMatriculaVazia() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Jose")
                .matricula("")
                .email("jose@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um aluno com um email vazio")
    void testCadastrandoAlunoEmailVazio() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Ana")
                .matricula("121210110")
                .email("")
                .periodoConclusao("2024.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um aluno com um período de conclusão vazio")
    void testCadastrandoAlunoPeridoConclusaoVazio() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121210110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de aluno com matrícula duplicada")
    void testCadastrandoAlunoMatriculaDuplicada() throws Exception {
        AlunoPostPutDto aluno1 = AlunoPostPutDto.builder()
                .nome("Sabrina")
                .matricula("121210110")
                .email("sabrina@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(aluno1)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto aluno2 = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121210110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        try {
            driver.perform(post(URI_ALUNO)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(aluno2)));
        } catch (MatriculaDuplicadaException e) {
            assertEquals("A matrícula já está em uso: 121210110", e.getMessage());
        }
        assertEquals(1, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de aluno com email duplicado")
    void testCadastrandoAlunoEmailDuplicada() throws Exception {
        AlunoPostPutDto aluno1 = AlunoPostPutDto.builder()
                .nome("Jose")
                .matricula("121210110")
                .email("jose@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(aluno1)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto aluno2 = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210111")
                .email("jose@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        try {
            driver.perform(post(URI_ALUNO)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(aluno2)));
        } catch (EmailDuplicadoException e) {
            assertEquals("O email já está em uso: jose@ccc.ufcg.edu.br", e.getMessage());
        }

        assertEquals(1, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um aluno com um nome nulo")
    void testCadastrandoAlunoNomeNulo() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome(null)
                .matricula("121210110")
                .email("ariel@ccc.ufcg.edu.br")
                .periodoConclusao("2021.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um aluno com uma matrícula nula")
    void testCadastrandoAlunoMatriculaNula() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Jose")
                .matricula(null)
                .email("jose@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um aluno com um email nulo")
    void testCadastrandoAlunoEmailNulo() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Ana")
                .matricula("121210110")
                .email(null)
                .periodoConclusao("2024.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um aluno com um período de conclusão nulo")
    void testCadastrandoAlunoPeridoConclusaoNulo() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121210110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao(null)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para acessar um aluno válido")
    void testAcessandoAlunoValido() throws Exception {
        Aluno aluno = alunoRepository.save(
                Aluno.builder()
                        .nome("Ana Beatriz")
                        .matricula("121278923")
                        .email("ana.beatriz@ccc.ufcg.edu.br")
                        .periodoConclusao("2023.2")
                        .areasDeInteresse(null)
                        .temasTCC(null).build());

        String responseJSONString = driver.perform(get(URI_ALUNO + "/" + aluno.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals(aluno, resultado);
    }

    @Test
    @DisplayName("Teste para acessar aluno com matrícula inexistente")
    void testAcessandoAlunoMatriculaInexistente() throws Exception {
        String matriculaInexistente = "121212019";

        try {
            driver.perform(get(URI_ALUNO + "/" + matriculaInexistente)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        } catch (AlunoInexistenteException e) {
            assertEquals("O aluno com essa matrícula não existe: " + matriculaInexistente, e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar o nome de um aluno válido")
    void testAtualizandoNomeAlunoValido() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur Miranda")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        String responseJSONString = driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals(1, alunoRepository.findAll().size());
        assertEquals("Arthur Miranda", resultado.getNome());
    }

    @Test
    @DisplayName("Teste para atualizar o nome de um aluno inválido")
    void testAtualizandoNomeAlunoInvalido() throws Exception {

        String matriculaInexistente = "121212019";

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121212019")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        try {
            driver.perform(put(URI_ALUNO + "/" + matriculaInexistente)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(alunoPostPutDto)));
        } catch (AlunoInexistenteException e) {
            assertEquals("O aluno com essa matrícula não existe: " + matriculaInexistente, e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar o nome de um aluno válido para valor em branco")
    void testAtualizandoNomeAlunoValidoValorBranco() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_ALUNO + "/121120110")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals("Arthur", resultado.getNome());
    }

    @Test
    @DisplayName("Teste para atualizar o nome de um aluno válido para valor nulo")
    void testAtualizandoNomeAlunoValidoValorNulo() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome(null)
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_ALUNO + "/121120110")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals("Arthur", resultado.getNome());
    }

    @Test
    @DisplayName("Teste para atualizar o período de conclusão de um aluno válido")
    void testAtualizandoPeriodoConclusaoAlunoValido() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2030.1")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        String responseJSONString = driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals(1, alunoRepository.findAll().size());
        assertEquals("2030.1", resultado.getPeriodoConclusao());
    }

    @Test
    @DisplayName("Teste para atualizar o período de conclusão de um aluno válido para valor em branco")
    void testAtualizandoPeriodoConclusaoAlunoValidoValorBranco() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_ALUNO + "/121120110")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals("2027.2", resultado.getPeriodoConclusao());
    }

    @Test
    @DisplayName("Teste para atualizar o período de conclusão de um aluno válido para valor nulo")
    void testAtualizandoPeriodoConclusaoAlunoValidoValorNulo() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao(null)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_ALUNO + "/121120110")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals("2027.2", resultado.getPeriodoConclusao());
    }

    @Test
    @DisplayName("Teste para atualizar a matrícula de um aluno válido")
    void testAtualizandoMatriculaAlunoValido() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120111")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        String responseJSONString = driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals(1, alunoRepository.findAll().size());
        assertEquals("121120111", resultado.getMatricula());
    }

    @Test
    @DisplayName("Teste para atualizar a matrícula de um aluno válido com valor branco")
    void testAtualizandoMatriculaAlunoValidoValorBranco() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_ALUNO + "/121120110")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals("121120110", resultado.getMatricula());
    }

    @Test
    @DisplayName("Teste para atualizar a matrícula de um usuário para um valor duplicado")
    void testAtualizandoMatriculaAlunoValidoValorDuplicado() throws Exception {

        AlunoPostPutDto aluno1 = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("123456789")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(aluno1)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto aluno2 = AlunoPostPutDto.builder()
                .nome("Maria")
                .matricula("987654321")
                .email("maria@@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(aluno2)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComMatriculaDuplicada = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("987654321")
                .email("Lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        try {
            driver.perform(put(URI_ALUNO + "/123456789")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(alunoComMatriculaDuplicada)));
        } catch (MatriculaDuplicadaException e) {
            assertEquals("A matrícula já está em uso: 987654321", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar o email de um aluno válido")
    void testAtualizandoEmailAlunoValido() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur.miranda@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        String responseJSONString = driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals(1, alunoRepository.findAll().size());
        assertEquals("arthur.miranda@ccc.ufcg.edu.br", resultado.getEmail());
    }

    @Test
    @DisplayName("Teste para atualizar o email de um aluno válido com valor vazio")
    void testAtualizandoEmailAlunoValidoValorVazio() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_ALUNO + "/121120110")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals("arthur@ccc.ufcg.edu.br", resultado.getEmail());
    }

    @Test
    @DisplayName("Teste para atualizar o email de um aluno válido com valor nulo")
    void testAtualizandoEmailAlunoValidoValorNulo() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComAlteracoes = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121120110")
                .email(null)
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_ALUNO + "/" + alunoPostPutDto.getMatricula())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_ALUNO + "/121120110")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Aluno resultado = objectMapper.readValue(responseJSONString, Aluno.class);
        assertEquals("arthur@ccc.ufcg.edu.br", resultado.getEmail());
    }

    @Test
    @DisplayName("Teste para atualizar o email de um usuário para um valor duplicado")
    void testAtualizandoEmailAlunoValidoValorDuplicado() throws Exception {

        AlunoPostPutDto aluno1 = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("123456789")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(aluno1)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto aluno2 = AlunoPostPutDto.builder()
                .nome("Maria")
                .matricula("987654321")
                .email("maria@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(aluno2)))
                .andExpect(status().isCreated())
                .andDo(print());

        AlunoPostPutDto alunoComMatriculaDuplicada = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("123456789")
                .email("maria@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        try {
            driver.perform(put(URI_ALUNO + "/123456789")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(alunoComMatriculaDuplicada)));
        } catch (EmailDuplicadoException e) {
            assertEquals("O email já está em uso: maria@ccc.ufcg.edu.br", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para a remoção de um aluno válido")
    void testRemovendoAlunoValido() throws Exception {
        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Sabrina")
                .matricula("121210110")
                .email("sabrina@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(delete(URI_ALUNO + "/121210110"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, alunoRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para a remoção de um aluno inválido")
    void testRemovendoAlunoInvalido() throws Exception {

        String matriculaInexistente = "12121017";

        try {
            driver.perform(delete(URI_ALUNO + "/" + matriculaInexistente));
        } catch (AlunoInexistenteException e) {
            assertEquals("O aluno com essa matrícula não existe: " + matriculaInexistente, e.getMessage());
        }
    }

    //Testes etapa 2
    @Test
    @DisplayName("Teste para cadastro de uma área de interesse válida pelo aluno")
    void testCadastrandoAreaInteresseValida() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Sabrina")
                .matricula("121210110")
                .email("sabrina@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        AreaEstudoPostPutDto areaEstudoPostPutDto = AreaEstudoPostPutDto.builder()
                .nomeAreaEstudo("Inteligencia Artificial")
                .temasTCC(null).build();

        driver.perform(post(URI_AREAESTUDO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(areaEstudoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(put(URI_ALUNO + "/" +
                        alunoPostPutDto.getMatricula() +
                        "/demonstrar-interesse?idAreaEstudo=" +
                        "1"))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1, alunoRepository.findAll().size());
        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula("121210110");
        Aluno alunoAtualizado = alunoOptional.get();
        Set<AreaEstudo> areasInteresseAtualizadas = alunoAtualizado.getAreasDeInteresse();
        assertEquals(1, areasInteresseAtualizadas.size());
        assertTrue(alunoAtualizado.getAreasDeInteresse().stream().
                anyMatch(a -> a.getNomeAreaEstudo().equals("Inteligencia Artificial")));
    }

    @Test
    @DisplayName("Teste para cadastro de uma área de interesse inválida pelo aluno")
    void testCadastrandoAreaInteresseInvalida() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Sabrina")
                .matricula("121210110")
                .email("sabrina@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        try {
            driver.perform(put(URI_ALUNO + "/" +
                            alunoPostPutDto.getMatricula() +
                            "/demonstrar-interesse?idAreaEstudo=" +
                            "1"));
        } catch (AreaEstudoInexistenteException e) {
            assertEquals("Essa área de estudo não existe", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para cadastro de uma área de interesse válida pelo aluno inválido")
    void testCadastrandoAreaInteresseValidaAlunoInvalido() throws Exception {
        String matriculaInexistente = "121212019";

        AreaEstudoPostPutDto areaEstudoPostPutDto = AreaEstudoPostPutDto.builder()
                .nomeAreaEstudo("Inteligencia Artificial")
                .temasTCC(null).build();

        driver.perform(post(URI_AREAESTUDO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(areaEstudoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        try {
            driver.perform(put(URI_ALUNO + "/" +
                    matriculaInexistente +
                    "/demonstrar-interesse?idAreaEstudo=" +
                    "1"));
        } catch (AlunoInexistenteException e) {
            assertEquals("O aluno com essa matrícula não existe: " + matriculaInexistente, e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para listar professores disponíveis para orientar tema de TCC")
    void testListandoProfessoresDisponiveis() throws Exception {

        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Sabrina")
                .matricula("121210110")
                .email("sabrina@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        AreaEstudoPostPutDto areaEstudoPostPutDto = AreaEstudoPostPutDto.builder()
                .nomeAreaEstudo("Inteligencia Artificial")
                .temasTCC(null).build();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(1)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        ProfessorPostPutDto professorPostPutDto2 = ProfessorPostPutDto.builder()
                .nome("Melina")
                .email("melina@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(1)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto2)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(post(URI_AREAESTUDO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(areaEstudoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(put(URI_ALUNO + "/" +
                        alunoPostPutDto.getMatricula() +
                        "/demonstrar-interesse?idAreaEstudo=" +
                        "1"))
                .andExpect(status().isOk())
                .andDo(print());

        driver.perform(put(URI_PROFESSOR + "/" +
                        "1" + "/demonstrar-interesse?idAreaEstudo=" +
                        "1"))
                .andExpect(status().isOk())
                .andDo(print());

        String responseJSONString = driver.perform(get(URI_ALUNO + "/" + alunoPostPutDto.getMatricula() +
                        "/professores")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Professor> professores = objectMapper.readValue(responseJSONString, new TypeReference<List<Professor>>() {});
        assertEquals(1, professores.size());
        assertTrue(professores.get(0).getQuota() > 0);
        assertTrue(professores.get(0).getAreasDeInteresse().stream()
                .anyMatch(area -> area.getNomeAreaEstudo().equals("Inteligencia Artificial")));
    }

    @Test
    @DisplayName("Teste para listar professores disponíveis para orientar tema de TCC por aluno inválido")
    void testListandoProfessoresDisponiveisAlunoInvalido() throws Exception {

        String matriculaInvalida = "121210110";

        try {
            driver.perform(get(URI_ALUNO + "/" + matriculaInvalida +
                    "/professores")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch (AlunoInexistenteException e) {
            assertEquals("O aluno com essa matrícula não existe: " + matriculaInvalida, e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para cadastrar proposta de TCC válida")
    void testCadastrandoPropostaTCCValida() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        testCadastrandoAreaInteresseValida();

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
                .content(objectMapper.writeValueAsString(temaTCCPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, temaTCCRepository.findAll().size());
        Optional<TemaTCC> temaTCCOptional = temaTCCRepository.findById(1L);
        TemaTCC temaTCC = temaTCCOptional.get();
        assertEquals(StatusTemaTCC.NOVO, temaTCC.getStatus());

        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula("121210110");
        Aluno aluno = alunoOptional.get();
        assertTrue(aluno.getTemasTCC().contains(temaTCC));
        assertEquals(aluno, temaTCC.getAluno());
    }

    @Test
    @DisplayName("Teste para cadastrar proposta de TCC válida por aluno inválido")
    void testCadastrandoPropostaTCCValidaPorAlunoInvalido() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        TemaTCCPostPutDto temaTCCPostPutDto = TemaTCCPostPutDto.builder()
                .titulo("TCC")
                .descricao("Padrao")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(null).build();
        try {
            driver.perform(post(URI_ALUNO + "/121210110/temas-tcc")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(temaTCCPostPutDto)));
        } catch (AlunoInexistenteException e) {
            assertEquals("O aluno com essa matrícula não existe: 121210110", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para cadastrar uma proposta de TCC com título duplicado")
    void testCadastrandoPropostaTCCTituloDuplicado() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        testCadastrandoPropostaTCCValida();

        TemaTCCPostPutDto temaTCCPostPutDto = TemaTCCPostPutDto.builder()
                .titulo("TCC")
                .descricao("Padrao2")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(null).build();

        try {
            driver.perform(post(URI_ALUNO + "/121210110/temas-tcc")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(temaTCCPostPutDto)));
        } catch (TituloDuplicadoException e) {
            assertEquals("O título do TCC já está em uso: " + temaTCCPostPutDto.getTitulo(), e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para listar temas de TCC cadastrados por professores")
    void testListandoTemaTCCDosProfessores() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AreaEstudoPostPutDto areaEstudoPostPutDto = AreaEstudoPostPutDto.builder()
                .nomeAreaEstudo("Inteligencia Artificial")
                .temasTCC(null).build();

        driver.perform(post(URI_AREAESTUDO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(areaEstudoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(1)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

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

        driver.perform(post(URI_PROFESSOR + "/1/temas-tcc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(temaTCCPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_ALUNO + "/temasTCC")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertThat(responseJSONString)
                .contains("Titulo: -----TCC-----")
                .contains("Areas de Estudo: -> Inteligencia Artificial")
                .contains("Professor responsavel: Fabio");
    }

    @Test
    @DisplayName("Teste para listar temas de TCC inexistentes cadastrados por professores")
    void testListandoTemasTCCInexistentesDosProfessores() throws Exception {
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
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        try {
            driver.perform(get(URI_ALUNO + "/temasTCC")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch (ProfessorSemTemasCadastradosException e) {
            assertEquals("O professor " + professorPostPutDto.getNome() + " não tem temas de TCC cadastrados.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para solicitar orientação de tema próprio")
    void testSolicitandoOrientacaoTemaProprio() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Sabrina")
                .matricula("121210110")
                .email("sabrina@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        AreaEstudoPostPutDto areaEstudoPostPutDto = AreaEstudoPostPutDto.builder()
                .nomeAreaEstudo("Inteligencia Artificial")
                .temasTCC(null).build();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Melina")
                .email("melina@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(1)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_AREAESTUDO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(areaEstudoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

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
                        .content(objectMapper.writeValueAsString(temaTCCPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/" + alunoPostPutDto.getMatricula() + "/solicitar-temaProprio?idProfessor=1&idTema=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, solicitacaoRepository.findAll().size());
        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(1L);
        Solicitacao solicitacao = solicitacaoOptional.get();
        Optional<TemaTCC> temaTCCOptional = temaTCCRepository.findById(1L);
        TemaTCC temaTCC = temaTCCOptional.get();
        Optional<Professor> professorOptional = professorRepository.findById(1L);
        Professor professor = professorOptional.get();
        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(alunoPostPutDto.getMatricula());
        Aluno aluno = alunoOptional.get();
        assertEquals(solicitacao.getAluno(), aluno);
        assertEquals(solicitacao.getProfessor(), professor);
        assertEquals(solicitacao.getTema(), temaTCC);
        assertEquals(solicitacao.getStatus(), StatusSolicitacao.EM_ANALISE);
        assertEquals(temaTCC.getStatus(), StatusTemaTCC.PENDENTE);
        solicitacaoRepository.deleteById(1L);
    }
}