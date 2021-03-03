package org.springframework.samples.petclinic.web.utils;

import static org.junit.Assume.assumeFalse;

import java.util.Collection;

/* Esta ese usa para los tests unitarios de controlador.
 * Sirve para ser utilizada como parametro en los tests negativos parametrizados.
 * Se construye en base a:
 * 	- Nombre del atributo
 * 	- Codigo de Error
 * */

public class FieldErrorCount {

	
	private String fieldName;
	private Integer errorCode;
	
	public FieldErrorCount(String fieldName, Integer errorCount) {
		super();
		this.fieldName = fieldName;
		this.errorCode = errorCount;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Integer getErrorCount() {
		return errorCode;
	}
	public void setErrorCount(Integer errorCount) {
		this.errorCode = errorCount;
	}
	
	public static Integer total(Collection<FieldErrorCount> errorCountList) {
		
		Integer res = errorCountList.stream().mapToInt(x -> x.getErrorCount()).sum();
		
		return res;
	} 

}
