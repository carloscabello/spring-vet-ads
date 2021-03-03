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
<%@ taglib prefix="vetADS" tagdir="/WEB-INF/tags/tagsADS" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	
<petclinic:layout pageName="Listar recetas">

	<jsp:body>
	<c:if test="${message != null}"><c:out value="${message}"/><br></br></c:if>
		
		<br/>
		<h2>Lista de recetas</h2>
		<br/>
		
		<div id="listaRecetas"  style="display:block;">
		<table id="listaRecetasTable" class="table table-striped">
			<thead>
			<tr>
			<sec:authorize access="hasAnyAuthority('ganadero')">
				<th>Veterinario</th>
			</sec:authorize>
			<sec:authorize access="hasAnyAuthority('veterinario')">
				<th>Ganadero</th>
			</sec:authorize>
				<th style="width: 300px;">Descripcion</th>
				<th style="width: 240px;">Fecha</th>
				<th style="width: 100px;">¿Facturada?</th>
				<th style="width: 200px"> </th>
      </tr>
			</thead>
			<tbody>
				<c:forEach items="${recetas}" var="receta">
					<tr>
						<sec:authorize access="hasAnyAuthority('ganadero')">
							<td><c:out value="${receta.cita.contrato.veterinario.firstName} ${receta.cita.contrato.veterinario.lastName}"/></td>
						</sec:authorize>
						<sec:authorize access="hasAnyAuthority('veterinario')">
							<td><c:out value="${receta.cita.contrato.explotacionGanadera.ganadero.firstName} ${receta.cita.contrato.explotacionGanadera.ganadero.lastName}"/></td>
						</sec:authorize>
						<td><c:out value="${receta.descripcion}"/></td>
						<td><fmt:formatDate value="${receta.fechaRealizacion}" type="date" pattern="dd/MM/yyyy hh:mm a"/></td>
						<td>
                            <c:choose>
                            <c:when test="${receta.esFacturado==true}">
                               <img height="25px" alt="SÃ­" src="/resources/images/tickverde.png"/>
                            </c:when>
                            <c:otherwise>
                               <img height="25px" alt="No" src="/resources/images/cruzroja.png"/>
                            </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                           <spring:url value="/receta/{recetaId}/show" var="showRecetaUrl">
                              <spring:param name="recetaId" value="${receta.id}"/>
                           </spring:url>
                           <a href="${fn:escapeXml(showRecetaUrl)}"><input class="btn btn-default" type="button" value="Mostrar receta"/></a>
                        </td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<sec:authorize access="hasAnyAuthority('veterinario')">
		<a href="/veterinario/receta/new"><input class="btn btn-default" type="button" value="Hacer una nueva receta"/></a>
     	</sec:authorize>
		</div>
	</jsp:body>
</petclinic:layout>