package com.ifpb.classup.controller;

import com.google.api.services.classroom.model.Student;
import com.ifpb.classup.model.Curso;
import com.ifpb.classup.model.Usuario;
import com.ifpb.classup.service.AuthService;
import com.ifpb.classup.service.CursoService;
import com.ifpb.classup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/curso")
@CrossOrigin(origins = "http://localhost:4200")
public class CursoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;


    @PostMapping("")
    public ResponseEntity<Curso> criarCurso(@RequestHeader String accessToken, @RequestBody Curso curso) throws IOException, GeneralSecurityException {
        return ResponseEntity.ok(this.cursoService.criarCurso(accessToken, curso));
    }

    @PostMapping("/associar")
    public ResponseEntity<Curso> associarCurso(@RequestHeader String accessToken, @RequestBody Curso curso) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.cursoService.associarCursoAoSistema(curso, accessToken));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Curso>> listarCursosPorIdProprietario(@RequestHeader String accessToken, @PathVariable String id) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.cursoService.listarCursoByIdProprietario(id, accessToken));
    }

    @GetMapping("")
    public ResponseEntity<List<Curso>> listarTodosOsCursos(@RequestHeader String accessToken) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.cursoService.getAllCursos(accessToken));
    }

    @GetMapping("/{idCurso}/alunos")
    public ResponseEntity<List<Usuario>> listarAlunosDoCurso(@RequestHeader String accessToken, @PathVariable String idCurso) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.cursoService.listarAlunosDoCurso(idCurso, accessToken));
    }


}
