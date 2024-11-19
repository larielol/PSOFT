package tccmanager.tccmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tccmanager.tccmanager.Dto.*;
import tccmanager.tccmanager.Enum.StatusSolicitacao;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Exception.AreaEstudoException.AreaEstudoInexistenteException;
import tccmanager.tccmanager.Exception.EmailDuplicadoException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorInexistenteException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorQuotaInvalidaException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorSemSolicitacoesException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorSemTemasCadastradosException;
import tccmanager.tccmanager.Exception.TemaTCCException.TituloDuplicadoException;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@DisplayName("Classe de testes de professor")
public class ProfessorControllerTests {
    final String URI_ALUNO = "/alunos";
    final String URI_PROFESSOR = "/professores";
    final String URI_AREAESTUDO = "/areasEstudo";

    @Autowired
    MockMvc driver;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Autowired
    AlunoRepository alunoRepository;

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
    @DisplayName("Teste para o cadastro de um professor válido")
    void testCadastrandoProfessorValido() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, professorRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um professor com um nome vazio")
    void testCadastrandoProfessorNomeVazio() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, professorRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um professor com um email vazio")
    void testCadastrandoProfessorEmailVazio() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, professorRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um professor com email duplicado")
    void testCadastrandoProfessorEmailDuplicado() throws Exception {
        ProfessorPostPutDto professor1 = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professor1)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        ProfessorPostPutDto professor2 = ProfessorPostPutDto.builder()
                .nome("Melina")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        try {
            driver.perform(post(URI_PROFESSOR)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(professor2)));
        } catch (EmailDuplicadoException e) {
            assertEquals("O email já está em uso: fabio@computacao.ufcg.edu.br", e.getMessage());
        }

        assertEquals(1, professorRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um professor com quota negativa")
    void testCadastrandoProfessorQuotaNegativa() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(-1)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, professorRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um professor com um nome nulo")
    void testCadastrandoProfessorNomeNulo() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome(null)
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, professorRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para o cadastro de um professor com um email nulo")
    void testCadastrandoProfessorEmailNulo() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email(null)
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, professorRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para acessar um professor válido")
    void testAcessandoProfessorValido() throws Exception {
        Professor professor = professorRepository.save(
                Professor.builder()
                        .nome("Melina")
                        .email("melina@computacao.ufcg.edu.br")
                        .laboratorios("LSD")
                        .quota(0)
                        .areasDeInteresse(null)
                        .temasTCC(null).build());

        String responseJSONString = driver.perform(get(URI_PROFESSOR + "/" + professor.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Professor resultado = objectMapper.readValue(responseJSONString, Professor.class);
        assertEquals(professor, resultado);
    }

    @Test
    @DisplayName("Teste para acessar um professor inexistente")
    void testAcessandoProfessorInexistente() throws Exception {

        try {
            driver.perform(get(URI_PROFESSOR + "/" + "1")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        } catch (ProfessorInexistenteException e) {
            assertEquals("Esse professor não existe", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar o nome de um professor válido")
    void testAtualizandoNomeProfessorValido() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        ProfessorPostPutDto professorComAlteracoes = ProfessorPostPutDto.builder()
                .nome("Fabio Jorge")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        String responseJSONString = driver.perform(put(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorComAlteracoes)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Professor resultado = objectMapper.readValue(responseJSONString, Professor.class);
        assertEquals(1, professorRepository.findAll().size());
        assertEquals("Fabio Jorge", resultado.getNome());
    }

    @Test
    @DisplayName("Teste para atualizar o nome de um professor inválido")
    void testAtualizandoNomeProfessorInvalido() throws Exception {
        try {
            driver.perform(put(URI_PROFESSOR + "/" + "1"));
        } catch (ProfessorInexistenteException e) {
            assertEquals("Esse professor não existe", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar o nome de um professor válido para valor vazio")
    void testAtualizandoNomeProfessorValidoValorVazio() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        ProfessorPostPutDto professorComAlteracoes = ProfessorPostPutDto.builder()
                .nome("")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Professor resultado = objectMapper.readValue(responseJSONString, Professor.class);
        assertEquals("Fabio", resultado.getNome());
    }

    @Test
    @DisplayName("Teste para atualizar o nome de um professor válido para valor nulo")
    void testAtualizandoNomeProfessorValidoValorNulo() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        ProfessorPostPutDto professorComAlteracoes = ProfessorPostPutDto.builder()
                .nome(null)
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Professor resultado = objectMapper.readValue(responseJSONString, Professor.class);
        assertEquals("Fabio", resultado.getNome());
    }

    @Test
    @DisplayName("Teste para atualizar o email de um professor válido")
    void testAtualizandoEmailProfessorValido() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        ProfessorPostPutDto professorComAlteracoes = ProfessorPostPutDto.builder()
                .nome("Fabio Jorge")
                .email("melina@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        String responseJSONString = driver.perform(put(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorComAlteracoes)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Professor resultado = objectMapper.readValue(responseJSONString, Professor.class);
        assertEquals(1, professorRepository.findAll().size());
        assertEquals("melina@computacao.ufcg.edu.br", resultado.getEmail());
    }

    @Test
    @DisplayName("Teste para atualizar o email de um professor válido para valor vazio")
    void testAtualizandoEmailProfessorValidoValorVazio() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        ProfessorPostPutDto professorComAlteracoes = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Professor resultado = objectMapper.readValue(responseJSONString, Professor.class);
        assertEquals("fabio@computacao.ufcg.edu.br", resultado.getEmail());
    }

    @Test
    @DisplayName("Teste para atualizar o email de um professor válido para valor nulo")
    void testAtualizandoEmailProfessorValidoValorNulo() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        ProfessorPostPutDto professorComAlteracoes = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email(null)
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(put(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorComAlteracoes)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_PROFESSOR + "/" + "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Professor resultado = objectMapper.readValue(responseJSONString, Professor.class);
        assertEquals("fabio@computacao.ufcg.edu.br", resultado.getEmail());
    }

    @Test
    @DisplayName("Teste para atualizar o email de um usuário para um valor duplicado")
    void testAtualizandoEmailProfessorValidoValorDuplicado() throws Exception {
        ProfessorPostPutDto professor1 = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professor1)))
                .andExpect(status().isCreated())
                .andDo(print());

        ProfessorPostPutDto professor2 = ProfessorPostPutDto.builder()
                .nome("Melina")
                .email("melina@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professor2)))
                .andExpect(status().isCreated())
                .andDo(print());

        ProfessorPostPutDto professorEmailDuplicado = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("melina@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        try {
            driver.perform(put(URI_PROFESSOR + "/" + "1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(professorEmailDuplicado)));
        } catch (EmailDuplicadoException e) {
            assertEquals("O email já está em uso: melina@computacao.ufcg.edu.br", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para a remoção de um professor válido")
    void testRemovendoProfessorValido() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(delete(URI_PROFESSOR + "/1"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(0, professorRepository.findAll().size());
    }

    @Test
    @DisplayName("Teste para a remoção de um professor inválido")
    void testRemovendoProfessorInvalido() throws Exception {
        try {
            driver.perform(delete(URI_PROFESSOR + "/1"));
        } catch (ProfessorInexistenteException e) {
            assertEquals("Esse professor não existe", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para cadastro de uma área de interesse válida pelo professor")
    void testCadastrandoAreaInteresseValida() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        AreaEstudoPostPutDto areaEstudoPostPutDto = AreaEstudoPostPutDto.builder()
                .nomeAreaEstudo("Inteligência Artificial")
                .temasTCC(null).build();

        driver.perform(post(URI_AREAESTUDO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(areaEstudoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(put(URI_PROFESSOR + "/" +
                "1" + "/demonstrar-interesse?idAreaEstudo=" +
                "1"))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1, professorRepository.findAll().size());
        Optional<Professor> professorOptional = professorRepository.findById(1L);
        Professor professorAtualizado = professorOptional.get();
        Set<AreaEstudo> areasInteresseAtualizadas = professorAtualizado.getAreasDeInteresse();
        assertEquals(1, areasInteresseAtualizadas.size());
        assertTrue(professorAtualizado.getAreasDeInteresse().stream().anyMatch(a -> a.getNomeAreaEstudo().equals("Inteligência Artificial")));
    }

    @Test
    @DisplayName("Teste para cadastro de uma área de interesse inválida pelo professor")
    void testCadastrandoAreaInteresseInvalida() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        try {
            driver.perform(put(URI_PROFESSOR + "/" +
                            "1" + "/demonstrar-interesse?idAreaEstudo=" +
                            "1"));
        } catch (AreaEstudoInexistenteException e) {
            assertEquals("Essa área de estudo não existe", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para cadastro de uma área de interesse válida pelo professor inválido")
    void testCadastrandoAreaInteresseValidaProfessorInvalido() throws Exception {
        AreaEstudoPostPutDto areaEstudoPostPutDto = AreaEstudoPostPutDto.builder()
                .nomeAreaEstudo("Inteligência Artificial")
                .temasTCC(null).build();

        driver.perform(post(URI_AREAESTUDO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(areaEstudoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        try {
            driver.perform(put(URI_PROFESSOR + "/" +
                    "1" + "/demonstrar-interesse?idAreaEstudo=" +
                    "1"));
        } catch (ProfessorInexistenteException e) {
            assertEquals("Esse professor não existe", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar a quota de um professor válido")
    void testAtualizandoQuotaProfessorValido() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        driver.perform(put(URI_PROFESSOR + "/" + "1" + "/quota?novaQuota=" + "5"))
                .andExpect(status().isOk())
                .andDo(print());

        Optional<Professor> professorOptional = professorRepository.findById(1L);
        Professor professorAtualizado = professorOptional.get();
        assertEquals(5, professorAtualizado.getQuota());
    }

    @Test
    @DisplayName("Teste para atualizar a quota de um professor inválido")
    void testAtualizandoQuotaProfessorInvalido() throws Exception {
        try {
            driver.perform(put(URI_PROFESSOR + "/" + "1" + "/quota?novaQuota=" + "5"));
        } catch (ProfessorInexistenteException e) {
            assertEquals("Esse professor não existe", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar a quota de um professor para um valor negativo")
    void testAtualizandoQuotaProfessorValorNegativo() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        try {
            driver.perform(put(URI_PROFESSOR + "/" + "1" + "/quota?novaQuota=" + "-5"));
        } catch (ProfessorQuotaInvalidaException e) {
            assertEquals("A quota não pode ser negativa.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar a quota de um professor para um valor acima da quota maxima")
    void testAtualizandoQuotaProfessorValorAcimaMaximo() throws Exception {
        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        try {
        driver.perform(put(URI_PROFESSOR + "/" + "1" + "/quota?novaQuota=" + "200"));
        } catch (ProfessorQuotaInvalidaException e) {
            assertEquals("Professor não está disponível.", e.getMessage());
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

        driver.perform(post(URI_PROFESSOR + "/1/temas-tcc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(temaTCCPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, temaTCCRepository.findAll().size());
        Optional<TemaTCC> temaTCCOptional = temaTCCRepository.findById(1L);
        TemaTCC temaTCC = temaTCCOptional.get();
        assertEquals(StatusTemaTCC.NOVO, temaTCC.getStatus());

        Optional<Professor> professorOptional = professorRepository.findById(1l);
        Professor professor = professorOptional.get();
        assertTrue(professor.getTemasTCC().contains(temaTCC));
        assertEquals(professor, temaTCC.getProfessor());
    }

    @Test
    @DisplayName("Teste para cadastrar proposta de TCC válida por professor inválido")
    void testCadastrandoPropostaTCCValidaPorProfessorInvalido() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        TemaTCCPostPutDto temaTCCPostPutDto = TemaTCCPostPutDto.builder()
                .titulo("TCC")
                .descricao("Padrao")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(null).build();
        try {
            driver.perform(post(URI_PROFESSOR + "/1/temas-tcc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(temaTCCPostPutDto)));
        } catch (ProfessorInexistenteException e) {
            assertEquals("O professor não existe", e.getMessage());
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
            driver.perform(post(URI_PROFESSOR + "/1/temas-tcc")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(temaTCCPostPutDto)));
        } catch (TituloDuplicadoException e) {
            assertEquals("O título do TCC já está em uso: " + temaTCCPostPutDto.getTitulo(), e.getMessage());
        }
    }
    @Test
    @DisplayName("Teste para listar os temas cadastrados por um professor válido")
    void testListandoTemasPropriosProfessorValido() throws Exception {
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

        Optional<AreaEstudo> areaEstudoOptional = areaEstudoRepository.findById(1L);
        AreaEstudo areaEstudo = areaEstudoOptional.get();
        areasInteresse.add(areaEstudo);

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        String professorResponse = driver.perform(post(URI_PROFESSOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Professor professor = objectMapper.readValue(professorResponse, Professor.class);

        TemaTCCPostPutDto temaTCCPostPutDto1 = TemaTCCPostPutDto.builder()
                .titulo("TCC1")
                .descricao("Padrao1")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(professor).build();

        driver.perform(post(URI_PROFESSOR + "/" + professor.getId() + "/temas-tcc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(temaTCCPostPutDto1)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        TemaTCCPostPutDto temaTCCPostPutDto2 = TemaTCCPostPutDto.builder()
                .titulo("TCC2")
                .descricao("Padrao2")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(professor).build();

        driver.perform(post(URI_PROFESSOR + "/" + professor.getId() + "/temas-tcc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(temaTCCPostPutDto2)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_PROFESSOR + "/" + professor.getId() + "/temas-tcc")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertThat(responseJSONString)
                .contains("Titulo: -----TCC1-----")
                .contains("Areas de Estudo: -> Inteligencia Artificial")
                .contains("Titulo: -----TCC2-----")
                .contains("Areas de Estudo: -> Inteligencia Artificial");
    }

    @Test
    @DisplayName("Teste para listar os temas cadastrados por um professor sem temas cadastrados")
    void testListandoTemasPropriosProfessorSemTemas() throws Exception {
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

        Optional<AreaEstudo> areaEstudoOptional = areaEstudoRepository.findById(1L);
        AreaEstudo areaEstudo = areaEstudoOptional.get();
        areasInteresse.add(areaEstudo);

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(professorPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        try {
            driver.perform(get(URI_PROFESSOR + "/" + "1" + "/temas-tcc")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch (ProfessorSemTemasCadastradosException e) {
            assertEquals("Esse professor não possui temas cadastrados", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para listar temas de TCC cadastrados por alunos")
    void testListandoTemasTCCCadastradosPorAlunos() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Arthur")
                .matricula("121210088")
                .email("arthur@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(1)
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

        driver.perform(post(URI_ALUNO + "/121210088/temas-tcc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(temaTCCPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        String responseJSONString = driver.perform(get(URI_PROFESSOR + "/1/alunos-temas-tcc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertThat(responseJSONString)
                .contains("Titulo: -----TCC-----")
                .contains("Areas de Estudo: -> Inteligencia Artificial\n");
    }

    @Test
    @DisplayName("Teste para checar a notificação por email enviada ao professor a partir do tema do aluno")
    void testVerificandoNotificacaoTemaAluno() throws Exception {
        testListandoTemasTCCCadastradosPorAlunos();

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/solicitar-temaProprio?idProfessor=1&idTema=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        solicitacaoRepository.deleteById(1L);
    }

    @Test
    @DisplayName("Teste para checar a notificação por email enviada ao professor a partir do tema do professor")
    void testverificandoNotificacaoTemaProfessor() throws Exception {
        testCadastrandoPropostaTCCValida();

        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210088")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/temaProfessor?idProfessor=1&idTema=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        solicitacaoRepository.deleteById(1L);
    }

    @Test
    @DisplayName("Teste para listas solicitações de alunos para temas do professor")
    void testListandoSolicitacoesTemasProfessor() throws Exception {
        testCadastrandoPropostaTCCValida();

        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210088")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/temaProfessor?idProfessor=1&idTema=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)));

        String responseJSONString = driver.perform(get(URI_PROFESSOR + "/1/solicitacoes-temas-tcc")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertThat(responseJSONString)
                .contains("Solicitacao para orientacao do aluno: Lucas")
                .contains("Tema de TCC solicitado: 'TCC'");

        solicitacaoRepository.deleteById(1L);
    }

    @Test
    @DisplayName("Teste para listar solicitações de alunos por professor inválido")
    void testListandoSolicitacoesProfessorInvalido() throws Exception{
        try {
            driver.perform(get(URI_PROFESSOR + "/1/solicitacoes-temas-tcc")
                            .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch (ProfessorInexistenteException e) {
            assertEquals("O professor com esse id não existe: 1L", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para listar solicitações por professor que não cadastrou tema")
    void testListandoSolicitacoesProfessorSemTemas() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(professorPostPutDto)));

        try {
            driver.perform(get(URI_PROFESSOR + "/1/solicitacoes-temas-tcc")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch(ProfessorSemTemasCadastradosException e) {
            assertEquals("Esse professor não tem temas de TCC cadastrados", e.getMessage());
        }
    }

    //Teste para verificar inconsistência em ListarSolicitacoesTemasPropriosService
    //Lógica parecia indicar que todos os temas do sistema tinham que ser cadastrados por um
    //mesmo professor ou o Service lançava exceção
    //Corrigido
    @Test
    @DisplayName("Teste de inconsistência de temas")
    void testInconsistenciaTemas() throws Exception {
        testCadastrandoPropostaTCCValida();

        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Melina")
                .email("melina@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(1)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(professorPostPutDto)));

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210088")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        try {
            driver.perform(get(URI_PROFESSOR + "/2/solicitacoes-temas-tcc")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch (ProfessorSemTemasCadastradosException e) {
            assertEquals("Esse professor não tem temas de TCC cadastrados", e.getMessage());
        }
    }

    //Teste para verificar inconsistência em ListarSolicitacoesTemasPropriosService
    //Lógica parecia indicar que todas as solicitações do sistema tinham que ser para temas de um
    // mesmo professor ou o Service lançava exceção
    //Corrigido
    @Test
    @DisplayName("Teste de inconsistência de solicitações")
    void testInconsistenciaSolicitacoes() throws Exception {
        testCadastrandoPropostaTCCValida();

        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210088")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/temaProfessor?idProfessor=1&idTema=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)));

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Melina")
                .email("melina@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(1)
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(professorPostPutDto)));

        try {
            driver.perform(get(URI_PROFESSOR + "/2/solicitacoes-temas-tcc")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch (ProfessorSemSolicitacoesException e) {
            assertEquals("Esse professor não tem solicitação de orientação para seus temas", e.getMessage());
        }
        solicitacaoRepository.deleteById(1L);
    }

    @Test
    @DisplayName("Teste para aprovar a solicitação de orientação de tema do professor")
    void testAprovandoSolicitacaoTemaProfessor() throws Exception {
        testCadastrandoPropostaTCCValida();

        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210088")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/temaProfessor?idProfessor=1&idTema=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)));

        driver.perform(put("/professores/1/solicitacoes-temas-tcc-professor-aprovar" +
                "?matricula=121210088&idSolicitacao=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, solicitacaoRepository.findAll().size());
        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(1L);
        Solicitacao solicitacao = solicitacaoOptional.get();
        assertEquals(solicitacao.getStatus(), StatusSolicitacao.APROVADA);
        assertEquals(1, temaTCCRepository.findAll().size());
        Optional<TemaTCC> temaTCCOptional = temaTCCRepository.findById(1L);
        TemaTCC temaTCC = temaTCCOptional.get();
        assertEquals(temaTCC.getStatus(), StatusTemaTCC.ALOCADO);

        solicitacaoRepository.deleteById(1L);
    }

    @Test
    @DisplayName("Teste para negar a solicitação de orientação de tema do professor")
    void testNegandoSolicitacaoTemaProfessor() throws Exception {
        testCadastrandoPropostaTCCValida();

        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210088")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/temaProfessor?idProfessor=1&idTema=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)));

        driver.perform(put("/professores/1/solicitacoes-temas-tcc-professor-negar" +
                        "?matricula=121210088&idSolicitacao=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, solicitacaoRepository.findAll().size());
        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(1L);
        Solicitacao solicitacao = solicitacaoOptional.get();
        assertEquals(solicitacao.getStatus(), StatusSolicitacao.NEGADA);
        assertEquals(1, temaTCCRepository.findAll().size());
        Optional<TemaTCC> temaTCCOptional = temaTCCRepository.findById(1L);
        TemaTCC temaTCC = temaTCCOptional.get();
        assertEquals(temaTCC.getStatus(), StatusTemaTCC.NOVO);

        solicitacaoRepository.deleteById(1L);
    }

    @Test
    @DisplayName("Teste para aprovar a solicitação de orientação de tema do aluno")
    void testAprovandoSolicitacaoTemaAluno() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(professorPostPutDto)));

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210088")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        TemaTCCPostPutDto temaTCCPostPutDto = TemaTCCPostPutDto.builder()
                .titulo("TCC")
                .descricao("Padrao")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/temas-tcc")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(temaTCCPostPutDto)));

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/solicitar-temaProprio?idProfessor=1&idTema=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)));

        driver.perform(put("/professores/1/solicitacoes-temas-tcc-aluno-aprovar" +
                        "?matricula=121210088&idSolicitacao=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, solicitacaoRepository.findAll().size());
        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(1L);
        Solicitacao solicitacao = solicitacaoOptional.get();
        assertEquals(solicitacao.getStatus(), StatusSolicitacao.APROVADA);
        assertEquals(1, temaTCCRepository.findAll().size());
        Optional<TemaTCC> temaTCCOptional = temaTCCRepository.findById(1L);
        TemaTCC temaTCC = temaTCCOptional.get();
        assertEquals(temaTCC.getStatus(), StatusTemaTCC.ALOCADO);

        solicitacaoRepository.deleteById(1L);
    }

    @Test
    @DisplayName("Teste para negar a solicitação de orientação de tema do aluno")
    void testNegandoSolicitacaoTemaAluno() throws Exception {
        Set<AreaEstudo> areasInteresse = new HashSet<AreaEstudo>();

        ProfessorPostPutDto professorPostPutDto = ProfessorPostPutDto.builder()
                .nome("Fabio")
                .email("fabio@computacao.ufcg.edu.br")
                .laboratorios("LSD")
                .quota(0)
                .areasDeInteresse(null)
                .temasTCC(null).build();

        driver.perform(post(URI_PROFESSOR)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(professorPostPutDto)));

        AlunoPostPutDto alunoPostPutDto = AlunoPostPutDto.builder()
                .nome("Lucas")
                .matricula("121210088")
                .email("lucas@ccc.ufcg.edu.br")
                .periodoConclusao("2027.2")
                .areasDeInteresse(areasInteresse)
                .temasTCC(null).build();

        driver.perform(post(URI_ALUNO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(alunoPostPutDto)));

        TemaTCCPostPutDto temaTCCPostPutDto = TemaTCCPostPutDto.builder()
                .titulo("TCC")
                .descricao("Padrao")
                .status(null)
                .areasEstudo(areasInteresse)
                .aluno(null)
                .professor(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/temas-tcc")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(temaTCCPostPutDto)));

        SolicitacaoPostPutDto solicitacaoPostPutDto = SolicitacaoPostPutDto.builder()
                .tema(null)
                .aluno(null)
                .professor(null)
                .status(null).build();

        driver.perform(post(URI_ALUNO + "/121210088/solicitar-temaProprio?idProfessor=1&idTema=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(solicitacaoPostPutDto)));

        driver.perform(put("/professores/1/solicitacoes-temas-tcc-aluno-negar" +
                        "?matricula=121210088&idSolicitacao=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, solicitacaoRepository.findAll().size());
        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(1L);
        Solicitacao solicitacao = solicitacaoOptional.get();
        assertEquals(solicitacao.getStatus(), StatusSolicitacao.NEGADA);
        assertEquals(1, temaTCCRepository.findAll().size());
        Optional<TemaTCC> temaTCCOptional = temaTCCRepository.findById(1L);
        TemaTCC temaTCC = temaTCCOptional.get();
        assertEquals(temaTCC.getStatus(), StatusTemaTCC.NOVO);

        solicitacaoRepository.deleteById(1L);
    }
}