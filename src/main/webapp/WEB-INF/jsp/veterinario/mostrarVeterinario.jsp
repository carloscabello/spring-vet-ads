<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Visualizar veterinario">


	<table id="verVetTable" class="table table-striped">
        <tbody>
        	<tr><td><label>Nombre</label><br/></td>
        	<td><label>Apellidos </label></td>
        	<td><label>Teléfono </label></td>
        	<td><label>Email </label></td>
        	<td><label>Provincia </label></td>
        	<td><label>Ciudad </label></td>
        	<td><label>Ganado que trata</label></td></tr>
        	<tr>
        	<td><c:out value="${vets.firstName}"/></td>
        	<td><c:out value="${vets.lastName}"/></td>
        	<td><c:out value="${vets.telephone}"/></td>
        	<td><c:out value="${vets.mail}"/></td>
        	<td><c:out value="${vets.province}"/></td>
        	<td><c:out value="${vets.city}"/></td>
        	<td><c:forEach items="${tiposGanado}" var="tiposGanado">
                
                    <c:out value="${tiposGanado.tipoGanado}"/></br>
                    
                
            </c:forEach>
            </td>
            <td><spring:url value="/veterinario/{vetId}/contrato" var="vetUrl">
                        <spring:param name="vetId" value="${vets.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(vetUrl)}">Contratar veterinario</a>
            </td></tr>
        </tbody>
    </table>
</petclinic:layout>