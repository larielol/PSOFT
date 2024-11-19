package tccmanager.tccmanager.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tccmanager.tccmanager.Dto.ProfessorPostPutDto;
import tccmanager.tccmanager.Dto.TemaTCCPostPutDto;
import tccmanager.tccmanager.Service.ProfessorService.*;

@RestController
@RequestMapping(value = "/professores", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfessorController {

    @Autowired
    ProfessorCadastrarService professorCadastrarService;

    @Autowired
    ProfessorAcessarService professorAcessarService;

    @Autowired
    ProfessorAtualizarService professorAtualizarService;

    @Autowired
    ProfessorRemoverService professorRemoverService;

    @Autowired
    ProfessorDemonstrarInteresseService professorDemonstrarInteresseService;

    @Autowired
    ProfessorAtualizarQuotaService professorAtualizarQuotaService;

    @Autowired
    ProfessorCadastrarTemaTCCService professorCadastrarTemaTCCService;

    @Autowired
    ProfessorListarTemasPropriosService professorListarTemasPropriosService;

    @Autowired
    ProfessorListarTemasCadastradosPorAlunosService professorListarTemasCadastradosPorAlunosService;

    @Autowired
    ProfessorListarSolicitacoesDeTemasPropriosService professorListarSolicitacoesDeTemasPropriosService;

    @Autowired
    ProfessorAprovarSolicitacaoTemaProprioService professorAprovarSolicitacaoTemaProprioService;

    @Autowired
    ProfessorNegarSolicitacaoTemaProprioService professorNegarSolicitacaoTemaProprioService;

    @Autowired
    ProfessorAprovarSolicitacaoTemaCadastradoPorAlunoService professorAprovarSolicitacaoTemaCadastradoPorAlunoService;

    @Autowired
    ProfessorNegarSolicitacaoTemaCadastradoPorAlunoService professorNegarSolicitacaoTemaCadastradoPorAlunoService;

    @PostMapping()
    public ResponseEntity<?> cadastrarProfessor(@RequestBody @Valid ProfessorPostPutDto professorPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(professorCadastrarService.cadastrarProfessor(professorPostPutDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> acessarProfessor(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorAcessarService.acessarProfessor(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfessor(@PathVariable Long id,
                                            @RequestBody @Valid ProfessorPostPutDto professorPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorAtualizarService.atualizarProfessor(id, professorPostPutDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerProfessor(@PathVariable Long id) {
        professorRemoverService.removerAluno(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("/{id}/demonstrar-interesse")
    public ResponseEntity<?> demonstrarInteresse(@PathVariable Long id,
                                                 @RequestParam Long idAreaEstudo) {
        professorDemonstrarInteresseService.demonstrarInteresse(id, idAreaEstudo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Interesse demonstrado com sucesso");
    }


    @PutMapping("/{id}/quota")
    public ResponseEntity<String> atualizarQuota(@PathVariable Long id, 
                                                @RequestParam int novaQuota) {
        professorAtualizarQuotaService.atualizarQuota(id, novaQuota);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Quota atualizada com sucesso para professor: " + id);
    }

    @PostMapping("/{id}/temas-tcc")
    public ResponseEntity<?> cadastrarTemaTcc(@PathVariable Long id,
                                              @Valid @RequestBody TemaTCCPostPutDto temaTCCPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(professorCadastrarTemaTCCService.cadastrarTemaTCC(id, temaTCCPostPutDto));
    }

    @GetMapping("/{id}/temas-tcc")
    public ResponseEntity<?> listarTemasTCCProprios(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorListarTemasPropriosService.listarTemasProprios(id));
    }


    @GetMapping("/{id}/alunos-temas-tcc")
    public ResponseEntity<?> listarTemasTCCCadastradosPorAlunos(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorListarTemasCadastradosPorAlunosService.listarTemasDosAlunos(id));
    }

    @GetMapping("/{id}/solicitacoes-temas-tcc")
    public ResponseEntity<?> professorListarSolicitacoesDeTemasPropriosService(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorListarSolicitacoesDeTemasPropriosService.listarSolicitacoesDeTemasProprios(id));
    }

    @PutMapping("/{id}/solicitacoes-temas-tcc-professor-aprovar")
    public ResponseEntity<?> professorAprovarSolicitacaoTemaProprio(@PathVariable Long id,
                                                                    @RequestParam String matricula,
                                                                    @RequestParam Long idSolicitacao) {
        professorAprovarSolicitacaoTemaProprioService.aprovarSolicitacaoTemaProprio(id, matricula, idSolicitacao);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Solicitacao de tema de TCC cadastrado pelo professor aprovada!");
    }

    @PutMapping("/{id}/solicitacoes-temas-tcc-professor-negar")
    public ResponseEntity<?> professorNegarSolicitacaoTemaProprio(@PathVariable Long id,
                                                                             @RequestParam String matricula,
                                                                             @RequestParam Long idSolicitacao) {
        professorNegarSolicitacaoTemaProprioService.negarSolicitacaoTemaProprio(id, matricula, idSolicitacao);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Solicitacao de tema de TCC cadastrado pelo professor negada.");
    }



    @PutMapping("/{id}/solicitacoes-temas-tcc-aluno-aprovar")
    public ResponseEntity<?> professorAprovarSolicitacaoTemaCadastradoPorAluno(@PathVariable Long id,
                                                                               @RequestParam String matricula,
                                                                               @RequestParam Long idSolicitacao) {
        professorAprovarSolicitacaoTemaCadastradoPorAlunoService.aprovarSolicitacaoTemaCadastradoPorAluno(id, matricula, idSolicitacao);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Solicitacao de tema de TCC cadastrado pelo aluno aprovada!");
    }

    @PutMapping("/{id}/solicitacoes-temas-tcc-aluno-negar")
    public ResponseEntity<?> professorNegarSolicitacaoTemaCadastradoPorAluno(@PathVariable Long id,
                                                                             @RequestParam String matricula,
                                                                             @RequestParam Long idSolicitacao) {
        professorNegarSolicitacaoTemaCadastradoPorAlunoService.negarSolicitacaoTemaCadastradoPorAluno(id, matricula, idSolicitacao);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Solicitacao de tema de TCC cadastrado pelo aluno negada.");
    }
}
