package com.ifpb.classup.controller;

import com.ifpb.classup.model.Atividade;
import com.ifpb.classup.model.Curso;
import com.ifpb.classup.service.ClassroomService;
import com.ifpb.classup.service.CursoService;
import com.ifpb.classup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/atividade")
@CrossOrigin(origins = "http://localhost:4200")
public class AtividadeController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private ClassroomService classroomService;


    @GetMapping("/{idCurso}")
    public ResponseEntity<List<Atividade>> listarAtividadeDoCurso(@RequestHeader String accessToken, @PathVariable String idCurso) throws IOException, GeneralSecurityException {
        List<Atividade> atividades = cursoService.listarAtividadeDoCurso(idCurso, accessToken);
        return ResponseEntity.ok(atividades);
    }

    @GetMapping("")
    public ResponseEntity<List<Curso>> listarTodosOsCursos(@RequestHeader String accessToken) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.cursoService.getAllCursos(accessToken));
    }


}
