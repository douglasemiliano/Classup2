package com.ifpb.classup.repository;
import com.ifpb.classup.model.Badge;
import com.ifpb.classup.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}
