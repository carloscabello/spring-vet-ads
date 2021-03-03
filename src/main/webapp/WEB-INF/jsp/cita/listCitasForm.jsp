<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ page import="java.util.*" %>
<%@ taglib prefix="vetADS" tagdir="/WEB-INF/tags/tagsADS" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type ="text/javascript">
	function mostrarcitasAceptadas() {
		document.getElementById('citasAceptadas').style.display="block";
		document.getElementById('citasPendientes').style.display="none";
		document.getElementById('citasTodas').style.display="none";
		/*Grupo de botones*/
		document.getElementById('citasAceptadasCheck').classList.add("selected");
		document.getElementById('citasPendientesCheck').classList.remove("selected");
		document.getElementById('citasTodasCheck').classList.remove("selected");
		document.getElementById('citasAceptadasCheck').disabled = true;
		document.getElementById('citasPendientesCheck').disabled = false;
		document.getElementById('citasTodasCheck').disabled = false;
	}
	function mostrarcitasPendientes() {
		document.getElementById('citasAceptadas').style.display="none";
		document.getElementById('citasPendientes').style.display="block";
		document.getElementById('citasTodas').style.display="none";
		/*Grupo de botones*/
		document.getElementById('citasAceptadasCheck').classList.remove("selected");
		document.getElementById('citasPendientesCheck').classList.add("selected");
		document.getElementById('citasTodasCheck').classList.remove("selected");
		document.getElementById('citasAceptadasCheck').disabled = false;
		document.getElementById('citasPendientesCheck').disabled = true;
		document.getElementById('citasTodasCheck').disabled = false;
	}
	function mostrarcitasTodas() {
		document.getElementById('citasAceptadas').style.display="none";
		document.getElementById('citasPendientes').style.display="none";
		document.getElementById('citasTodas').style.display="block";
		/*Grupo de botones*/
		document.getElementById('citasAceptadasCheck').classList.remove("selected");
		document.getElementById('citasPendientesCheck').classList.remove("selected");
		document.getElementById('citasTodasCheck').classList.add("selected");
		document.getElementById('citasAceptadasCheck').disabled = false;
		document.getElementById('citasPendientesCheck').disabled = false;
		document.getElementById('citasTodasCheck').disabled = true;
	}
