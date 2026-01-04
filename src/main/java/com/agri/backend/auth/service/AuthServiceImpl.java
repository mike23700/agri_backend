package com.agri.backend.auth.service;

import com.agri.backend.auth.dto.LoginRequestDto;
import com.agri.backend.auth.dto.RegisterRequestDto;
import com.agri.backend.user.entity.Role;
import com.agri.backend.user.entity.User;
import com.agri.backend.user.repository.RoleRepository;
import com.agri.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User registerUser(RegisterRequestDto request) {
        // Sécurité : Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Erreur: Le nom d'utilisateur est déjà pris !");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Erreur: L'email est déjà utilisé !");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setTel(request.getTel());
        user.setPassword(request.getPassword()); 

        Set<Role> roles = new HashSet<>();
        String roleSaisi = request.getRole();

        // Logique de sélection de rôle sécurisée contre le null
        if (roleSaisi == null || roleSaisi.isEmpty()) {
            Role buyerRole = roleRepository.findByName("ROLE_ACHETEUR")
                    .orElseThrow(() -> new RuntimeException("Erreur: Rôle ROLE_ACHETEUR non trouvé en base."));
            roles.add(buyerRole);
        } else {
            // Utilisation de equals sur la constante pour éviter NullPointerException
            if ("agri".equalsIgnoreCase(roleSaisi)) {
                Role agriRole = roleRepository.findByName("ROLE_AGRICULTEUR")
                        .orElseThrow(() -> new RuntimeException("Erreur: Rôle ROLE_AGRICULTEUR non trouvé."));
                roles.add(agriRole);
            } else if ("admin".equalsIgnoreCase(roleSaisi)) {
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .orElseThrow(() -> new RuntimeException("Erreur: Rôle ROLE_ADMIN non trouvé."));
                roles.add(adminRole);
            } else {
                Role buyerRole = roleRepository.findByName("ROLE_ACHETEUR")
                        .orElseThrow(() -> new RuntimeException("Erreur: Rôle ROLE_ACHETEUR non trouvé."));
                roles.add(buyerRole);
            }
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User loginUser(LoginRequestDto request) {
        // 1. Chercher l'utilisateur en base
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé en base de données !"));

        // 2. Vérifier le mot de passe (en clair pour le moment)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect !");
        }

        return user; // Si tout est OK, on renvoie l'utilisateur
    }
}