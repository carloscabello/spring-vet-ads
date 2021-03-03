<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Lista Veterinarios">

	<table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 200px;">Nombre</th>
            <th style="width: 160px;">Disponibilidad</th>
            <th style="width: 120px;">Ciudad</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${Veterinarios}" var="vets">
            <tr>
            	<td>
            		<spring:url value="/veterinario/{vetId}/show" var="vetUrl">
                        <spring:param name="vetId" value="${vets.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(vetUrl)}"><c:out value="${vets.firstName} "/> <c:out value="${vets.lastName}"/></a>
                </td> 
                <td>
                <c:choose>
                <c:when test="${vets.esDisponible}">
                	Está disponible
                </c:when>
                <c:otherwise>
                	No está disponible
                </c:otherwise>
                </c:choose>
                </td>
                <td>
                    <c:out value="${vets.city}"/>
                </td>       
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>