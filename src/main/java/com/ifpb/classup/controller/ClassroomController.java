package com.ifpb.classup.controller;

import com.google.api.services.classroom.model.Course;
import com.ifpb.classup.DTO.AlunoQueConcluiuAtividadeDto;
import com.ifpb.classup.DTO.AlunoRankingDto;
import com.ifpb.classup.DTO.AtividadeRequestDto;
import com.ifpb.classup.model.Atividade;
import com.ifpb.classup.service.AuthService;
import com.ifpb.classup.service.ClassroomService;
import com.google.api.services.classroom.model.CourseWork;
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
@RequestMapping("/classroom")
@CrossOrigin(origins = "http://localhost:4200")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private AuthService authService;

    @GetMapping("/courses")
    public ResponseEntity<?> listCourses(@RequestBody String accessToken) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.classroomService.listCourses(accessToken));
    }

    //pega as infos de um curso
    @GetMapping("/coursework/{courseId}")
    public ResponseEntity<List<Atividade>> listCourseWork(@PathVariable String courseId, @RequestBody String accessToken) throws GeneralSecurityException, IOException {
        System.err.println("coursework");
        List<Atividade> courseWorks = classroomService.listCourseWork(courseId, accessToken);
        return ResponseEntity.ok(courseWorks);
    }

    @PostMapping("/coursework/{courseId}")
    public ResponseEntity<CourseWork> criarAtividade(@PathVariable String courseId,
                                                     @RequestBody AtividadeRequestDto atividadeRequestDto,
                                                     @RequestBody String accessToken) throws GeneralSecurityException, IOException {
        CourseWork courseWork = classroomService.criarAtividade(courseId, atividadeRequestDto, accessToken);
        System.err.println(courseWork);
        return ResponseEntity.ok(courseWork);
    }

    @DeleteMapping("/coursework/{courseId}/{atividadeId}")
    public ResponseEntity<String> deletarAtividade(@PathVariable String courseId, @PathVariable String atividadeId, @RequestBody String accessToken) throws GeneralSecurityException, IOException {
        String courseWork = classroomService.deletarAtividade(courseId, atividadeId, accessToken);
        System.err.println(courseWork);
        return ResponseEntity.ok(courseWork);
    }

    @GetMapping("/fullCoursework/{courseId}")
    public ResponseEntity<List<CourseWork>> listarAtividadesCompletas(@PathVariable String courseId, @RequestBody String accessToken) throws GeneralSecurityException, IOException {
        System.err.println("coursework");
        List<CourseWork> courseWorks = classroomService.listarAtividadeCompleta(courseId, accessToken);
        return ResponseEntity.ok(courseWorks);
    }

    @GetMapping("/submissions/{courseId}/{assignmentId}")
    public ResponseEntity<List<AlunoQueConcluiuAtividadeDto>> listStudentSubmissions(@PathVariable String courseId, @PathVariable String assignmentId, @RequestBody String accessToken) throws GeneralSecurityException, IOException {
        List<AlunoQueConcluiuAtividadeDto> submissions = classroomService.listStudentSubmissions(courseId, assignmentId, accessToken);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/ranking/{courseId}")
    public ResponseEntity<List<AlunoRankingDto>> obterRankingAlunos(@PathVariable String courseId, @RequestBody String accessToken) throws GeneralSecurityException, IOException {
// Use scopes apropriados para alunos
        List<AlunoRankingDto> ranking = classroomService.obterRankingAlunos(courseId, accessToken);

        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/courses/{courseId}/badges")
    public ResponseEntity<Map<String, String>> getBadges(@PathVariable String courseId, @RequestBody String accessToken) {
        try {
            Map<String, String> badges = classroomService.atribuirBadgeParaAlunos(courseId, accessToken);
            return ResponseEntity.ok(badges);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }


}
