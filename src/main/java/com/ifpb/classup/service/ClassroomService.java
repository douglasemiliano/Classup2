package com.ifpb.classup.service;

import com.google.api.client.auth.oauth2.BearerToken;
import com.ifpb.classup.DTO.AlunoQueConcluiuAtividadeDto;
import com.ifpb.classup.DTO.AlunoRankingDto;
import com.ifpb.classup.DTO.AtividadeRequestDto;
import com.ifpb.classup.model.Atividade;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.*;
import com.ifpb.classup.model.Badge;
import com.ifpb.classup.model.Curso;
import com.ifpb.classup.repository.BadgeRepository;
import com.ifpb.classup.utils.BadgesPadrao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.api.services.classroom.model.Date;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class ClassroomService {

    @Autowired
    private AuthService authService;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UsuarioService usuarioService;

    private Classroom initializeClassroomClient(String accessToken) throws GeneralSecurityException, IOException {
        Credential credentials = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(accessToken);

        return new Classroom.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credentials
        ).setApplicationName("Classroom App").build();
    }


    public Course createCourse(String accessToken, Curso curso) throws GeneralSecurityException, IOException {
        Classroom service = initializeClassroomClient(accessToken);

        curso.setIdProprietario(this.usuarioService.getGoogleProfile(accessToken).getId());

        DriveFolder driveFolder = new DriveFolder();
        driveFolder.setTitle(curso.getTitulo());

        Course course = new Course()
                .setName(curso.getTitulo())
                .setTeacherFolder(driveFolder)
                .setSection(curso.getSection())
                .setDescription(curso.getDescricao())
                .setOwnerId(curso.getIdProprietario());
        return service.courses().create(course).execute();
    }


    public List<Course> listCourses(String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            ListCoursesResponse response = service.courses().list().execute();
            service.courses().list().execute();
            return response.getCourses();
        } catch (Exception e) {
            throw new IOException("Erro ao listar cursos: " + e.getMessage(), e);
        }
    }

    public CourseWork criarAtividade(String courseId, AtividadeRequestDto atividadeRequestDto, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            CourseWork courseWork = new CourseWork()
                    .setTitle(atividadeRequestDto.getTitulo())
                    .setDescription(atividadeRequestDto.getDescricao())
                    .setMaxPoints(atividadeRequestDto.getPontuacaoMaxima())
                    .setWorkType("ASSIGNMENT") // Tipo de trabalho (ex: tarefa)
                    .setState("PUBLISHED")    // Estado (ex: publicado)
                    .setDueDate(new Date()
                            .setYear(atividadeRequestDto.getAno())
                            .setMonth(atividadeRequestDto.getMes())
                            .setDay(atividadeRequestDto.getDia()))
                    .setDueTime(new TimeOfDay()
                            .setHours(atividadeRequestDto.getHoras())
                            .setMinutes(atividadeRequestDto.getMinutos()))
                    .setMaterials(Collections.emptyList()) // Materiais (se houver)
                    .setCourseId(courseId);

            return service.courses().courseWork().create(courseId, courseWork).execute();
        } catch (Exception e) {
                throw new IOException("Erro ao listar cursos: " + e.getMessage(), e);
        }
    }

    public String deletarAtividade(String courseId, String atividadeId, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            service.courses().courseWork().delete(courseId, atividadeId).execute();
            return "Atividade excluida com sucesso!";
        } catch (Exception e) {
            throw new IOException("Erro ao listar cursos: " + e.getMessage(), e);
        }
    }

    public List<Atividade> listCourseWork(String courseId, String accessToken) throws GeneralSecurityException, IOException {

        try {
            Classroom service = initializeClassroomClient(accessToken);
            ListCourseWorkResponse courseWorkResponse = service.courses().courseWork().list(courseId).execute();

            System.err.println(courseWorkResponse);

            List<Atividade> atividades = new ArrayList<>();
            if (courseWorkResponse.getCourseWork() != null) {
                for (CourseWork courseWork : courseWorkResponse.getCourseWork()) {
                    Atividade atividade = new Atividade();
                    atividade.setTitulo(courseWork.getTitle());
                    atividade.setDescricao(courseWork.getDescription());
                    atividade.setId(courseWork.getId());
                    atividade.setIdCurso(courseWork.getCourseId());
                    atividade.setPontuacaoMaxima(courseWork.getMaxPoints());
                    atividade.setStatus(courseWork.getState());
                    atividade.setUltimaModificacao(courseWork.getUpdateTime());
                    atividade.setLink(courseWork.getAlternateLink());
                    atividades.add(atividade);
                }
            }

            return atividades;
        } catch (Exception e) {
            throw new IOException("Erro ao listar atividades: " + e.getMessage(), e);
        }
    }

    public List<CourseWork> listarAtividadeCompleta(String courseId, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            ListCourseWorkResponse courseWorkResponse = service.courses().courseWork().list(courseId).execute();

            return courseWorkResponse.getCourseWork();
        } catch (Exception e) {
            throw new IOException("Erro ao listar atividades: " + e.getMessage(), e);
        }
    }

    public List<CourseWork> listarAtividadesComStatus(String courseId, String userId, String accessToken) throws GeneralSecurityException, IOException {
        Classroom service = initializeClassroomClient(accessToken);

        List<CourseWork> response = new ArrayList<>();

        List<CourseWork> courseWorkResponse = service.courses().courseWork().list(courseId).execute().getCourseWork();

        for (CourseWork courseWork : courseWorkResponse) {
           List<StudentSubmission> submissions = service.courses().courseWork().studentSubmissions().list(courseId, courseWork.getId()).execute().getStudentSubmissions();
           submissions.stream()
                   .filter(studentSubmission -> studentSubmission.getUserId().equals(userId))
                   .forEach(submission -> {
               System.err.println(submission.getUserId());
                   courseWork.setState(submission.getState());
                   response.add(courseWork);

               System.err.println(submission.getState());
           });

        }


        return response;
    }

    public List<Student> listStudents(String courseId, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            ListStudentsResponse response = service.courses().students().list(courseId).execute();
            List<Student> alunos = service.courses().students().list(courseId).execute().getStudents();
            System.err.println("alinoooos - - -  " + alunos);
            System.err.println(response);
            return response.getStudents();
        } catch (Exception e) {
            throw new IOException("Erro ao listar alunos: " + e.getMessage(), e);
        }
    }

    public List<AlunoQueConcluiuAtividadeDto> listStudentSubmissions(String courseId, String assignmentId, String accessToken)
            throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            ListStudentSubmissionsResponse response = service.courses().courseWork().studentSubmissions()
                    .list(courseId, assignmentId).execute();

            List<AlunoQueConcluiuAtividadeDto> alunos = new ArrayList<>();
            for (StudentSubmission submission : response.getStudentSubmissions()) {
                AlunoQueConcluiuAtividadeDto aluno = new AlunoQueConcluiuAtividadeDto();
                aluno.setId(submission.getUserId());
                aluno.setIdCurso(submission.getCourseId());
                aluno.setIdAtividade(submission.getCourseWorkId());
                aluno.setLink(submission.getAlternateLink());
                aluno.setStatus(submission.getState());
                aluno.setPontuacao(submission.getDraftGrade());
                aluno.setUltimaModificacao(submission.getUpdateTime());
                aluno.setDataCriacao(submission.getCreationTime());

                try {
                    UserProfile profile = service.userProfiles().get(submission.getUserId()).execute();
                    aluno.setNome(profile.getName() != null ? profile.getName().getFullName() : "Aluno");
                } catch (IOException e) {
                    System.err.println("Erro ao buscar perfil do aluno: " + e.getMessage());
                    aluno.setNome("Aluno");
                }

                alunos.add(aluno);
            }

            return alunos;
        } catch (Exception e) {
            throw new IOException("Erro ao listar alunos que concluíram a atividade: " + e.getMessage(), e);
        }
    }

    public List<AlunoRankingDto> obterRankingAlunos(String courseId, String accessToken) throws IOException, GeneralSecurityException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            Map<String, Double> pontuacoesAlunos = new HashMap<>();
            List<CourseWork> atividades = listarAtividadeCompleta(courseId, accessToken);

            for (CourseWork atividade : atividades) {
                ListStudentSubmissionsResponse response = service.courses().courseWork().studentSubmissions()
                        .list(courseId, atividade.getId()).execute();

                for (StudentSubmission submissao : response.getStudentSubmissions()) {
                    String alunoId = submissao.getUserId();
                    double pontuacao = obterPontuacao(submissao);
                    pontuacoesAlunos.merge(alunoId, pontuacao, Double::sum);
                }
            }

            List<AlunoRankingDto> ranking = new ArrayList<>();
            for (Map.Entry<String, Double> entry : pontuacoesAlunos.entrySet()) {
                String alunoId = entry.getKey();
                UserProfile perfilAluno = service.userProfiles().get(alunoId).execute();
                ranking.add(new AlunoRankingDto(alunoId, perfilAluno.getName().getFullName(), entry.getValue(), 0));
            }

            ranking.sort(Comparator.comparingDouble(AlunoRankingDto::getPontuacaoTotal).reversed());
            return ranking;
        } catch (Exception e) {
            throw new IOException("Erro ao obter ranking de alunos: " + e.getMessage(), e);
        }
    }

    private double obterPontuacao(StudentSubmission submissao) {
        Double grade = submissao.getAssignedGrade() != null ? submissao.getAssignedGrade() : submissao.getDraftGrade();
        return grade != null ? grade : 0.0;
    }

