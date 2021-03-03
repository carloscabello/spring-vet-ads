<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="animal">

<script type ="text/javascript">
	function mostrarAnimales() {
		document.getElementById('tablaAnimal').style.display="block";
		document.getElementById('tablaLote').style.display="none";
	}
	function mostrarLote() {
		document.getElementById('tablaAnimal').style.display="none";
		document.getElementById('tablaLote').style.display="block";
	}
</script>
<label>Ganado <c:out value="${tipoGanado.tipoGanado}"/>: 
<c:choose>
<c:when test="${!esArchivado}">Animales actuales </c:when>
<c:otherwise>Animales archivados </c:otherwise></c:choose></label>

<spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/esArchivado/{esArchivado}/list" var="animalesArchivadosUrl">
                        <spring:param name="expGanaderaId" value="${expId}"/>
                        <spring:param name="ganadoId" value="${tipoGanado.id}"/>
                        <spring:param name="esArchivado" value="${!esArchivado}"/>
                    </spring:url>
<a href="${fn:escapeXml(animalesArchivadosUrl)}"><input class="btn btn-success" type="button" value="<c:choose><c:when test="${!esArchivado}">Animales archivados</c:when><c:otherwise>Animales actuales</c:otherwise></c:choose>"></a>

<div style="float:right;">
<input class="btn btn-success" type="button" id="animalesCheck" name="animales" value="Animales" onclick="mostrarAnimales();"/>
<input class="btn btn-success" type="button" id="lotesCheck" name="lotes" value="Lotes" onclick="mostrarLote();"/>
</div>
<br></br>
<div id="tablaAnimal"  style="display:block;">
<table id="animalTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 200px;">Identificador animal</th>
            <th style="width: 160px;">Especie</th>
            <th style="width: 120px;">fecha de nacimiento</th>
            <th style="width: 120px;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${animales}" var="animales">
            <tr>
            	<td>
                    <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/show" var="animalUrl">
                        <spring:param name="animalId" value="${animales.id}"/>
                        <spring:param name="expGanaderaId" value="${expId}"/>
                        <spring:param name="ganadoId" value="${tipoGanado.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(animalUrl)}"><c:out value="${animales.identificadorAnimal}"/></a>
                </td> 
                <td>
                    <c:out value="${animales.tipoGanado.tipoGanado}"/>
                </td>
                <td>
                    <c:out value="${animales.fechaNacimiento}"/>
                </td>
                <td>
                	<c:if test="${animales.esArchivado}">
						<sec:authorize access="hasAnyAuthority('veterinario')">
	                     <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/delete" var="deleteAnimalUrl">
	                        <spring:param name="animalId" value="${animales.id}"/>
	                        <spring:param name="ganadoId" value="${animales.tipoGanado.id}"/>
	                        <spring:param name="expGanaderaId" value="${expGanaderaId}"/>
	                    </spring:url>
						<a href="${fn:escapeXml(deleteAnimalUrl)}"><button type="button" id="eliminarAnimal" class="btn btn-danger">Eliminar</button></a>
						</sec:authorize>
					</c:if>
                </td>       
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${!esArchivado}">
    <sec:authorize access="hasAnyAuthority('veterinario')">
     <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/new" var="newAnimalUrl">
                    <spring:param name="expGanaderaId" value="${expId}"/>
                    <spring:param name="ganadoId" value="${tipoGanado.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(newAnimalUrl)}"><input class="btn btn-default" type="button" value="Añade un nuevo animal"></a><p></p>
    </sec:authorize>
    </c:if>
    </div>
    
    <div id=tablaLote style="display:none;">
    <table id="loteTable" class="table table-striped" >
        <thead>
        <tr>
        	<th style="width: 200px;">Identificador Lote</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${lote}" var="lote">
            <tr>
            	<td>
                    <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/lote/{loteId}/show" var="loteUrl">
                        <spring:param name="loteId" value="${lote.id}"/>
                        <spring:param name="expGanaderaId" value="${expId}"/>
                        <spring:param name="ganadoId" value="${tipoGanado.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(loteUrl)}"><c:out value="${lote.identificadorLote}"/></a>
                </td> 
    
                
                     
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${!esArchivado}">
    <sec:authorize access="hasAnyAuthority('veterinario')">
    <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/lote/new" var="newLoteUrl">
                    <spring:param name="expGanaderaId" value="${expId}"/>
                    <spring:param name="ganadoId" value="${tipoGanado.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(newLoteUrl)}"><input class="btn btn-default" type="button" value="Añade un nuevo lote"/></a>
    </sec:authorize>
    </c:if>
    </div>
    
    <c:if test="${!esArchivado}">
    <sec:authorize access="hasAnyAuthority('veterinario')">
    <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new" var="newAnimalArchivadoUrl">
                    <spring:param name="expGanaderaId" value="${expId}"/>
                    <spring:param name="ganadoId" value="${tipoGanado.id}"/>
                    </spring:url>
    <a href="${fn:escapeXml(newAnimalArchivadoUrl)}"><input class="btn btn-default" type="button" value="Archivar todo el ganado <c:out value="${tipoGanado.tipoGanado}"/>"/></a>
    </sec:authorize>
    </c:if>
   
                    
    
                    
</petclinic:layout>