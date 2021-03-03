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
        	<th style="width: 150px;">Nombre</th>
            <th style="width: 140px;">Número de registros</th>
            <th style="width: 140px;">Término municipal</th>
            <th style="width: 180px;">Ganados</th>
            <th style="width: 50px;"></th>
            <th style="width: 50px;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${expGanaderas}" var="exps">
            <tr>
            	<td>
                    <c:out value="${exps.name}"/>
                </td> 
                <td>
                    <c:out value="${exps.numeroRegistro}"/>
                </td>
                <td>
                    <c:out value="${exps.terminoMunicipal}"/>
                </td>
                <td>
                     <c:forEach var="ganado" items="${todosTiposG}">
                       <spring:url value="/explotacion-ganadera/{expId}/ganado/{ganadoId}/animal/esArchivado/false/list" var="ganadoUrl">
                          <spring:param name="ganadoId" value="${ganado.id}"/>
                          <spring:param name="expId" value="${exps.id}"/>
                       </spring:url>
                       <a href="${fn:escapeXml(ganadoUrl)}"><img height="25px" alt="<c:out value="${ganado.tipoGanado}"/>" src="/resources/images/fotosGanado/<c:out value="${ganado.tipoGanado}"/>N.png"></a>
                     </c:forEach>
                </td>
                <td>
                     <spring:url value="/ganadero/explotacion-ganadera/{expId}/archivarExpGanadera" var="archivarExpGanaderaUrl">
                          <spring:param name="expId" value="${exps.id}"/>
                     </spring:url>
                     <a href="${fn:escapeXml(archivarExpGanaderaUrl)}"><input type="button" value="Archivar"></a>
                </td>
                <td>
                     <spring:url value="/ganadero/explotacion-ganadera/{expId}/contrato/list" var="verContratosUrl">
                          <spring:param name="expId" value="${exps.id}"/>
                     </spring:url>
                     <a href="${fn:escapeXml(verContratosUrl)}"><input type="button" value="Ver contratos"></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <a href="/ganadero/explotacion-ganadera/new"><input class="btn btn-default" type="button" value="Crear una nueva explotación ganadera"/></a>
     
</petclinic:layout>