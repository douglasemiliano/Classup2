package com.ifpb.classup.service;

import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.Student;
import com.ifpb.classup.model.Atividade;
import com.ifpb.classup.model.Badge;
import com.ifpb.classup.model.Curso;
import com.ifpb.classup.model.Usuario;
import com.ifpb.classup.parser.CourseToCursoParser;
import com.ifpb.classup.parser.CourseWorkToAtividadeParser;
import com.ifpb.classup.repository.BadgeRepository;
import com.ifpb.classup.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {


    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private UsuarioService usuarioService;


    public Curso criarCurso(String accessToken, Curso curso) throws GeneralSecurityException, IOException {
        curso.setIdProprietario(this.usuarioService.getGoogleProfile(accessToken).getId());

        Course cursoCriado = this.classroomService.createCourse(accessToken, curso);

        return CourseToCursoParser.parseToCurso(cursoCriado, true);
    }

    public Curso associarCursoAoSistema(Curso curso, String accessToken) throws GeneralSecurityException, IOException {
        List<Usuario> alunos = this.listarAlunosDoCurso(curso.getId() ,accessToken);
        curso.setAlunos(alunos);
        return cursoRepository.save(curso);
    }
    public List<Curso> getCursoByIdProprietario(String id){
        return this.cursoRepository.findByIdProprietario(id);
    }

    public List<Curso> listarCursoByIdProprietario(String id, String accessToken) throws GeneralSecurityException, IOException {
        List<Course> cursos = this.classroomService.listCourses(accessToken);
        return cursos.stream()
                .map(course -> CourseToCursoParser.parseToCurso(course, false))
                .filter(curso -> curso.getIdProprietario().equals(id))
                .toList();
    }

    public List<Curso> getAllCursos(String accessToken) throws GeneralSecurityException, IOException {
        List<Course> cursos = this.classroomService.listCourses(accessToken);
        System.err.println(cursos);
        return cursos.stream()
                .map(course -> CourseToCursoParser.parseToCurso(course, false))
                .toList();
    }

    public List<Atividade> listarAtividadeDoCurso(String idCurso, String accessToken) throws IOException, GeneralSecurityException {

        List<CourseWork> atividades = this.classroomService.listarAtividadeCompleta(idCurso, accessToken);

        System.err.println(atividades);

        return  atividades.stream()
                .map(CourseWorkToAtividadeParser::parseToAtividade)
                .toList();

    }

    public List<Usuario> listarAlunosDoCurso(String idCurso, String accessToken) throws IOException, GeneralSecurityException {

        List<Student> alunos = this.classroomService.listStudents(idCurso, accessToken);

       alunos.forEach(aluno -> System.err.println(aluno.getUserId()));

        return alunos.stream()
                .map(aluno -> this.usuarioService.encontrarUsuarioPorId(aluno.getUserId()))
                .toList();

    }



}
