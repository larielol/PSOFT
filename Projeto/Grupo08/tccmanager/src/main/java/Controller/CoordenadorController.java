package tccmanager.tccmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tccmanager.tccmanager.Service.CoordenadorService.CoordenadorGerarRelatorioService;

@RestController
@RequestMapping(value = "/coordenador", produces = MediaType.APPLICATION_JSON_VALUE)
public class CoordenadorController {

    @Autowired
    CoordenadorGerarRelatorioService coordenadorGerarRelatorioService;

    @GetMapping("/relatorio")
    public ResponseEntity<?> gerarRelatorio() {
        String relatorio = coordenadorGerarRelatorioService.gerarRelatorio();
        System.out.println(relatorio);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(relatorio);
    }
}
