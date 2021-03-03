<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type ="text/javascript">
	function explotacionGanaderaOnChange() {
		var selectElement = document.getElementById('select-contrato-cita-form-explotacionGanadera');
		window.explotacionClass = selectElement.options[selectElement.selectedIndex].className;
		Array.from(document.querySelector("#select-contrato-cita-form-contrato").options).forEach(mostrarOcultarContratoOption);
	}
	function mostrarOcultarContratoOption(element, index, array){
		var option = element;
		var explotacionClass = window.explotacionClass;
		var enabledContracts = 0;
		if(option.className == explotacionClass){
			option.disabled = false;
			option.style.display="block";
			enabledContracts ++;
			if(enabledContracts == 1){
				option.selected = true;
			}
		
		}else if(option.className == "" && document.getElementsByClassName(explotacionClass).length <=1){
			option.disabled = true;
			option.selected = true;
			option.style.display="block";
		}else{
			option.disabled = true;
			option.selected = false;
			option.style.display="none";
		}
		var selectContratoElement = document.getElementById('select-contrato-cita-form-contrato');
		var formButton = document.getElementById('select-contrato-cita-form-button');
		if(enabledContracts <=0){
			selectContratoElement.disabled=true;
			formButton.disabled=true;
		}else{
			selectContratoElement.disabled=false;
			formButton.disabled=false;
		}
	}
</script>

<form:form modelAttribute="selectCreateCitaForm" class="form-horizontal" id="select-contrato-cita-form">
        <div class="form-group has-feedback">
			<spring:bind path="explotacionGanadera">
				<c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
				<c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
			    <div class="${cssGroup}">
			        <label class="col-sm-6 control-label">Explotación Ganadera: </label>
			        <div class="col-sm-6">
			            <select name="explotacionGanadera" id="select-contrato-cita-form-explotacionGanadera" onchange="explotacionGanaderaOnChange();">
		            		<option class="explotacion-null" value="" disabled selected>Selecciona una explotación</option>
			            	<c:forEach items="${explotacionesGanadero}" var="explotacion">
			            		<option 
			            		class="explotacion-${explotacion.id}" 
			            		value="${explotacion.id}">
			            			<c:out value="${explotacion.name}"/></option>
			            	</c:forEach>
			            </select>
			            <c:if test="${valid}">
			                <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
			            </c:if>
			            <c:if test="${status.error}">
			                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
			                <span class="help-inline">${status.errorMessage}</span>
			            </c:if>
			        </div>
			    </div> 
			</spring:bind>
			<spring:bind path="contrato">
				<c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
				<c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
			    <div class="${cssGroup}">
			        <label class="col-sm-6 control-label">Contrato: </label>
			
			        <div class="col-sm-6">
			            <select disabled name="contrato" id="select-contrato-cita-form-contrato">
			            <option value="" disabled selected></option>
			            <c:set var="tiposGanadoContrato" value="" />
			            	<c:forEach items="${contratos}" var="contrato">

			            	<c:forEach items="${contrato.ganados}" var="tipoGanado" varStatus="tiposGanadoLoopStatus">
			            	<c:choose>
			            		<c:when test="${tiposGanadoLoopStatus.isFirst()}" >
									<c:set var="tiposGanadoContrato" value="${tiposGanadoContrato}${tipoGanado.tipoGanado}"/>
								</c:when>
								<c:otherwise>
									<c:set var="tiposGanadoContrato" value="${tiposGanadoContrato}, ${tipoGanado.tipoGanado}"/>
								</c:otherwise>
							</c:choose> 
			            	</c:forEach>
			            		<option disabled style="display: none;"
			            		class="explotacion-${contrato.explotacionGanadera.id}" 
			            		value="${contrato.id}">
			            			<c:out value="${contrato.veterinario.firstName} ${contrato.veterinario.lastName} (${tiposGanadoContrato})"/></option>
			            	</c:forEach>
			            </select>
			            <c:if test="${valid}">
			                <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
			            </c:if>
			            <c:if test="${status.error}">
			                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
			                <span class="help-inline">${status.errorMessage}</span>
			            </c:if>
			        </div>
			    </div> 
			</spring:bind>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-6 col-sm-6">
                        <button disabled id="select-contrato-cita-form-button" class="btn btn-default" type="submit">Solicitar</button>
            </div>
        </div>
    </form:form>