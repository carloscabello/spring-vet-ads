package org.springframework.samples.petclinic.web.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.util.MultiValueMap;

public class EntityListSerializer {
	
	/*Converts a List<BaseEntity>, or any subclass that extends it, 
	 * into a string so it can be send to the controller
	 * as plain text
	 * - Carlos */
	public static String listToString(List<? extends BaseEntity> list) {
		String res;
		
		res = list.stream().map(tipo->tipo.getId().toString()).collect(Collectors.joining(", ", "", ""));
		
		return res;
	}
	
	public static void addList(MultiValueMap<String, String> res ,List<? extends BaseEntity> list) {
		
		//res = list.stream().map(tipo->tipo.getId().toString()).collect(Collectors.joining(", ", "", ""));
	}
	
	

}
