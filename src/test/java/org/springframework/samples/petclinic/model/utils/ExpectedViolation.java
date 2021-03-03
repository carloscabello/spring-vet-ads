
package org.springframework.samples.petclinic.model.utils;

import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;

import org.springframework.util.Assert;

public class ExpectedViolation {

	private String	properyPath;
	private String	message;
	private String	messageTemplate;


	public ExpectedViolation(final String properyPath, final String message, final String messageTemplate) {
		super();
		this.properyPath = properyPath;
		this.message = message;
		this.messageTemplate = messageTemplate;
	}

	public String getProperyPath() {
		return this.properyPath;
	}

	public String getMessage() {
		return this.message;
	}

	public String getMessageTemplate() {
		return this.messageTemplate;
	}
	
	
	/*getCorrespondingViolation
	 * <T> es un tipo generico, equivale a tipo de la entidad con la que estemos trabajando
	 * 
	 * Inputs: 
	 * 			- Map<String,List<ExpectedViolation>> expectedConstraintViolations
	 * 			Un map, cuyas keys son el propertyPath de la constraint
	 * 			Los values son objetos ExpectedViolation (De esta misma clase)
	 * 
	 * 			- ConstraintViolation<T> violation
	 * 			Una de las constraints que se devuelven al validar cierto objeto del modelo del tipo <T>
	 * 
	 * Uso: Sirve para devolver la ExpectedViolation correspondiente a una ConstraintViolation<T> que se recibe como
	 * parametro. Para ello, debe coincidir el propertyPath y que el message de la ExpectedViolation este contenido en el mensaje de error de la constraint
	 * 
	 * Si no se encuentra la constraint correspondiente, salta el Assert.notNull */
	public static <T> ExpectedViolation getCorrespondingExpectedViolation(Map<String,List<ExpectedViolation>> expectedConstraintViolations, ConstraintViolation<T> violation) {
		List<ExpectedViolation> propertyExpectedViolations = expectedConstraintViolations.get(violation.getPropertyPath().toString());
		if(propertyExpectedViolations==null) {
			System.out.println("============ ExpectedViolation.getCorrespondingExpectedViolation() ===========");
			System.out.println("      The property with path "+ violation.getRootBeanClass().getSimpleName()+"."+violation.getPropertyPath() +" had no ExpectedViolations");
			System.out.println("      but a ConstraintViolation was received");
			System.out.println("ConstraintViolation: ");
			System.out.println("- PropertyPath: "+violation.getPropertyPath());
			System.out.println("- Message: "+violation.getMessage());
			System.out.println("- MessageTemplate: "+violation.getMessageTemplate());
			System.out.println("===================");}
		Assert.notNull(propertyExpectedViolations, "ConstraintViolation was not found in expected in Map<String,List<ExpectedViolation>>");
		ExpectedViolation res = null;
		for(ExpectedViolation iteratorExpectedViolation: propertyExpectedViolations) {
			if(iteratorExpectedViolation.getProperyPath().equals(violation.getPropertyPath().toString())
					&& violation.getMessage().toString().contains(iteratorExpectedViolation.getMessage())){
				res = iteratorExpectedViolation;
			}
		}
		
		if(res==null) {
			System.out.println("============ ExpectedViolation.getCorrespondingExpectedViolation() ===========");
			System.out.println("      The corresponding ExpectedViolation for path "+ violation.getPropertyPath() +" was not found.");
			System.out.println("ConstraintViolation: ");
			System.out.println("- PropertyPath: "+violation.getPropertyPath());
			System.out.println("- Message: "+violation.getMessage());
			System.out.println("- MessageTemplate: "+violation.getMessageTemplate());
			System.out.println("===================");}
		Assert.notNull(res, "The corresponding ConstraintViolation was not found in expected in Map<String,List<ExpectedViolation>>");
		return res;
	}
	
	

}
