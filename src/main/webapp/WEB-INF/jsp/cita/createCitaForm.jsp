<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Solicitar cita">

	<jsp:attribute name="customScript">
	        <script>
	            $(function () {
	                $("#fecha").datepicker();*/
	            });
	        </script>
	</jsp:attribute>
	
	<jsp:body>
		<form:form modelAttribute="citaForm" class="form-horizontal" id="create-cita-form">
		        <div class="form-group has-feedback">
		            <petclinic:inputField label="Fecha" name="fecha"/>
					<petclinic:inputField label="Hora de inicio" name="horaInicio"/>
					<petclinic:inputField label="Duracion" name="duracion"/>
		            <petclinic:inputField label="Motivo" name="motivo"/>
		        </div>
		        <div class="form-group">
		            <div class="col-sm-offset-2 col-sm-10">
		                        <button class="btn btn-default" type="submit">Solicitar Cita</button>
		            </div>
		        </div>
		    </form:form>
	</jsp:body>
</petclinic:layout>