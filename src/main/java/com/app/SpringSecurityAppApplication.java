package com.app;

import com.app.entities.PermissionEntity;
import com.app.entities.RoleEntity;
import com.app.entities.UserEntity;
import com.app.enums.RoleEnum;
import com.app.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringSecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}

	// Despues de iniciar la aplicacion vamos a crear los roles y permisos de los usuarios
	@Bean
	CommandLineRunner initCommandLineRunner(UserRepository userRepository){
		return args -> {
			// Crear permisos
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();
			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();
			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();
			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();
			PermissionEntity refactorPermission = PermissionEntity.builder()
					.name("REFACTOR")
					.build();

			//	Crear roles y asignarlees sus permisos de cada rol
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();
			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissions(Set.of(createPermission, readPermission))
					.build();

			RoleEntity roleInvited = RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissions(Set.of(readPermission))
					.build();

			RoleEntity roleDeveloper = RoleEntity.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
					.build();

			// Crear los usuarios
			UserEntity userDaniel = UserEntity.builder()
					.username("daniel")
					.password("$2a$10$uIfG9LJJnSJLN3FZ8RprqekP7EXLcNGDnIpSd09V1OiFUzH8o0cMe")
					.isEnabled(true)
					.isCredentialsNonExpired(true)
					.isAccountNonLocked(true)
					.isAccountNonExpired(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userPedro = UserEntity.builder()
					.username("pedro")
					.password("$2a$10$uIfG9LJJnSJLN3FZ8RprqekP7EXLcNGDnIpSd09V1OiFUzH8o0cMe")
					.isEnabled(true)
					.isCredentialsNonExpired(true)
					.isAccountNonLocked(true)
					.isAccountNonExpired(true)
					.roles(Set.of(roleUser))
					.build();

			UserEntity userPablo = UserEntity.builder()
					.username("pablo")
					.password("$2a$10$uIfG9LJJnSJLN3FZ8RprqekP7EXLcNGDnIpSd09V1OiFUzH8o0cMe")
					.isEnabled(true)
					.isCredentialsNonExpired(true)
					.isAccountNonLocked(true)
					.isAccountNonExpired(true)
					.roles(Set.of(roleInvited))
					.build();

			UserEntity userEmilio = UserEntity.builder()
					.username("emilio")
					.password("$2a$10$uIfG9LJJnSJLN3FZ8RprqekP7EXLcNGDnIpSd09V1OiFUzH8o0cMe")
					.isEnabled(true)
					.isCredentialsNonExpired(true)
					.isAccountNonLocked(true)
					.isAccountNonExpired(true)
					.roles(Set.of(roleDeveloper))
					.build();

			userRepository.saveAll(List.of(userDaniel,userPedro,userPablo,userEmilio));

		};
	}
}
