package com.ifpb.classup.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.*;
import com.ifpb.classup.DTO.AlunoQueConcluiuAtividadeDto;
import com.ifpb.classup.DTO.AlunoRankingDto;
import com.ifpb.classup.model.Atividade;
import com.ifpb.classup.model.Badge;
import com.ifpb.classup.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class BadgeService {


    @Autowired
    private BadgeRepository badgeRepository;


    public Badge criarBadge(Badge badge){
        return badgeRepository.save(badge);
    }


}
