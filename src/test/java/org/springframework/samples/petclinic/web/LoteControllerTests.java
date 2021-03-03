package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.forms.LoteForm;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.repository.springdatajpa.TiposGanadoRepository;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.LoteService;
import org.springframework.samples.petclinic.service.TipoGanadoServiceTests;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(
		controllers=LoteController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
	/*Controlador a instanciar para probarlo*/	
	/*Excluir los componentes de seguridad al instanciar el controlador*/
	/*PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios), 
	 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
	 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios*/
public class LoteControllerTests {
	
	@Autowired
	private MockMvc mockMvc;	
	
	@MockBean
	private LoteService			loteService;

	@MockBean
	private AnimalService				animalService;

	@MockBean
	private ExplotacionGanaderaService	expGanaderaService;

	@MockBean
	private TiposGanadoRepository		tiposGanadoRepository;
	
	/* Varible de utilidad para consultar los tipos de ganado */
	private static Map<String, TiposGanado> tiposGanadoMap = TipoGanadoServiceTests.defaultTiposGanado();
	
	final Integer EXP_GANADERA_ID = 1;
	final Integer GANADO_ID = 1;
	
	@WithMockUser(value="spring")
	@Test
	void testCrearLoteGET() throws Exception{
		
		mockMvc.perform(get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/lote/new",EXP_GANADERA_ID,GANADO_ID))
		/*1. Comprobamos que se recibe una respuesta HTTP correcta*/
		.andExpect(status().isOk())
		/*2. Comprobamos el nombre de la vista*/
		.andExpect(view().name("lote/editLote"))
		/*3. Comprobamos que recibimos un modelAttribute Veterinario*/
		.andExpect(model().attributeExists("loteForm"))
		/*4. Comprobamos que Veterinario tiene las propiedades esperadas*/
		.andExpect(model().attribute("loteForm", hasProperty("identificadorLote", blankOrNullString()) ))
		.andExpect(model().attribute("loteForm", hasProperty("numeroMachos", blankOrNullString()) ) )
		.andExpect(model().attribute("loteForm", hasProperty("numeroHembras", blankOrNullString())) )
		.andExpect(model().attribute("loteForm", hasProperty("fechaNacimiento", blankOrNullString())) )
		.andExpect(model().attribute("loteForm", hasProperty("fechaIdentificacion", blankOrNullString())) )
		.andExpect(model().attribute("loteForm", hasProperty("comprado", blankOrNullString())) )
		.andExpect(model().attribute("loteForm", hasProperty("procedencia", blankOrNullString())) )
		.andExpect(model().attribute("loteForm", hasProperty("fechaEntrada", blankOrNullString())) );		
	}
	
	private static Stream<Arguments> testsPositivosCrearLoteData(){
		Stream<Arguments> res;
		
		MultiValueMap<String, String> loteParamsPos1 = loteParams("L028762873", "3", "5", "2020/02/01", "2020/02/15", null, null, null);
		MultiValueMap<String, String> loteParamsPos2 = loteParams("01982374010192", "10", "3", "2016/02/01", "2016/03/15", null, null, null);
		MultiValueMap<String, String> loteParamsPos3 = loteParams("LOJEHSMNND", "2", "8", "2017/11/01", "2017/12/01", null, null, null);
		MultiValueMap<String, String> loteParamsPos4 = loteParams("LOJEHSMNND", "2", "8", "2017/11/01", "2017/12/01", "true", "Alemania", "2018/01/18");
		
		res = Stream.of(
				Arguments.of(loteParamsPos1, 1,tiposGanadoMap.get("Porcino")),
				Arguments.of(loteParamsPos2, 2,tiposGanadoMap.get("Ovino")),
				Arguments.of(loteParamsPos3, 3,tiposGanadoMap.get("Caprino")),
				Arguments.of(loteParamsPos4, 3,tiposGanadoMap.get("Equino")));
		return res;
	}
	
	@WithMockUser(value="spring")
	@ParameterizedTest()
	@MethodSource("testsPositivosCrearLoteData")
	void testsPositivosCrearLotePOST(MultiValueMap<String, String> loteParams, Integer expGanaderaId, 
			TiposGanado ganado) throws Exception {
		/*1. Arrange
		 *	Preparamos la peticion POST con los parametros correspondientes*/
		MockHttpServletRequestBuilder postRequest = post("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/lote/new",
				expGanaderaId,
				ganado.getId())
				.with(csrf())
				.params(loteParams);
		/*Hacemos stubs necesarios*/
		ExplotacionGanadera explotacion = new ExplotacionGanadera();
		explotacion.setId(expGanaderaId);
		Optional<ExplotacionGanadera> explotacionOptional = Optional.of(explotacion);
		when(expGanaderaService.findExpGanaderaById(expGanaderaId)).thenReturn(explotacionOptional);
		
		Optional<TiposGanado> ganadoOptional = Optional.of(tiposGanadoMap.get(ganado.getTipoGanado()));
		when(tiposGanadoRepository.findById(ganado.getId())).thenReturn(ganadoOptional);
		/*2. Act
		*	Enviamos la peticion POST*/
		ResultActions postResultActions = mockMvc.perform(postRequest)
				/*Comprobamos que se recibe una respuesta HTTP correcta 
				 * y se devuelve a la vista correcta*/
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/explotacion-ganadera/"+expGanaderaId+"/ganado/"+ganado.getId()+"/animal/esArchivado/false/list"));
		
		/*3.Assert
		 *	Comprobamos que no se devuelven errores*/
		ArgumentCaptor<Lote> argument = ArgumentCaptor.forClass(Lote.class);
		verify(loteService).saveLote(argument.capture());
		Assertions.assertThat(argument.getValue().getIdentificadorLote()).isEqualTo(loteParams.getFirst("identificadorLote"));
		Assertions.assertThat(argument.getValue().getExplotacionGanadera().getId()).isEqualTo(expGanaderaId);
		Assertions.assertThat(argument.getValue().getTipoGanado().getId()).isEqualTo(ganado.getId());
		
		ArgumentCaptor<Lote> argumentLote = ArgumentCaptor.forClass(Lote.class);
		ArgumentCaptor<LoteForm> argumentLoteForm = ArgumentCaptor.forClass(LoteForm.class);
		verify(animalService).saveAnimalesLote(argumentLote.capture(),argumentLoteForm.capture());
		Assertions.assertThat(argumentLote.getValue()).isEqualTo(argument.getValue());
		Assertions.assertThat(argumentLoteForm.getValue().getIdentificadorLote()).isEqualTo(loteParams.getFirst("identificadorLote"));
		Assertions.assertThat(argumentLoteForm.getValue().getNumeroMachos()).isEqualTo(new Integer(loteParams.getFirst("numeroMachos")));
		Assertions.assertThat(argumentLoteForm.getValue().getNumeroHembras()).isEqualTo(new Integer(loteParams.getFirst("numeroHembras")));
	}
	
	private static MultiValueMap<String, String> loteParams(String identificadorLote, String machos, String hembras,
			String fechaNacimiento, String fechaIdentificacion, String comprado, String procedencia, String fechaEntrada){
		MultiValueMap<String, String> res = new LinkedMultiValueMap<String, String>();
		
		res.add("identificadorLote", identificadorLote);
		res.add("numeroMachos", machos);
		res.add("numeroHembras", hembras);
		res.add("fechaIdentificacion", fechaIdentificacion);
		res.add("fechaNacimiento", fechaNacimiento);
		if(comprado!=null) {
			res.add("comprado", comprado.toString());	
		}
		
		res.add("procedencia", procedencia);
		res.add("fechaEntrada", fechaEntrada);
		
		return res;
	}
	
	


}
