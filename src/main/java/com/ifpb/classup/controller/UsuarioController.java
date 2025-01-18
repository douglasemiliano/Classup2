package com.ifpb.classup.controller;

import com.google.api.services.classroom.model.CourseWork;
import com.ifpb.classup.DTO.AlunoQueConcluiuAtividadeDto;
import com.ifpb.classup.DTO.AlunoRankingDto;
import com.ifpb.classup.model.Atividade;
import com.ifpb.classup.model.Usuario;
import com.ifpb.classup.service.AuthService;
import com.ifpb.classup.service.ClassroomService;
import com.ifpb.classup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthService authService;

    @GetMapping("")
    public ResponseEntity<Usuario> listCourses(@RequestBody String accessToken) throws IOException {
        return ResponseEntity.ok(this.usuarioService.getGoogleProfile(accessToken));
    }





}
