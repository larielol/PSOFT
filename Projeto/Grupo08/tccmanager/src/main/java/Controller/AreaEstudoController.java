package tccmanager.tccmanager.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tccmanager.tccmanager.Dto.AreaEstudoPostPutDto;
import tccmanager.tccmanager.Service.AreaEstudoService.AreaEstudoAcessarService;
import tccmanager.tccmanager.Service.AreaEstudoService.AreaEstudoAtualizarService;
import tccmanager.tccmanager.Service.AreaEstudoService.AreaEstudoCadastrarService;
import tccmanager.tccmanager.Service.AreaEstudoService.AreaEstudoRemoverService;

@RestController
@RequestMapping(value = "/areasEstudo", produces = MediaType.APPLICATION_JSON_VALUE)
public class AreaEstudoController {

    @Autowired
    AreaEstudoCadastrarService areaEstudoCadastrarService;

    @Autowired
    AreaEstudoAcessarService areaEstudoAcessarService;

    @Autowired
    AreaEstudoAtualizarService areaEstudoAtualizarService;

    @Autowired
    AreaEstudoRemoverService areaEstudoRemoverService;

    @PostMapping()
    public ResponseEntity<?> cadastrarAreaEstudo(@RequestBody @Valid AreaEstudoPostPutDto areaEstudoPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(areaEstudoCadastrarService.cadastrarAreaEstudo(areaEstudoPostPutDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> acessarAreaEstudo(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(areaEstudoAcessarService.acessarAreaEstudo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAreaEstudo(@PathVariable Long id,
                                                @RequestBody @Valid AreaEstudoPostPutDto areaEstudoPostPutDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(areaEstudoAtualizarService.atualizarAreaEstudo(id, areaEstudoPostPutDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerAreaEstudo(@PathVariable Long id) {
        areaEstudoRemoverService.removerAreaEstudo(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }
}