</script>
<petclinic:layout pageName="Listar citas">
	
	<jsp:body>
		<h1>Citas</h1>
				<br/>
		<sec:authorize access="hasAnyAuthority('ganadero')">
		<p>${message}</p>
			<h2>Solicitar nueva cita</h2>
		    <jsp:include page="selectContratoCitaForm.jsp"/> 
		</sec:authorize>
		<br/>
		<h2>Lista de citas</h2>
		<div class="btn-group-wrap">
			<div id="btn-group" class="btn-group mx-auto">
				<button type="button" id="citasAceptadasCheck" class="selected" disabled onclick="mostrarcitasAceptadas();">Aceptadas</button>
				<button type="button" id="citasPendientesCheck" onclick="mostrarcitasPendientes();">Pendientes</button>
				<button type="button" id="citasTodasCheck" onclick="mostrarcitasTodas();">Todas</button>
			</div>
		</div>
		<br/>
		<div id="citasAceptadas"  style="display:block;">
		<h3>Próximas citas aceptadas</h3>
		<table id="citasAceptadasTable" class="table table-striped">
			<thead>
			<sec:authorize access="hasAnyAuthority('ganadero')">
				<th>Veterinario</th>
			</sec:authorize>
				<th>Explotacion</th>
				<th>Motivo</th>
				<th>Inicio</th>
				<th>Fin</th>
				<!--  <th>Estado</th>-->
			</thead>
			<tbody>
				<c:forEach items="${citasAceptadas}" var="citaAceptada">
					<tr>
						<sec:authorize access="hasAnyAuthority('ganadero')">
							<td><c:out value="${citaAceptada.contrato.veterinario.firstName} ${citaAceptada.contrato.veterinario.lastName}"/></td>
						</sec:authorize>
						<td><c:out value="${citaAceptada.contrato.explotacionGanadera.name}"/></td>
						<td><c:out value="${citaAceptada.motivo}"/></td>
						<td><fmt:formatDate value="${citaAceptada.fechaHoraInicio}" type="date" pattern="dd/MM/yyyy hh:mm"/></td>
						<td><fmt:formatDate value="${citaAceptada.fechaHoraFin}" type="date" pattern="dd/MM/yyyy hh:mm"/></td>
						<!--  <td><c:out value="${citaAceptada.estado}"/></td>-->
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		</div>
		<div id="citasPendientes"  style="display:none;">
		<h3>Próximas citas pendientes de gestionarse</h3>
		<table id="citasPendientesTable" class="table table-striped">
			<thead>
			<sec:authorize access="hasAnyAuthority('ganadero')">
				<th>Veterinario</th>
			</sec:authorize>
				<th>Explotacion</th>
				<th>Motivo</th>
				<th>Inicio</th>
				<th>Fin</th>
			<sec:authorize access="hasAnyAuthority('veterinario')">
			<th></th>
			</sec:authorize>
				<!--  <th>Estado</th>-->
			</thead>
			<tbody>
				<c:forEach items="${citasPendientes}" var="citaPendiente">
					<tr>
						<sec:authorize access="hasAnyAuthority('ganadero')">
							<td><c:out value="${citaPendiente.contrato.veterinario.firstName} ${citaPendiente.contrato.veterinario.lastName}"/></td>
						</sec:authorize>
						<td><c:out value="${citaPendiente.contrato.explotacionGanadera.name}"/></td>
						<td><c:out value="${citaPendiente.motivo}"/></td>
						<td><fmt:formatDate value="${citaPendiente.fechaHoraInicio}" type="date" pattern="dd/MM/yyyy hh:mm"/></td>
						<td><fmt:formatDate value="${citaPendiente.fechaHoraFin}" type="date" pattern="dd/MM/yyyy hh:mm"/></td>
						<sec:authorize access="hasAnyAuthority('veterinario')">
							<td><nobr>
							<button type="button" id="aceptar-cita-${citaPendiente.id}" onclick="location.href='/veterinario/cita/${citaPendiente.id}/accept';">Aceptar</button>
							<button type="button" id="rechazar-cita-${citaPendiente.id}" onclick="location.href='/veterinario/cita/${citaPendiente.id}/decline';">Rechazar</button>
							</nobr></td>
						</sec:authorize>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		</div>
		<div id="citasTodas"  style="display:none;">
		<h3>Todas las citas</h3>
		<table id="citasTodasTable" class="table table-striped">
			<thead>
			<sec:authorize access="hasAnyAuthority('ganadero')">
				<th>Veterinario</th>
			</sec:authorize>
				<th>Explotacion</th>
				<th>Motivo</th>
				<th>Inicio</th>
				<th>Fin</th>
				<th>Estado</th>
				<th>Justificación de rechazo</th>
			</thead>
			<tbody>
				<c:forEach items="${citasTodas}" var="cita">
					<tr>
						<sec:authorize access="hasAnyAuthority('ganadero')">
							<td><c:out value="${cita.contrato.veterinario.firstName} ${cita.contrato.veterinario.lastName}"/></td>
						</sec:authorize>
						<td><c:out value="${cita.contrato.explotacionGanadera.name}"/></td>
						<td><c:out value="${cita.motivo}"/></td>
						<td><fmt:formatDate value="${cita.fechaHoraInicio}" type="date" pattern="dd/MM/yyyy hh:mm"/></td>
						<td><fmt:formatDate value="${cita.fechaHoraFin}" type="date" pattern="dd/MM/yyyy hh:mm"/></td>
						<td><c:out value="${cita.estado}"/></td>
						<td><c:out value="${cita.rechazoJustificacion}"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		</div>
	</jsp:body>
</petclinic:layout>