//    public Map<String, String> atribuirBadgeParaAlunos(String courseId) throws IOException, GeneralSecurityException {
//        try {
//            Classroom service = initializeClassroomClient();
//            Map<String, Integer> atividadesConcluidasPorAluno = new HashMap<>();
//            List<CourseWork> atividades = listarAtividadeCompleta(courseId);
//
//
//            // Contar atividades concluídas por cada aluno
//            for (CourseWork atividade : atividades) {
//                ListStudentSubmissionsResponse response = service.courses().courseWork().studentSubmissions()
//                        .list(courseId, atividade.getId()).execute();
//
//                System.err.println("atividade - " + atividade);
//
//                for (StudentSubmission submissao : response.getStudentSubmissions()) {
//
//                    System.err.println("sub - " + submissao);
//
//                    if ("TURNED_IN".equals(submissao.getState()) || "RETURNED".equals(submissao.getState())) { // Verifica se o aluno entregou a atividade
//                        String alunoId = submissao.getUserId();
//                        atividadesConcluidasPorAluno.merge(alunoId, 1, Integer::sum);
//                    }
//                }
//            }
//
//            System.err.println(atividades);
//            System.err.println(atividadesConcluidasPorAluno);
//
//            // Atribuir badges para alunos com pelo menos 2 atividades concluídas
//            Map<String, String> badges = new HashMap<>();
//            for (Map.Entry<String, Integer> entry : atividadesConcluidasPorAluno.entrySet()) {
//                if (entry.getValue() >= 2) {
//                    UserProfile perfilAluno = service.userProfiles().get(entry.getKey()).execute();
//                    String nomeAluno = perfilAluno.getName().getFullName();
//                    badges.put(entry.getKey(), "Badge concedida para " + nomeAluno);
//                }
//            }
//
//            return badges;
//        } catch (Exception e) {
//            throw new IOException("Erro ao atribuir badges: " + e.getMessage(), e);
//        }
//    }


    public Map<String, String> atribuirBadgeParaAlunos(String courseId, String accessToken) throws IOException, GeneralSecurityException {
        Classroom service = initializeClassroomClient(accessToken);
        Map<String, Integer> atividadesConcluidasPorAluno = new HashMap<>();
        List<CourseWork> atividades = listarAtividadeCompleta(courseId, accessToken);

        for (CourseWork atividade : atividades) {
            ListStudentSubmissionsResponse response = service.courses().courseWork().studentSubmissions()
                    .list(courseId, atividade.getId()).execute();

            for (StudentSubmission submissao : response.getStudentSubmissions()) {
                if ("TURNED_IN".equals(submissao.getState()) || "RETURNED".equals(submissao.getState())) {
                    String alunoId = submissao.getUserId();
                    atividadesConcluidasPorAluno.merge(alunoId, 1, Integer::sum);
                }
            }
        }

        Map<String, String> badges = new HashMap<>();

        for (Map.Entry<String, Integer> entry : atividadesConcluidasPorAluno.entrySet()) {
            if (entry.getValue() >= 2) {
                UserProfile perfilAluno = service.userProfiles().get(entry.getKey()).execute();
                String nomeAluno = perfilAluno.getName().getFullName();
                Badge badge = BadgesPadrao.COMPLETAR_DUAS_ATIVIDADES;
                badgeRepository.save(badge); // Salva no MongoDB
                badges.put(entry.getKey(), "Badge concedida para " + nomeAluno);
            }
        }

        return badges;
    }



}
