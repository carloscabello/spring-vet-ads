<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Visualizar veterinario">


	<table id="showGanaderoTable" class="table table-striped">
	<h1>Datos del ganadero</h1>
        <tbody>
        	<tr><td><label>Nombre</label><br/></td>
        	<td><label>Apellidos</label></td>
        	<td><label>Teléfono</label></td>
        	<td><label>Email</label></td>
        	<td><label>Provincia</label></td>
        	<td><label>Ciudad</label></td>
        	<td><label>Ganados que trata</label></td>
        	
        	<sec:authorize access="hasAnyAuthority('veterinario')">
        	<td><label>Código postal</label></td>
        	<td><label>Dirección</label></td>
        	</sec:authorize>
        	
        	</tr>
        	<tr>
        	<sec:authorize access="hasAnyAuthority('ganadero')">
        	<td><c:out value="${contratoMostrado.veterinario.firstName}"/></td>
        	<td><c:out value="${contratoMostrado.veterinario.lastName}"/></td>
        	<td><c:out value="${contratoMostrado.veterinario.telephone}"/></td>
        	<td><c:out value="${contratoMostrado.veterinario.mail}"/></td>
        	<td><c:out value="${contratoMostrado.veterinario.province}"/></td>
        	<td><c:out value="${contratoMostrado.veterinario.city}"/></td>
        	<td><c:forEach items="${contratoMostrado.veterinario.tiposGanado}" var="tiposGanado" varStatus="tiposGanadoStatus">
        	    <c:out value="${tiposGanado.tipoGanado}"/><c:if test="${!tiposGanadoStatus.isLast()}">,&nbsp;</c:if>
        	</c:forEach></td>
        	</sec:authorize>
        	
        	<sec:authorize access="hasAnyAuthority('veterinario')">
        	<td><c:out value="${contratoMostrado.explotacionGanadera.ganadero.firstName}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.ganadero.lastName}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.ganadero.telephone}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.ganadero.mail}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.ganadero.province}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.ganadero.city}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.ganadero.postalCode}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.ganadero.address}"/></td>
        	</sec:authorize>
  			</tr>
        </tbody>
        </table>
        <table id="showExpGanaderaTable" class="table table-striped">
        <h1>Datos de la explotación ganadera</h1>
        <tbody>
        
        	<tr><td><label>Nombre</label><br/></td>
        	<td><label>Número de registro</label><br/></td>
        	<td><label>Término municipal</label></td>
        	</tr>
        	<tr>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.name}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.numeroRegistro}"/></td>
        	<td><c:out value="${contratoMostrado.explotacionGanadera.terminoMunicipal}"/></td>
  			</tr>
        </tbody>
         </table>
        <table id="showFechasTable" class="table table-striped">
        <h1>Datos del contrato</h1>
        <tbody>
        <tr><td><label>Fecha petición</label><br/></td>
        	<td><label>Fecha inicio </label></td>
        	<td><label>Fecha finalización</label></td>
        	<td><label>Ganados del contrato</label></td>
        	</tr><tr>
        	<td><c:out value="${contratoMostrado.fechaPeticion}"/></td>
        	<td><c:out value="${contratoMostrado.fechaInicial}"/></td>
        	<td><c:out value="${contratoMostrado.fechaFinal}"/></td>
        	<td><c:forEach items="${contratoMostrado.ganados}" var="ganado" varStatus="ganadoStatus">
        	    <c:out value="${ganado.tipoGanado}"/><c:if test="${!ganadoStatus.isLast()}">,&nbsp;</c:if>
        	</c:forEach></td>
        	</tr>
        	</tbody>
    </table>
    <sec:authorize access="hasAnyAuthority('veterinario')">
    <c:if test="${contratoMostrado.estado eq 'PENDIENTE'}">
    <spring:url value="/contrato/{contratoId}/accept" var="aceptarContrato">
                          <spring:param name="contratoId" value="${contratoMostrado.id}"/>
                     </spring:url>
                     <a href="${fn:escapeXml(aceptarContrato)}"><input type="button" class="btn btn-default" value="Aceptar"></a>
    <spring:url value="/contrato/{contratoId}/refuse" var="rechazarContrato">
                          <spring:param name="contratoId" value="${contratoMostrado.id}"/>
                     </spring:url>
                     <a href="${fn:escapeXml(rechazarContrato)}"><input type="button" class="btn btn-default" value="Rechazar"></a>
    </c:if>
    </sec:authorize>
    
    <sec:authorize access="hasAnyAuthority('ganadero')">
    <c:if test="${contratoMostrado.estado eq 'ACEPTADO'}">
    <spring:url value="/contrato/{contratoId}/edit" var="editContrato">
                          <spring:param name="contratoId" value="${contratoMostrado.id}"/>
                     </spring:url>
                     <a href="${fn:escapeXml(editContrato)}"><input type="button" class="btn btn-default" value="Añadir ganados al contrato"></a>
    </c:if>
    </sec:authorize>
    
    <c:if test="${contratoMostrado.estado eq 'ACEPTADO'}">
    <spring:url value="/contrato/{contratoId}/conclude" var="finalizarContrato">
                          <spring:param name="contratoId" value="${contratoMostrado.id}"/>
                     </spring:url>
                     <a href="${fn:escapeXml(finalizarContrato)}"><input type="button" class="btn btn-default" value="Finalizar"></a>
    </c:if>
</petclinic:layout>