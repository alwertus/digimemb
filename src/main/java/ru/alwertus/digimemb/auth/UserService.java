package ru.alwertus.digimemb.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
@Transactional // без него при удалении падает ошибка
public class UserService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final Set<Role> defaultRole;

    @Autowired
    public UserService(BCryptPasswordEncoder passwordEncoder, RoleRepo roleRepo, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        defaultRole = new HashSet<>();

        defaultRole.add(getRole("ROLE_ANONYMOUS"));
    }

    // получить роль.
    // создать и сохранить в БД, если её нет
    private Role getRole(String name) {
        Role findRole = roleRepo.findByName(name).orElse(new Role(name));
        roleRepo.save(findRole);
        return findRole;
    }

    // Создать нового пользователя
    public User createUser(String name, String password) {
        log.info("Add user: " + name);
        User newUser = userRepo.findByName(name).orElse(new User(name, passwordEncoder.encode(password), defaultRole));
        userRepo.save(newUser);
        return newUser;
    }

    public void deleteUser(String name) {
        log.info("Delete user: " + name);
        Optional<User> user = userRepo.findByName(name);
        if (!user.isPresent()) return;
        userRepo.deleteByName(name);
    }

    // Добавить роль к пользователю
    public void addRole(User user, String roleName) {
        log.info("Add role <" + roleName + "> to " + user.getName());
        Set<Role> userRoles = new HashSet<>(user.getRoles());
        userRoles.add(getRole(roleName));
        user.setRoles(userRoles);
        userRepo.save(user);
    }

    // Удалить роль пользователя
    public void removeRole(User user, String roleName) {
        log.info("Remove Role <" + roleName + "> from " + user.getName());
        if (!user.hasRole(roleName))
            return;
        Set<Role> userRoles = new HashSet<>(user.getRoles());
        userRoles.remove(new Role(roleName));
        user.setRoles(userRoles);
        userRepo.save(user);
    }

    // Метод для Spring Security
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("SPRING SECURITY try load user: " + userName);
        Optional<User> oUser = userRepo.findByName(userName);
        if (!oUser.isPresent()) {
            log.error("SPRING SECURITY. User not found");
            throw new UsernameNotFoundException("User not found");
        }
        log.info("SPRING SECURITY. User found success");
        return oUser.get();
    }
}
