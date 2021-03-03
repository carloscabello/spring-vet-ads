
package org.springframework.samples.petclinic.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;


	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/resources/**", "/webjars/**", "/h2-console/**").permitAll().antMatchers(HttpMethod.GET, "/", "/oups").permitAll().antMatchers("/ganadero/new").anonymous().antMatchers("/veterinario/new").anonymous()
			.antMatchers("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/esArchivado/{esArchivado}/list").hasAnyAuthority("veterinario", "ganadero")
			.antMatchers("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/show").hasAnyAuthority("veterinario", "ganadero").antMatchers("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/delete")
			.hasAnyAuthority("veterinario", "ganadero").antMatchers("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/lote/{loteId}/show").hasAnyAuthority("veterinario", "ganadero").antMatchers("/explotacion-ganadera/**")
			.hasAnyAuthority("veterinario").antMatchers("/ganadero/**").hasAnyAuthority("ganadero").antMatchers("/veterinario/{vetId}/contrato").hasAnyAuthority("ganadero").antMatchers("/veterinario/encontrar").hasAnyAuthority("ganadero")
			.antMatchers("/veterinario/{vetId}/show").hasAnyAuthority("ganadero").antMatchers("/veterinario/**").hasAnyAuthority("veterinario").antMatchers("/producto/**").hasAnyAuthority("veterinario").antMatchers("/contrato/{contratoId}/show")
			.hasAnyAuthority("veterinario", "ganadero").antMatchers("/contrato/{contratoId}/conclude").hasAnyAuthority("veterinario", "ganadero").antMatchers("/contrato/{contratoId}/edit").hasAnyAuthority("ganadero").antMatchers("/contrato/**")
			.hasAnyAuthority("veterinario").antMatchers("/factura/select").hasAnyAuthority("veterinario").antMatchers("/receta/{recetaId}/show").hasAnyAuthority("veterinario", "ganadero").antMatchers("/factura/{facturaId}/show")
			.hasAnyAuthority("veterinario", "ganadero")

			.anyRequest().denyAll().and().formLogin()
			/* .loginPage("/login") */
			.failureUrl("/login-error").and().logout().logoutSuccessUrl("/");
		// Configuración para que funcione la consola de administración
		// de la BD H2 (deshabilitar las cabeceras de protección contra
		// ataques de tipo csrf y habilitar los framesets si su contenido
		// se sirve desde esta misma página.
		http.csrf().ignoringAntMatchers("/h2-console/**");
		http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(this.dataSource).usersByUsernameQuery("select username,password,enabled " + "from users " + "where username = ?")
			.authoritiesByUsernameQuery("select username, authority " + "from authorities " + "where username = ?").passwordEncoder(this.passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
		return encoder;
	}

}
