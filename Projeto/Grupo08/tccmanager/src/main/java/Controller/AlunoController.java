package tccmanager.tccmanager.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tccmanager.tccmanager.Dto.AlunoPostPutDto;
import tccmanager.tccmanager.Dto.SolicitacaoPostPutDto;
import tccmanager.tccmanager.Dto.TemaTCCPostPutDto;
import tccmanager.tccmanager.Service.AlunoService.*;

@RestController
@RequestMapping(value = "/alunos", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlunoController {

    @Autowired
    AlunoCadastrarService alunoCadastrarService;

    @Autowired
    AlunoAcessarService alunoAcessarService;

    @Autowired
    AlunoAtualizarService alunoAtualizarService;

    @Autowired
    AlunoRemoverService alunoRemoverService;

    @Autowired
    AlunoListarProfessoresService alunoListarProfessoresService;

    @Autowired
    AlunoDemonstrarInteresseService alunoDemonstrarInteresseService;

    @Autowired
    AlunoCadastrarTemaTCCService alunoCadastrarTemaTCCService;

    @Autowired
    AlunoListarTemasCadastradosPorProfessorService alunoListarTemasCadastradosPorProfessorService;

    @Autowired
    AlunoSolicitarOrientacaoTemaProprioService alunoSolicitarOrientacaoTemaProprioService;

    @Autowired
    AlunoSolicitarOrientacaoTemaProfessorService alunoSolicitarOrientacaoTemaProfessorService;

    @PostMapping()
    public ResponseEntity<?> cadastrarAluno(@RequestBody @Valid AlunoPostPutDto alunoPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alunoCadastrarService.cadastrarAluno(alunoPostPutDto));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<?> acessarAluno(@PathVariable String matricula) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alunoAcessarService.acessarAluno(matricula));
    }

    @PutMapping("/{matricula}")
    public ResponseEntity<?> atualizarAluno(@PathVariable String matricula,
                                            @RequestBody @Valid AlunoPostPutDto alunoPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alunoAtualizarService.atualizarAluno(matricula, alunoPostPutDto));
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<?> removerAluno(@PathVariable String matricula) {
        alunoRemoverService.removerAluno(matricula);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("/{matricula}/demonstrar-interesse")
    public ResponseEntity<?> demonstrarInteresse(@PathVariable String matricula,
                                                 @RequestParam Long idAreaEstudo) {
        alunoDemonstrarInteresseService.demonstrarInteresse(matricula, idAreaEstudo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Interesse demonstrado com sucesso");
    }

    @GetMapping("/{matricula}/professores")
    public ResponseEntity<?> listarProfessores(@PathVariable String matricula) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alunoListarProfessoresService.listarProfessores(matricula));
    }

    @PostMapping("/{matricula}/temas-tcc")
    public ResponseEntity<?> cadastrarTemaTcc(@PathVariable String matricula,
                                              @Valid @RequestBody TemaTCCPostPutDto temaTCCPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alunoCadastrarTemaTCCService.cadastrarTemaTCC(matricula, temaTCCPostPutDto));
    }

    @GetMapping("/temasTCC")
    public ResponseEntity<?> listarTemasTCCCadastradosPorProfessor() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alunoListarTemasCadastradosPorProfessorService.listarTemasCadastradosPorProfessores());
    }

    @PostMapping("/{matricula}/solicitar-temaProprio")
    public ResponseEntity<?> solicitarOrientacaoTemaProprio(@PathVariable String matricula,
                                                            @RequestParam Long idProfessor,
                                                            @RequestParam Long idTema,
                                                            @Valid @RequestBody SolicitacaoPostPutDto solicitacaoPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alunoSolicitarOrientacaoTemaProprioService.solicitarOrientacaoTemaProprio(matricula, idProfessor, idTema, solicitacaoPostPutDto));
    }

    @PostMapping("/{matricula}/temaProfessor")
    public ResponseEntity<?> solicitarOrientacaoTemasCadastradosPorProfessor(@PathVariable String matricula,
                                                                             @RequestParam Long idProfessor,
                                                                             @RequestParam Long idTema,
                                                                             @Valid @RequestBody SolicitacaoPostPutDto solicitacaoPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alunoSolicitarOrientacaoTemaProfessorService.solicitarOrientacaoTemaProfessor(matricula, idProfessor, idTema, solicitacaoPostPutDto));
    }
}
