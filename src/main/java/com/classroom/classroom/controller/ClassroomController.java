package com.classroom.classroom.controller;

import com.classroom.classroom.DTO.AlunoQueConcluiuAtividadeDto;
import com.classroom.classroom.DTO.AlunoRankingDto;
import com.classroom.classroom.model.Atividade;
import com.classroom.classroom.service.AuthService;
import com.classroom.classroom.service.ClassroomService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.ListCoursesResponse;
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
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private AuthService authService;

    @GetMapping("/courses")
    public ResponseEntity<?> listCourses() throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.classroomService.listCourses());
    }

    //pega as infos de um curso
    @GetMapping("/coursework/{courseId}")
    public ResponseEntity<List<Atividade>> listCourseWork(@PathVariable String courseId) throws GeneralSecurityException, IOException {
        System.err.println("coursework");
        List<Atividade> courseWorks = classroomService.listCourseWork(courseId);
        return ResponseEntity.ok(courseWorks);
    }

    @GetMapping("/fullCoursework/{courseId}")
    public ResponseEntity<List<CourseWork>> listarAtividadesCompletas(@PathVariable String courseId) throws GeneralSecurityException, IOException {
        System.err.println("coursework");
        List<CourseWork> courseWorks = classroomService.listarAtividadeCompleta(courseId);
        return ResponseEntity.ok(courseWorks);
    }

    @GetMapping("/submissions/{courseId}/{assignmentId}")
    public ResponseEntity<List<AlunoQueConcluiuAtividadeDto>> listStudentSubmissions(@PathVariable String courseId, @PathVariable String assignmentId) throws GeneralSecurityException, IOException {
        List<AlunoQueConcluiuAtividadeDto> submissions = classroomService.listStudentSubmissions(courseId, assignmentId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/ranking/{courseId}")
    public ResponseEntity<List<AlunoRankingDto>> obterRankingAlunos(@PathVariable String courseId) throws GeneralSecurityException, IOException {
// Use scopes apropriados para alunos
        List<AlunoRankingDto> ranking = classroomService.obterRankingAlunos(courseId);

        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/courses/{courseId}/badges")
    public ResponseEntity<Map<String, String>> getBadges(@PathVariable String courseId) {
        try {
            Map<String, String> badges = classroomService.atribuirBadgeParaAlunos(courseId);
            return ResponseEntity.ok(badges);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }


}
