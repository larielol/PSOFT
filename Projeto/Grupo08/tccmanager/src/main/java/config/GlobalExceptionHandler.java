package tccmanager.tccmanager.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tccmanager.tccmanager.Exception.*;
import tccmanager.tccmanager.Exception.AlunoException.AlunoInexistenteException;
import tccmanager.tccmanager.Exception.AlunoException.AlunoSemTemasCadastradosException;
import tccmanager.tccmanager.Exception.AlunoException.MatriculaDuplicadaException;
import tccmanager.tccmanager.Exception.AreaEstudoException.AreaEstudoDuplicadaException;
import tccmanager.tccmanager.Exception.AreaEstudoException.AreaEstudoInexistenteException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorInexistenteException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorQuotaInvalidaException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorSemSolicitacoesException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorSemTemasCadastradosException;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoDeTemaNaoCadastradoPeloAlunoException;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoDeTemaNaoCadastradoPeloProfessorException;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoInexistenteException;
import tccmanager.tccmanager.Exception.TemaTCCException.TemaTCCInexistenteException;
import tccmanager.tccmanager.Exception.TemaTCCException.TemaTCCNaoCadastradoPeloAlunoException;
import tccmanager.tccmanager.Exception.TemaTCCException.TemaTCCNaoCadastradoPeloProfessorException;
import tccmanager.tccmanager.Exception.TemaTCCException.TituloDuplicadoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MatriculaDuplicadaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMatriculaDuplicadaException(MatriculaDuplicadaException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleEmailDuplicadoException(EmailDuplicadoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AlunoInexistenteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleAlunoInexistenteException(AlunoInexistenteException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ProfessorInexistenteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleProfessorInexistenteException(ProfessorInexistenteException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AreaEstudoDuplicadaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleAreaEstudoDuplicadaException(AreaEstudoDuplicadaException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(TituloDuplicadoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleTituloDuplicadoException(TituloDuplicadoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AreaEstudoInexistenteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleAreaEstudoInexistenteException(AreaEstudoInexistenteException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AlunoSemTemasCadastradosException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleAlunoSemTemasCadastradosException(AlunoSemTemasCadastradosException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ProfessorQuotaInvalidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleProfessorQuotaInvalidaException(ProfessorQuotaInvalidaException ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ProfessorSemTemasCadastradosException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleProfessorSemTemasCadastradosException(ProfessorSemTemasCadastradosException ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(TemaTCCInexistenteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleTemaTCCInexistenteException(TemaTCCInexistenteException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(TemaTCCNaoCadastradoPeloAlunoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleTemaTCCNaoCadastradoPeloAlunoException(TemaTCCNaoCadastradoPeloAlunoException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(TemaTCCNaoCadastradoPeloProfessorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleTemaTCCNaoCadastradoPeloProfessorException(TemaTCCNaoCadastradoPeloProfessorException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SolicitacaoInexistenteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleSolicitacaoInexistenteException(SolicitacaoInexistenteException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SolicitacaoDeTemaNaoCadastradoPeloAlunoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleSolicitacaoDeTemaNaoCadastradoPeloAlunoException(SolicitacaoDeTemaNaoCadastradoPeloAlunoException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SolicitacaoDeTemaNaoCadastradoPeloProfessorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleSolicitacaoDeTemaNaoCadastradoPeloProfessorException(SolicitacaoDeTemaNaoCadastradoPeloProfessorException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ProfessorSemSolicitacoesException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleProfessorSemSolicitacoesException(ProfessorSemSolicitacoesException ex) {
        return ResponseEntity.notFound().build();
    }
}