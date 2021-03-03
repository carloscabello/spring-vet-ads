<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="lote">

            Lote<br></br>

            Identificador:<c:out value="${identificadorLote}"/><br></br>
            Fecha identificación:<c:out value="${animal.fechaIdentificacion}"/><br></br>
            Fecha nacimiento:<c:out value="${animal.fechaNacimiento}"/><br></br>
            Número de Machos actuales:<c:out value="${machos}"/><br></br>
            Número de hembras actuales:<c:out value="${hembras}"/><br></br>
            Tipo ganado:<c:out value="${animal.tipoGanado.tipoGanado}"/><br></br>
            ¿Los animales han sido comprados?:
            <c:choose><c:when test="${animal.comprado == true}">
             Sí
            <div>
            <br></br>
            Datos de la compra<br></br>
            Procedencia:<c:out value="${animal.procedencia}"/><br></br>
            Fecha entrada:<c:out value="${animal.fechaEntrada}"/><br></br>
            </div>
            </c:when>
            <c:otherwise>
            No
            </c:otherwise>
            </c:choose>
            <br></br>
            
        <sec:authorize access="hasAnyAuthority('veterinario')">
        <table id="animalTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 200px;">Identificador animal</th>
            <th style="width: 160px;">Sexo</th>
            <th style="width: 160px;">¿Es un animal archivado?</th>
            <th style="width: 120px;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${animales}" var="animales">
            <tr>
            	<td>
                    <c:out value="${animales.identificadorAnimal}"/>
                </td> 
                <td>
                    <c:out value="${animales.sexo}"/>
                </td>
                <td>
                    <c:choose>
                    <c:when test="${!animales.esArchivado}">
                    No
                    </c:when>
                    <c:otherwise>
                    Sí
                    </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                    <c:when test="${!animales.esArchivado}">
                    <spring:url value="/explotacion-ganadera/{expId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new" var="animalHistoricoUrl">
                          <spring:param name="animalId" value="${animales.id}"/>
                          <spring:param name="ganadoId" value="${animales.tipoGanado.id}"/>
                          <spring:param name="expId" value="${expGanaderaId}"/>
                     </spring:url>
                    <a href="${fn:escapeXml(animalHistoricoUrl)}"><input type="button" class="btn btn-default" value="Archivar"></a>
                    </c:when>
                    <c:otherwise>
                     <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/show" var="animalUrl">
                        <spring:param name="animalId" value="${animales.id}"/>
                        <spring:param name="ganadoId" value="${animales.tipoGanado.id}"/>
                        <spring:param name="expGanaderaId" value="${expGanaderaId}"/>
                    </spring:url>
                     <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/delete" var="deleteAnimalUrl">
                        <spring:param name="animalId" value="${animales.id}"/>
                        <spring:param name="ganadoId" value="${animales.tipoGanado.id}"/>
                        <spring:param name="expGanaderaId" value="${expGanaderaId}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(animalUrl)}"><input class="btn btn-primary" type="button" value="Mostrar"></a>
                    <a href="${fn:escapeXml(deleteAnimalUrl)}"><button type="button" id="eliminarAnimal" class="btn btn-danger">Eliminar</button></a>
                    </c:otherwise>
                    </c:choose>
                </td>       
            </tr>
        </c:forEach>
        </tbody>
        </table>
        
        <spring:url value="/explotacion-ganadera/{expId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new" var="loteHistoricoUrl">
                          <spring:param name="ganadoId" value="${animal.tipoGanado.id}"/>
                          <spring:param name="expId" value="${expGanaderaId}"/>
                          <spring:param name="loteId" value="${lote.id}"/>
                     </spring:url>
                    <a href="${fn:escapeXml(loteHistoricoUrl)}"><input class="btn btn-default" type="button" value="Archivar lote completo"></a>
        </sec:authorize>
</petclinic:layout>