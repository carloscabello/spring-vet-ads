<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="Explotaciones ganaderas">

<c:if test="${message != null}"><c:out value="${message}"/></c:if>

	<table id="expGanaderaTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 200px;">Nombre</th>
            <th style="width: 140px;">Número de registros</th>
            <th style="width: 140px;">Término municipal</th>
            <th style="width: 180px;">Ganados</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${contratos}" var="contrato">
            <tr>
            	<td>
                    <c:out value="${contrato.explotacionGanadera.name}"/>
                </td> 
                <td>
                    <c:out value="${contrato.explotacionGanadera.numeroRegistro}"/>
                </td>
                <td>
                    <c:out value="${contrato.explotacionGanadera.terminoMunicipal}"/>
                </td>
                <td>
                     <c:forEach var="ganado" items="${todosTiposG}">
                     <c:set var="contiene" value="${false}"/>
                       <c:forEach var="ganadoContrato" items="${contrato.ganados}">
                       <c:if test="${ganado == ganadoContrato}">
                       <c:set var="contiene" value="${true}"/>
                       </c:if>
                       </c:forEach>
                       
                       <c:choose>
                       <c:when test="${contiene}">
                       <spring:url value="/explotacion-ganadera/{expId}/ganado/{ganadoId}/animal/esArchivado/false/list" var="ganadoUrl">
                          <spring:param name="ganadoId" value="${ganado.id}"/>
                          <spring:param name="expId" value="${contrato.explotacionGanadera.id}"/>
                       </spring:url>
                       <a href="${fn:escapeXml(ganadoUrl)}"><img height="25px" alt="<c:out value="${ganado.tipoGanado}"/>" src="/resources/images/fotosGanado/<c:out value="${ganado.tipoGanado}"/>Y.png"></a>
                       </c:when>
                       
                       <c:when test="${!contiene}">
                       <img height="25px" alt="<c:out value="${ganado.tipoGanado}"/>" src="/resources/images/fotosGanado/<c:out value="${ganado.tipoGanado}"/>N.png"></a>
                       </c:when>
                       </c:choose>
                     </c:forEach>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>