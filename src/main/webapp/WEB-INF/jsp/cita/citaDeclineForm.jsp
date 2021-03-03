<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Rechazar cita">
	<jsp:body>
		<form:form modelAttribute="cita" class="form-horizontal" id="create-cita-form">
		        <div class="form-group has-feedback">
	 	            <petclinic:inputDisabled label="Fecha y hora de inicio" name="fechaHoraInicio"/>
					<petclinic:inputDisabled label="Fecha y hora de fin" name="fechaHoraFin"/>
					<petclinic:inputDisabled label="Motivo" name="motivo"/>
					<petclinic:inputDisabled label="Estado" name="estado"/>
		            <petclinic:inputField label="Justificación de rechazo" name="rechazoJustificacion"/>
		        </div>
		        <div class="form-group">
		            <div class="col-sm-offset-2 col-sm-10">
		                        <button class="btn btn-default" type="submit">Rechazar Cita</button>
		            </div>
		        </div>
		    </form:form>
	</jsp:body>
</petclinic:layout>